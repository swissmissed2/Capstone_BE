package com.capstonebe.capstonebe.notify.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.notify.dto.NotifyResponse;
import com.capstonebe.capstonebe.notify.entity.Notify;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.notify.repository.EmitterRepository;
import com.capstonebe.capstonebe.notify.repository.NotifyRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class NotifyService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;

    // SseEmitter는 클라이언트에게 이벤트를 전송하는 역할 수행
    // sse 구독 설정
    public SseEmitter subscribe(String email, String lastEventId) {
        String emitterId = makeTimeIncludeId(email);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(email);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + email + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, email, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String email) {
        return email + "_" + System.currentTimeMillis();
    }

    // SseEmitter를 통해 이벤트 전송
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // 지정된 수신자에게 알림 전송(비동기)
    @Async
    public void send(String email, NotifyType notifyType, String content, String url) {

        try {
            User receiver = userRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

            Notify notify = notifyRepository.save(createNotify(receiver, notifyType, content, url));

            //String receiverEmail = receiver.getEmail();
            String eventId = email + "_" + notify.getId() + "_" + System.currentTimeMillis();
            Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(email);
            emitters.forEach(
                    (emitterId, emitter) -> {
                        emitterRepository.saveEventCache(eventId, notify);
                        sendNotification(emitter, eventId, emitterId, NotifyResponse.fromEntity(notify));
                    }
            );
        } catch (Exception e) {
            log.error("알림 전송 실패: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<NotifyResponse> getNotificationsByType(String type, String email, Pageable pageable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        NotifyType notifyType = switch (type) {
            case "keyword" -> NotifyType.KEYWORD;
            case "matching" -> NotifyType.MATCHING;
            default -> throw new CustomException(CustomErrorCode.INVALID_NOTIFY_TYPE);
        };

        Page<Notify> notifications = notifyRepository.findByReceiverAndTypeAndIsReadFalse(user, notifyType, pageable);

        return notifications.map(NotifyResponse::fromEntity);
    }

    @Transactional
    public void readNotify(String email, Long id) {

        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Notify notify = notifyRepository.findByReceiverAndId(receiver, id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOTIFY_NOT_FOUND));

        notify.readNotify();
    }

    @Transactional
    public void deleteNotify(String email, Long id) {

        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Notify notify = notifyRepository.findByReceiverAndId(receiver, id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOTIFY_NOT_FOUND));

        emitterRepository.deleteAllEventCacheByEventId(List.of(id));

        notifyRepository.delete(notify);
    }

    private Notify createNotify(User receiver, NotifyType notifyType, String content, String url) {
        return Notify.builder()
                .receiver(receiver)
                .type(notifyType)
                .content(content)
                .relatedUrl(url)
                .isRead(false)
                .build();
    }

}
