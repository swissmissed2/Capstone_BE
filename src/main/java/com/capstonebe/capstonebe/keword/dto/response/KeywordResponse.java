package com.capstonebe.capstonebe.keword.dto.response;

import com.capstonebe.capstonebe.keword.entity.Keyword;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KeywordResponse {

    private String keyword;

    @Builder
    public KeywordResponse(String keyword) {
        this.keyword = keyword;
    }

    public static KeywordResponse fromEntity(Keyword keyword) {

        return KeywordResponse.builder()
                .keyword(keyword.getKeyword())
                .build();
    }
}
