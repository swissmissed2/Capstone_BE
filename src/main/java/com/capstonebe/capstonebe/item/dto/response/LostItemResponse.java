package com.capstonebe.capstonebe.item.dto.response;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import com.capstonebe.capstonebe.place.entity.Place;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LostItemResponse {

    private Long id;
    private ItemType type;
    private String name;
    private Double latitude;
    private Double longitude;
    private LocalDateTime time;
    private String description;
    private ItemState state;
    private Long categoryId;
    private List<PlaceInfo> places;


    @Builder
    public LostItemResponse(Long id, ItemType type, String name, Double latitude, Double longitude, LocalDateTime time, String description, ItemState state, Long categoryId, List<PlaceInfo> places) {
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
    }

    public static LostItemResponse fromEntity(Item item, List<Place> places) {
        return LostItemResponse.builder()
                .id(item.getId())
                .type(item.getType())
                .name(item.getName())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .time(item.getTime())
                .description(item.getDescription())
                .state(item.getState())
                .categoryId(item.getCategoryId())
                .places(places.stream()
                        .map(place -> new PlaceInfo(
                                place.getId(),
                                place.getName(),
                                place.getLatitude(),
                                place.getLongitude()))
                        .toList())
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
