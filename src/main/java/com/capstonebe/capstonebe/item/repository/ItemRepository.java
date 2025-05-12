package com.capstonebe.capstonebe.item.repository;

import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemState;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByType(ItemType type);

//    List<Item> findByCategoryAndType(Category category, ItemType type);
//
//    @Query("SELECT ip.item FROM ItemPlace ip WHERE ip.place.id = :placeId AND ip.item.type = :itemType")
//    List<Item> findItemsByPlaceIdAndType(@Param("placeId") Long placeId,
//                                         @Param("itemType") ItemType itemType);
//
//    @Query("SELECT ip.item FROM ItemPlace ip WHERE ip.place.id = :placeId AND ip.item.category.id = :categoryId AND ip.item.type = :itemType")
//    List<Item> findItemsByPlaceCategoryAndType(@Param("placeId") Long placeId,
//                                               @Param("categoryId") Long categoryId,
//                                               @Param("itemType") ItemType itemType);

    // 최신순
    @Query("""
    SELECT i FROM Item i
    JOIN i.itemPlaces ip
    JOIN ip.place p
    JOIN i.category c
    WHERE i.type = :itemType
    AND (:placeName IS NULL OR p.name = :placeName)
    AND (:categoryName IS NULL OR c.name = :categoryName)
    AND (
        :keyword IS NULL
        OR LOWER(COALESCE(i.name, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(COALESCE(i.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND (:startDate IS NULL OR i.time >= :startDate)
    AND (:endDate IS NULL OR i.time <= :endDate)
    ORDER BY i.time DESC
""")
    Page<Item> findItemsByFilter(
            @Param("placeName") String placeName,
            @Param("categoryName") String categoryName,
            @Param("itemType") ItemType itemType,
            @Param("keyword") String keyword,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    Page<Item> findByUser(User user, Pageable pageable);

    List<Item> findByCreatedAtBeforeAndState(LocalDateTime createdAt, ItemState state);


}