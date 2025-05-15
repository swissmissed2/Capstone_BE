package com.capstonebe.capstonebe.qr.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Qr extends BaseEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long lockerId;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private String qrImageUrl;

    @Builder
    public Qr(String id, Long userId, Long lockerId, LocalDateTime expiresAt, boolean used, String qrImageUrl) {
        this.id = id;
        this.userId = userId;
        this.lockerId = lockerId;
        this.expiresAt = expiresAt;
        this.used = used;
        this.qrImageUrl = qrImageUrl;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
