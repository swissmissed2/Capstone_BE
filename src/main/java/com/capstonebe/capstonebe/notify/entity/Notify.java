package com.capstonebe.capstonebe.notify.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.item.entity.Item;
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

    @Builder
    public Notify(String content, String relatedUrl, NotifyType type, boolean isRead, User receiver) {
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.type = type;
        this.isRead = isRead;
        this.receiver = receiver;
    }
}
