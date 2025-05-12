package com.capstonebe.capstonebe.item.event;


public record ItemRegisteredEvent(Long itemId,
                                  String categoryName,
                                  String typeName) {
}
