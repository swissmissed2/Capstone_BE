package com.capstonebe.capstonebe.item.dto.response;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ItemDetailResponse {

    private Long id;
    private ItemType type;
    private String name;
    private Double latitude;
    private Double longitude;
    private LocalDate time;
    private String description;
    private ItemState state;
    private Long categoryId;
    private List<PlaceInfo> places;
    private String imageUrl;


    @Builder
    public ItemDetailResponse(Long id, ItemType type, String name, Double latitude, Double longitude, LocalDate time, String description, ItemState state, Long categoryId, List<PlaceInfo> places, String imageUrl) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.description = description;
        this.state = state;
        this.categoryId = categoryId;
        this.places = places;
        this.imageUrl = imageUrl;
    }

    public static ItemDetailResponse from(Item item, List<Place> places, String imageUrl) {
        return ItemDetailResponse.builder()
                .id(item.getId())
                .type(item.getType())
                .name(item.getName())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .time(item.getTime())
                .description(item.getDescription())
                .state(item.getState())
                .categoryId(item.getCategory().getId())
                .places(places.stream()
                        .map(place -> new PlaceInfo(
                                place.getId(),
                                place.getName(),
                                place.getLatitude(),
                                place.getLongitude()))
                        .toList())
                .imageUrl(imageUrl)
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class PlaceInfo {
        private Long id;
        private String name;
        private Double latitude;
        private Double longitude;
    }
}
