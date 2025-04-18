package com.capstonebe.capstonebe.image.dto.response;

import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.image.entity.Image;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageResponse {

    //private Long id;
    //private User userId;

    @JsonProperty("image_url")
    private List<String> imageUrl;

    @JsonProperty("item_id")
    private Long itemId;

    private String category;

    private String type;

    @Builder
    public ImageResponse(List<String> imageUrl, Long itemId, String category, String type) {
        this.imageUrl = imageUrl;
        this.itemId = itemId;
        this.category = category;
        this.type = type;
    }

    public static ImageResponse from(List<Image> images) {

        List<String> imageUrls = images.stream()
                .map(Image::getPath)
                .toList();

        Image image = images.get(0);

        return ImageResponse.builder()
                .imageUrl(imageUrls)
                .itemId(image.getItem().getId())
                .category(image.getItem().getCategory().getName())
                .type(image.getItem().getType().getName())
                .build();
    }
}
