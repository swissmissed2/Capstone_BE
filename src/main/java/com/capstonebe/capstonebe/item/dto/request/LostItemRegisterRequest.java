package com.capstonebe.capstonebe.item.dto.request;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LostItemRegisterRequest {

    private String name;

    private Double latitude;

    private Double longitude;

    private String description;

    private Long categoryId;

    public static Item toEntity(LostItemRegisterRequest lostItemRegisterRequest) {
        return Item.builder()
                .userId(1L)
                .type(ItemType.LOST_ITEM)
                .name(lostItemRegisterRequest.getName())
                .latitude(lostItemRegisterRequest.getLatitude())
                .longitude(lostItemRegisterRequest.getLongitude())
                .description(lostItemRegisterRequest.getDescription())
                .itemState(ItemState.NOT_RETURNED)
                .categoryId(lostItemRegisterRequest.getCategoryId())
                .build();
    }
}
