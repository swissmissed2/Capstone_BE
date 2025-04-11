package com.capstonebe.capstonebe.itemplace.repository;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.itemplace.entity.ItemPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPlaceRepository extends JpaRepository<ItemPlace, Long> {
    void deleteByItem(Item item);
}
