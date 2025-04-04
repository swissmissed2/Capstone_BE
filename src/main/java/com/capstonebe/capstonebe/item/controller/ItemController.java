package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.response.LostItemResponse;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> resisterLostItem(@RequestBody @Valid LostItemRegisterRequest request) {

        return ResponseEntity.ok(itemService.resisterLostItem(request));
    }

    // 분실물 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editLostItem(@RequestBody @Valid LostItemEditRequest request) {

        return ResponseEntity.ok(itemService.editLostItem(request));
    }

    // 물건 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    // 물건 단일 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {

        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // 물건 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllItemsByType(@RequestParam ItemType type) {

        return ResponseEntity.ok(itemService.getItemsByType(type));
    }




    // 테스트용
    @PostMapping("/generate-test-data")
    public String generateTestItems() {

        itemService.generateTestItems(100);  // 원하는 개수
        return "100 test items generated";
    }
}
