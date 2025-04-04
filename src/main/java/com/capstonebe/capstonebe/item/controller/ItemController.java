package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.response.LostItemResponse;
import com.capstonebe.capstonebe.item.service.ItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 분실물 등록
    @PostMapping("/register")
    public String resisterLostItem(@RequestBody LostItemRegisterRequest request) {
        itemService.resisterLostItem(request);
        return "Resister Item";
    }

    // 분실물 수정
    @PatchMapping("/edit")
    public String editLostItem(@RequestBody LostItemEditRequest request) {
        itemService.editLostItem(request);
        return "Edit successful";
    }

    // 물건 삭제
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "delete item";
    }

    // 물건 단일 목록 조회
    @GetMapping("/{id}")
    public LostItemResponse getItem(@PathVariable Long id) {
        return itemService.getItem(id);
    }

}
