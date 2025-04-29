package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.request.MatchingRequest;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.notify.service.NotifyService;
import com.capstonebe.capstonebe.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MatchingService {

    private final ItemRepository itemRepository;
    private final NotifyService notifyService;

    public void sendMatchingNotify(MatchingRequest request) {

        List<Item> lostItems = itemRepository.findAllById(request.getLostItemId());

        List<User> receivers = lostItems.stream()
                .map(Item::getUser)
                .distinct()
                .collect(Collectors.toList());

        String content = "등록하신 분실물과 유사한 습득물이 있습니다.";
        String url = "/api/items/" + request.getFoundItemId();

        receivers.forEach(user ->
                notifyService.send(user, NotifyType.MATCHING, content, url)
        );
    }

}
