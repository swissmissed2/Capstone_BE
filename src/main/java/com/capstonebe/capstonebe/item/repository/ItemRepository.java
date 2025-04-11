package com.capstonebe.capstonebe.item.repository;

import com.capstonebe.capstonebe.category.entity.Category;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByType(ItemType type);

    List<Item> findByCategoryAndType(Category category, ItemType type);

    @Query("SELECT ip.item FROM ItemPlace ip WHERE ip.place.id = :placeId AND ip.item.type = :itemType")
    List<Item> findItemsByPlaceIdAndType(@Param("placeId") Long placeId,
                                         @Param("itemType") ItemType itemType);

    @Query("SELECT ip.item FROM ItemPlace ip WHERE ip.place.id = :placeId AND ip.item.category.id = :categoryId AND ip.item.type = :itemType")
    List<Item> findItemsByPlaceCategoryAndType(@Param("placeId") Long placeId,
                                               @Param("categoryId") Long categoryId,
                                               @Param("itemType") ItemType itemType);
}
