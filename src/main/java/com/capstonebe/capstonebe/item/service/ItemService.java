package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.category.repository.CategoryRepository;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.service.ImageService;
import com.capstonebe.capstonebe.item.dto.request.FoundItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.response.ItemDetailResponse;
import com.capstonebe.capstonebe.item.dto.response.ItemListResponse;
import com.capstonebe.capstonebe.item.dto.response.ItemResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.event.ItemRegisteredEvent;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import com.capstonebe.capstonebe.itemplace.repository.ItemPlaceRepository;
import com.capstonebe.capstonebe.place.entity.Place;
import com.capstonebe.capstonebe.place.repository.PlaceRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPlaceRepository itemPlaceRepository;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ItemResponse resisterLostItem(LostItemRegisterRequest request, String email) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Item item = Item.builder()
                .user(user)
                .type(ItemType.LOST_ITEM)
                .name(request.getName())
                .time(request.getTime())
                .description(request.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .category(category)
                .build();

        itemRepository.save(item);

        List<Place> places = placeRepository.findAllById(request.getPlaceIds());
        saveItemPlaces(item, places);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPublisher.publishEvent(new ItemRegisteredEvent(item.getId(),
                        category.getName(), ItemType.LOST_ITEM.getName()));
            }
        });

        return ItemResponse.fromEntity(item, places);
    }

    @Transactional
    public ItemResponse registerFoundItem(FoundItemRegisterRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Item item = Item.builder()
                .user(user)
                .type(ItemType.FOUND_ITEM)
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .description(request.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .category(category)
                .build();

        itemRepository.save(item);

        List<Place> places = placeRepository.findAllById(request.getPlaceIds());
        saveItemPlaces(item, places);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                eventPublisher.publishEvent(new ItemRegisteredEvent(item.getId(),
                        category.getName(), ItemType.LOST_ITEM.getName()));
            }
        });

        return ItemResponse.fromEntity(item, places);
    }

    @Transactional
    public ItemResponse editLostItem(LostItemEditRequest request, String email) {

        Item item = itemRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        if (!item.getUser().getEmail().equals(email)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        item.edit(
                request.getName(),
                request.getDescription(),
                request.getLatitude(),
                request.getLongitude(),
                category
        );

        itemPlaceRepository.deleteByItem(item);

        List<Place> places = placeRepository.findAllById(request.getPlaceIds());
        saveItemPlaces(item, places);

        return ItemResponse.fromEntity(item, places);
    }

    @Transactional
    public void deleteItem(Long id, String email) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        if (!item.getUser().getEmail().equals(email)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }

        itemRepository.delete(item);

        itemPlaceRepository.deleteByItem(item);
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        List<Place> places = item.getItemPlaces().stream()
                .map(ItemPlace::getPlace)
                .toList();

        String imageUrl = imageService.getFirstImagePathByItem(item);

        return ItemDetailResponse.from(item, places, imageUrl);
    }

    @Transactional(readOnly = true)
    public Page<ItemListResponse> getItemsByFilter(ItemType type, String placeName, String categoryName,
                                                   String keyword, LocalDate startDate, LocalDate endDate,
                                                   Pageable pageable) {

        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        Page<Item> items = itemRepository.findItemsByFilter(placeName, categoryName, type, keyword, startDate, endDate, pageable);

        return items.map(item -> {
            String imageUrl = imageService.getFirstImagePathByItem(item);
            return ItemListResponse.from(item, imageUrl);
        });
    }


    @Transactional(readOnly = true)
    public Page<ItemResponse> getLostItemsByUser(String email, Pageable pageable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Page<Item> items = itemRepository.findByUser(user, pageable);

        return items.map(item -> {
            List<Place> places = item.getItemPlaces().stream()
                    .map(ItemPlace::getPlace)
                    .toList();
            return ItemResponse.fromEntity(item, places);
        });
    }

    public Page<ItemListResponse> getExpiredItems(Pageable pageable) {

        Page<Item> expiredItems = itemRepository.findByState(ItemState.EXPIRED, pageable);

        return expiredItems.map(item -> {
            String imageUrl = imageService.getFirstImagePathByItem(item);
            return ItemListResponse.from(item, imageUrl);
        });
    }

    private void saveItemPlaces(Item item, List<Place> places) {
        for (Place place : places) {
            ItemPlace itemPlace = ItemPlace.builder()
                    .item(item)
                    .place(place)
                    .build();
            itemPlaceRepository.save(itemPlace);
        }
    }

}
