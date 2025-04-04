package com.capstonebe.capstonebe.item.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LostItemEditRequest {
    private Long id;              // 어떤 아이템을 수정할지
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long categoryId;
}
