package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.category.repository.CategoryRepository;
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
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPlaceRepository itemPlaceRepository;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public LostItemResponse resisterLostItem(LostItemRegisterRequest request, String email) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_CATEGORY));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Item item = Item.builder()
                .user(user)
                .type(ItemType.LOST_ITEM)
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .time(request.getTime())
                .description(request.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .category(category)
                .build();

        itemRepository.save(item);

        List<Place> places = placeRepository.findAllById(request.getPlaceIds());
        saveItemPlaces(item, places);

        return LostItemResponse.fromEntity(item, places);
    }

    @Transactional
    public LostItemResponse editLostItem(LostItemEditRequest request) {

        Item item = itemRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

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

        return LostItemResponse.fromEntity(item, places);
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

    @Transactional(readOnly = true)
    public List<LostItemResponse> getLostItemsByFilter(ItemType type, String placeName, String categoryName, String keyword, LocalDate startDate, LocalDate endDate) {

        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        List<Item> items = itemRepository.findItemsByFilter(placeName, categoryName, type, keyword, startDate, endDate);

        return items.stream()
                .map(item -> {
                    List<Place> places = item.getItemPlaces().stream()
                            .map(ItemPlace::getPlace)
                            .toList();
                    return LostItemResponse.fromEntity(item, places);
                })
                .toList();
    }


    @Transactional(readOnly = true)
    public List<LostItemResponse> getLostItemsByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        List<Item> items = itemRepository.findByUser(user);

        return items.stream()
                .map(item -> {
                    List<Place> places = item.getItemPlaces().stream()
                            .map(ItemPlace::getPlace)
                            .toList();
                    return LostItemResponse.fromEntity(item, places);
                })
                .toList();
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



    // 테스트용
    @Transactional
    public void registerFixedTestItems(String email) {
        List<LostItemRegisterRequest> testItems = List.of(
                createRequest("검정색 지갑", 37.5665, 126.9780, "지하철역 근처에서 분실한 검정 지갑", 1L, List.of(1L, 2L)),
                createRequest("아이폰 13", 37.5700, 126.9825, "카페에서 두고 온 아이폰", 2L, List.of(2L)),
                createRequest("회색 후드티", 37.5641, 126.9750, "버스에서 분실한 옷", 3L, List.of(3L)),
                createRequest("서류 가방", 37.5610, 126.9832, "사무실 근처에서 잃어버림", 4L, List.of(1L)),
                createRequest("무선 이어폰", 37.5599, 126.9700, "공원 벤치에서 분실한 이어폰", 5L, List.of(1L, 3L))
        );

        for (LostItemRegisterRequest request : testItems) {
            resisterLostItem(request, email); // 기존 등록 메서드 재사용!
        }
    }

    private LostItemRegisterRequest createRequest(String name, Double lat, Double lon, String desc, Long categoryId, List<Long> placeIds) {
        LostItemRegisterRequest request = new LostItemRegisterRequest();
        request.setName(name);
        request.setLatitude(lat);
        request.setLongitude(lon);
        request.setTime(LocalDate.now().minusDays(new Random().nextInt(7))); // 최근 일주일 랜덤
        request.setDescription(desc);
        request.setCategoryId(categoryId);
        request.setPlaceIds(placeIds);
        return request;
    }


}
