package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.notify.service.NotifyService;
import com.capstonebe.capstonebe.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MatchingService {

    private final ItemRepository itemRepository;
    private final NotifyService notifyService;

    public void sendMatchingNotify(AiMatchingResponse response) {

        List<Map<String, String>> result = response.getMatchedItems();

        List<Long> ids = result.stream()
                .map(map -> map.get("item_id"))
                .map(Long::parseLong)
                .toList();

        List<Item> matchedItems = itemRepository.findAllById(ids);

        String content = "등록하신 분실물과 유사한 습득물이 있습니다.";
        String url = "/api/items/" + response.getItemId();

        matchedItems.stream()
                .map(Item::getUser)
                .distinct()
                .peek(User::getEmail)
                .forEach(user ->
                        notifyService.send(user, NotifyType.MATCHING, content, url)
                );
    }

}
