package com.capstonebe.capstonebe.item.dto.response;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ItemListResponse {

    private Long id;
    private String name;
    private LocalDate time;
    private ItemType type;
    private ItemState state;
    private Long categoryId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;


    @Builder
    public ItemListResponse(Long id, String name, LocalDate time, ItemType type, ItemState state, Long categoryId, String imageUrl, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.type = type;
        this.state = state;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static ItemListResponse from(Item item, String imageUrl) {

        return ItemListResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .time(item.getTime())
                .type(item.getType())
                .state(item.getState())
                .categoryId(item.getCategory().getId())
                .imageUrl(imageUrl)
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }
}
