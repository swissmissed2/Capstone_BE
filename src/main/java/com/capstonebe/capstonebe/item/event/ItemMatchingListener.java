package com.capstonebe.capstonebe.item.event;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.entity.Image;
import com.capstonebe.capstonebe.image.repository.ImageRepository;
import com.capstonebe.capstonebe.matching.dto.request.AiMatchingRequest;
import com.capstonebe.capstonebe.matching.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.item.service.AiService;
import com.capstonebe.capstonebe.matching.service.MatchingService;
import com.capstonebe.capstonebe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMatchingListener {

    private final AiService aiService;
    private final MatchingService matchingService;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Async
    @Transactional
    @EventListener
    public void handleItemRegistered(ItemRegisteredEvent event) {

        Item item = itemRepository.findById(event.itemId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        List<String> imageUrls = imageRepository.findAllByItem(item).stream()
                .map(Image::getPath)
                .toList();

        AiMatchingRequest request = AiMatchingRequest.builder()
                .itemId(item.getId())
                .imageUrls(imageUrls)
                .category(event.categoryName())
                .type(event.typeName())
                .build();

        AiMatchingResponse response = aiService.requestMatchingFromAI(request);

        List<Long> ids = response.getMatchedItems().stream()
                .map(m -> Long.parseLong(m.get("item_id")))
                .toList();

        List<Item> matchedItems = itemRepository.findAllById(ids);

        List<String> emails = matchedItems.stream()
                .map(Item::getUser)
                .map(User::getEmail)
                .distinct()
                .toList();

        matchingService.sendMatchingNotify(response, emails);
    }
}
