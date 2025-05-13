package com.capstonebe.capstonebe.itemplace.entity;

import com.capstonebe.capstonebe.global.entity.BaseEntity;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.place.entity.Place;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class ItemPlace extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Builder
    public ItemPlace(Item item, Place place) {
        this.item = item;
        this.place = place;
    }

    public ItemPlace() { }
}
