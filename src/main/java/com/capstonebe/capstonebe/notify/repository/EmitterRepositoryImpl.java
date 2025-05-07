package com.capstonebe.capstonebe.notify.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepositoryImpl implements EmitterRepository{

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();   //사용자에게 전송되지 못한 이벤트를 캐시로 저장

    // emitter 저장
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // 이벤트 저장
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    // 해당 회원과 관련된 모든 이벤트 찾기
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // emitter 삭제
    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    // 해당 회원과 관련된 모든 emitter 삭제
    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    // 해당 회원과 관련된 모든 이벤트 지움
    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }

    @Override
    public void deleteAllEventCacheByEventId(List<Long> ids) {

        Set<Long> idSet = new HashSet<>(ids);

        eventCache.keySet().removeIf(key -> {
            try {
                Long notifyId = extractNotifyIdFromEventId(key);
                return idSet.contains(notifyId);
            } catch (Exception e) {
                return false;
            }
        });
    }

    private Long extractNotifyIdFromEventId(String eventId) {
        try {
            String[] parts = eventId.split("_");
            if (parts.length < 3) {
                throw new IllegalArgumentException("이벤트 ID 형식이 잘못되었습니다: " + eventId);
            }
            return Long.parseLong(parts[1]);
        } catch (Exception e) {
            throw new RuntimeException("notifyId 추출 중 오류 발생: " + e.getMessage(), e);
        }
    }

}
