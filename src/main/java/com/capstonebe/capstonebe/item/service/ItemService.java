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
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import com.capstonebe.capstonebe.itemplace.repository.ItemPlaceRepository;
import com.capstonebe.capstonebe.place.entity.Place;
import com.capstonebe.capstonebe.place.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPlaceRepository itemPlaceRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public LostItemResponse resisterLostItem(LostItemRegisterRequest request) {

        Item item = Item.builder()
                .userId(1L)
                .type(ItemType.LOST_ITEM)
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .time(request.getTime())
                .description(request.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .categoryId(request.getCategoryId())
                .build();

        itemRepository.save(item);

        List<Place> places = placeRepository.findAllById(request.getPlaceIds());

        for (Place place : places) {
            ItemPlace itemPlace = ItemPlace.builder()
                    .item(item)
                    .place(place)
                    .build();
            itemPlaceRepository.save(itemPlace);
        }

        return LostItemResponse.fromEntity(item, places);
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

        // 2. 기존 장소 연결 제거
        itemPlaceRepository.deleteByItem(item);

        // 3. 새로운 장소 연결
        List<Place> newPlaces = placeRepository.findAllById(request.getPlaceIds());

        for (Place place : newPlaces) {
            ItemPlace itemPlace = ItemPlace.builder()
                    .item(item)
                    .place(place)
                    .build();
            itemPlaceRepository.save(itemPlace);
        }

        return LostItemResponse.fromEntity(item, newPlaces);
    }

    @Transactional
    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        itemRepository.delete(item);

        itemPlaceRepository.deleteByItem(item);
    }

    @Transactional(readOnly = true)
    public LostItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        List<Place> places = item.getItemPlaces().stream()
                .map(ItemPlace::getPlace)
                .toList();

        return LostItemResponse.fromEntity(item, places);
    }


    @Transactional(readOnly = true)
    public List<LostItemResponse> getItemsByType(ItemType type) {
        List<Item> items = itemRepository.findByType(type);

        return items.stream()
                .map(item -> {
                    List<Place> places = item.getItemPlaces().stream()
                            .map(ItemPlace::getPlace)
                            .toList();
                    return LostItemResponse.fromEntity(item, places);
                })
                .collect(Collectors.toList());
    }




    // 테스트용
    @Transactional
    public void registerTestItems(int count) {
        List<Place> allPlaces = placeRepository.findAll();

        if (allPlaces.size() < 3) {
            throw new RuntimeException("Place가 최소 3개 이상 있어야 합니다.");
        }

        for (int i = 1; i <= count; i++) {
            Item item = Item.builder()
                    .userId((long) (i % 5 + 1)) // 1~5 사용자 반복
                    .type(ItemType.LOST_ITEM)
                    .name("테스트 아이템 " + i)
                    .description("이것은 테스트용 아이템입니다 #" + i)
                    .latitude(37.5 + i * 0.001)
                    .longitude(127.0 + i * 0.001)
                    .time(LocalDateTime.now().minusDays(i % 7))
                    .itemState(ItemState.NOT_RETURNED)
                    .categoryId((long) (i % 10 + 1)) // 1~10 카테고리
                    .build();

            itemRepository.save(item);

            // 랜덤으로 1~3개 장소 연결
            Collections.shuffle(allPlaces);
            List<Place> selectedPlaces = allPlaces.subList(0, new Random().nextInt(3) + 1);

            for (Place place : selectedPlaces) {
                ItemPlace itemPlace = ItemPlace.builder()
                                .item(item)
                                .place(place)
                                .build();
                itemPlaceRepository.save(itemPlace);
            }
        }
    }

}
