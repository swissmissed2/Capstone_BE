package com.capstonebe.capstonebe.qr.dto.response;

import com.capstonebe.capstonebe.qr.entity.Qr;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchQrResponse {

    private String qrImageUrl;
    private Boolean used;
    private LocalDateTime expiresAt;

    @Builder
    public SearchQrResponse(String qrImageUrl, Boolean used, LocalDateTime expiresAt) {
        this.qrImageUrl = qrImageUrl;
        this.used = used;
        this.expiresAt = expiresAt;
    }

    public static SearchQrResponse from(Qr qr) {

        return SearchQrResponse.builder()
                .qrImageUrl(qr.getQrImageUrl())
                .used(qr.getUsed())
                .expiresAt(qr.getExpiresAt())
                .build();
    }
}
