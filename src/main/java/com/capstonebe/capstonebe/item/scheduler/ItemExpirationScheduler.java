package com.capstonebe.capstonebe.item.scheduler;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemExpirationScheduler {

    private final ItemRepository itemRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시 실행
    public void expireOldItems() {

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        List<Item> expiredItems = itemRepository.findByCreatedAtBeforeAndState(twoWeeksAgo, ItemState.NOT_RETURNED);

        for (Item item : expiredItems) {
            item.setExpired();
        }

        itemRepository.saveAll(expiredItems);
    }

}
