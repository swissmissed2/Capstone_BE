package com.capstonebe.capstonebe.returnrecord.dto.response;

import com.capstonebe.capstonebe.returnrecord.entity.ReturnRecord;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReturnRecordResponse {

    private Long id;

    private Long itemId;

    private Long userId;

    private LocalDateTime returnDate;

    @Builder
    public ReturnRecordResponse(Long id, Long itemId, Long userId, LocalDateTime returnDate) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.returnDate = returnDate;
    }

    public static ReturnRecordResponse from(ReturnRecord returnRecord) {
        return ReturnRecordResponse.builder()
                .id(returnRecord.getId())
                .itemId(returnRecord.getItem().getId())
                .userId(returnRecord.getUser().getId())
                .returnDate(returnRecord.getReturnDate())
                .build();
    }
}
