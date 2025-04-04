package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.response.LostItemResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void resisterLostItem(LostItemRegisterRequest lostItemRegisterRequest) {

        Item newItem = LostItemRegisterRequest.toEntity(lostItemRegisterRequest);

        itemRepository.save(newItem);
    }

    @Transactional
    public void editLostItem(LostItemEditRequest request) {
        Item item = itemRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.edit(
                request.getName(),
                request.getDescription(),
                request.getLatitude(),
                request.getLongitude(),
                request.getCategoryId()
        );

        itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        itemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public LostItemResponse getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        return LostItemResponse.fromEntity(item);
    }

}
