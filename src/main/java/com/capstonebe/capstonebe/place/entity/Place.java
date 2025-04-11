package com.capstonebe.capstonebe.place.entity;

import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPlace> itemPlaces = new ArrayList<>();

    @Builder
    public Place(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place() {}

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void update(String name, Double latitude, Double longitude) {
        updateName(name);
        updateLatitude(latitude);
        updateLongitude(longitude);
    }
}
