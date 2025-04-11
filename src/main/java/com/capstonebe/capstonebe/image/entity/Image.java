package com.capstonebe.capstonebe.image.entity;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String path;

    private String field;

    @Builder
    public Image(Item item, User user, String path, String field) {
        this.item = item;
        this.user = user;
        this.path = path;
        this.field = field;
    }

    public Image() {}

    public void updatePath(String path) {
        this.path = path;
    }

    public void updateField(String field) {
        this.field = field;
    }
}
