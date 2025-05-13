package com.capstonebe.capstonebe.matching.dto.response;

import com.capstonebe.capstonebe.matching.entity.Matching;
import com.capstonebe.capstonebe.matching.entity.MatchingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MatchingResponse {
    private final Long lostItemId;
    private final Long matchedItemId;
    private final MatchingState state;

    public static MatchingResponse from(Matching matching) {
        return new MatchingResponse(
                matching.getLostItem().getId(),
                matching.getFoundItem().getId(),
                matching.getState()
        );
    }
}
