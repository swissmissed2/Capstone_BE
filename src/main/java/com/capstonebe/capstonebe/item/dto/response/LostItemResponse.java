package com.capstonebe.capstonebe.item.dto.response;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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

    @Builder
    public LostItemResponse(Long id, ItemType type, String name, Double latitude, Double longitude, LocalDateTime time, String description, ItemState state, Long categoryId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.description = description;
        this.state = state;
        this.categoryId = categoryId;
    }

    public static LostItemResponse fromEntity(Item item) {
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
                .build();
    }
}
