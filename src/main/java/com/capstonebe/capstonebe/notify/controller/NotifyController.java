package com.capstonebe.capstonebe.notify.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.notify.service.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notify")
@AllArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    /**
     * 서버에서 클라이언트로의 단방향 통신만 필요하므로 SSE 사용
     * sse 이벤트 스트림 구독
     * @param user
     * @param lastEventId
     * @return
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal User user,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return notifyService.subscribe(user.getUsername(), lastEventId);
    }

    /**
     * 알림 읽음 처리
     * @param id
     * @param user
     * @return
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> read(@PathVariable Long id, @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        notifyService.readNotify(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }

    /**
     * 알림 삭제
     * @param id
     * @param user
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotify(@PathVariable Long id, @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        notifyService.deleteNotify(user.getUsername(), id);

        return ResponseEntity.ok().build();
    }

    /**
     * 알림 타입별 조회
     * @param type
     * @param user
     * @param pageable
     * @return
     */
    @GetMapping("/{type}")
    public ResponseEntity<?> getNotificationsByType(@PathVariable String type,
                                                    @AuthenticationPrincipal User user, Pageable pageable) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(notifyService.getNotificationsByType(type, user.getUsername(), pageable));
    }

}

