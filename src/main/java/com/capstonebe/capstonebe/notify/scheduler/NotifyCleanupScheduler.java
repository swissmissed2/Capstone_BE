package com.capstonebe.capstonebe.notify.scheduler;

import com.capstonebe.capstonebe.notify.entity.Notify;
import com.capstonebe.capstonebe.notify.repository.EmitterRepository;
import com.capstonebe.capstonebe.notify.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotifyCleanupScheduler {

    private final NotifyRepository notifyRepository;
    private final EmitterRepository emitterRepository;

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    public void deleteOldReadNotifications() {

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        List<Long> ids = notifyRepository.findByCreatedAtBefore(oneWeekAgo)
                .stream()
                .map(Notify::getId)
                .toList();

        emitterRepository.deleteAllEventCacheByEventId(ids);

        notifyRepository.deleteAllById(ids);
    }
}
