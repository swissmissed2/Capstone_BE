package com.capstonebe.capstonebe.category.entity;

import com.capstonebe.capstonebe.item.entity.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public Category() {}

    public void updateName(String name) {
        this.name = name;
    }
}
