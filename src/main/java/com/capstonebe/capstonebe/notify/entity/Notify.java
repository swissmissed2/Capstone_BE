package com.capstonebe.capstonebe.notify.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Notify extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String relatedUrl;

    @Column(nullable = false)
    private NotifyType type;

    @Column(nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    private String itemName;

    @Builder
    public Notify(Long id, String content, String relatedUrl, NotifyType type, Boolean isRead, User receiver, String itemName) {
        this.id = id;
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.type = type;
        this.isRead = isRead;
        this.receiver = receiver;
        this.itemName = itemName;
    }

    public void readNotify() {
        this.isRead = true;
    }
}
