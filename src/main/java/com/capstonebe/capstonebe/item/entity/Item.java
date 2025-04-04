package com.capstonebe.capstonebe.item.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Item {

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

    @Column(nullable = false)
    private LocalDateTime time = LocalDateTime.now();

    private String description;

    @Column(nullable = false)
    private ItemState state;

    @Column(nullable = false)
    private Long categoryId;

    public Item() {}

    @Builder
    public Item(Long userId, ItemType type, String name, Double latitude, Double longitude, String description, ItemState itemState, Long categoryId) {
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.state = itemState;
        this.categoryId = categoryId;
    }

    public void updateName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateLocation(Double latitude, Double longitude) {
        if (latitude != null)
            this.latitude = latitude;
        if (longitude != null)
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
