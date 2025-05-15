package com.capstonebe.capstonebe.qr.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class IssueQrRequest {

    @NotNull
    private Long lockerId;

    @NotNull
    private Long itemId;
}
