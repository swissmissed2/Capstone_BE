package com.capstonebe.capstonebe.image.dto.response;

import com.capstonebe.capstonebe.image.entity.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ImageResponse {

    private Long id;

    private Long itemId;

    private Long userId;

    private String path;

    @Builder
    public ImageResponse(Long id, Long itemId, Long userId, String path) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.path = path;
    }

    public static ImageResponse fromEntity(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .itemId(image.getItem().getId())
                .userId(image.getUser().getId())
                .path(image.getPath())
                .build();
    }
}
