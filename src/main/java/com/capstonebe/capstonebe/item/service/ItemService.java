package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.response.LostItemResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public LostItemResponse resisterLostItem(LostItemRegisterRequest lostItemRegisterRequest) {

        Item newItem = Item.builder()
                .userId(1L)
                .type(ItemType.LOST_ITEM)
                .name(lostItemRegisterRequest.getName())
                .latitude(lostItemRegisterRequest.getLatitude())
                .longitude(lostItemRegisterRequest.getLongitude())
                .time(lostItemRegisterRequest.getTime())
                .description(lostItemRegisterRequest.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .categoryId(lostItemRegisterRequest.getCategoryId())
                .build();

        return LostItemResponse.fromEntity(itemRepository.save(newItem));
    }

    @Transactional
    public LostItemResponse editLostItem(LostItemEditRequest request) {

        Item item = itemRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        item.edit(
                request.getName(),
                request.getDescription(),
                request.getLatitude(),
                request.getLongitude(),
                request.getCategoryId()
        );

        return LostItemResponse.fromEntity(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        itemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public LostItemResponse getItemById(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        return LostItemResponse.fromEntity(item);
    }

    @Transactional(readOnly = true)
    public List<LostItemResponse> getItemsByType(ItemType type) {

        List<Item> items = itemRepository.findByType(type);

        return items.stream()
                .map(LostItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 테스트용
    @Transactional
    public void generateTestItems(int count) {

        for (int i = 1; i <= count; i++) {
            Item item = Item.builder()
                    .userId((long) (i % 5 + 1))
                    .type(ItemType.LOST_ITEM)
                    .name("Test Item " + i)
                    .latitude(37.5 + i * 0.001)
                    .longitude(127.0 + i * 0.001)
                    .time(LocalDateTime.now())
                    .description("This is test item #" + i)
                    .itemState(ItemState.NOT_RETURNED)
                    .categoryId((long) (i % 10 + 1))
                    .build();

            itemRepository.save(item);
        }
    }
}
