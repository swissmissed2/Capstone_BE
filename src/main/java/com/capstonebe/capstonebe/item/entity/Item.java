package com.capstonebe.capstonebe.item.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private ItemType type;

    @Column(nullable = false)
    private String name;

    private Double latitude;

    private Double longitude;

    private LocalDateTime time;

    private String description;

    @Column(nullable = false)
    private ItemState state;

    @Column(nullable = false)
    private Long categoryId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPlace> itemPlaces = new ArrayList<>();

    public Item() {}

    @Builder
    public Item(Long userId, ItemType type, String name, Double latitude, Double longitude, LocalDateTime time, String description, ItemState itemState, Long categoryId) {
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
        this.description = description;
        this.state = itemState;
        this.categoryId = categoryId;
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

    public void updateCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void edit(String name, String description, Double latitude, Double longitude, Long categoryId) {
        updateName(name);
        updateDescription(description);
        updateLocation(latitude, longitude);
        updateCategory(categoryId);
    }
}
