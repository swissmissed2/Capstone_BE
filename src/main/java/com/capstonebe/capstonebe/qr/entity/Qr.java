package com.capstonebe.capstonebe.qr.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    private Item item;

    @Column(nullable = false)
    private Long lockerId;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean used;

    @Column(nullable = false)
    private String qrImageUrl;

    @Builder
    public Qr(String id, User user, Item item, Long lockerId, LocalDateTime expiresAt, Boolean used, String qrImageUrl) {
        this.id = id;
        this.user = user;
        this.item = item;
        this.lockerId = lockerId;
        this.expiresAt = expiresAt;
        this.used = used;
        this.qrImageUrl = qrImageUrl;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
