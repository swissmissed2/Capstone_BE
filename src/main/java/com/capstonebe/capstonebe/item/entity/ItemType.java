package com.capstonebe.capstonebe.item.entity;

public enum ItemType {
    LOST_ITEM("분실물"),
    FOUND_ITEM("습득물");

    private final String name;

    ItemType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}