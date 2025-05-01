package com.capstonebe.capstonebe.item.entity;

import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import com.capstonebe.capstonebe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private ItemType type;

    @Column(nullable = false)
    private String name;

    private Double latitude;

    private Double longitude;

    private LocalDate time;

    private String description;

    @Column(nullable = false)
    private ItemState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPlace> itemPlaces = new ArrayList<>();

    public Item() {}

    @Builder
    public Item(User user, ItemType type, String name, Double latitude, Double longitude, LocalDate time, String description, ItemState itemState, Category category) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.description = description;
        this.state = itemState;
        this.category = category;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void edit(String name, String description, Double latitude, Double longitude, Category category) {
        updateName(name);
        updateDescription(description);
        updateLocation(latitude, longitude);
        updateCategory(category);
    }

    // DB에 insert가 끝난 직후 실행
    @PostPersist
    private void setTimeAfterPersist() {

        if (this.type == ItemType.FOUND_ITEM && this.time == null)
            this.time = getCreatedAt().toLocalDate();
    }

}
