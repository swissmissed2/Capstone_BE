package com.capstonebe.capstonebe.item.dto.request;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class LostItemRegisterRequest {

    @NotBlank
    private String name;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private LocalDateTime time;

    @NotNull
    private String description;

    @NotNull
    private Long categoryId;

    public static Item toEntity(LostItemRegisterRequest lostItemRegisterRequest) {
        return Item.builder()
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
    }
}
