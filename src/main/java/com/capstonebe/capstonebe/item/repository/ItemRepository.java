package com.capstonebe.capstonebe.item.repository;

import com.capstonebe.capstonebe.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
