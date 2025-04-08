package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    public ResponseEntity<?> resisterLostItem(@RequestBody @Valid LostItemRegisterRequest request,
                                              @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        String email = user.getUsername();

        return ResponseEntity.ok(itemService.resisterLostItem(request, email));
    }

    // 분실물 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editLostItem(@RequestBody @Valid LostItemEditRequest request,
                                          @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(itemService.editLostItem(request));
    }

    // 물건 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    // 물건 단일 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItem(@PathVariable Long id) {

        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // 전체 목록 조회 또는 필터 적용하여 분실물 목록 조회
    @GetMapping()
    public ResponseEntity<?> getLostItemsByFilter(@RequestParam(required = false) Long placeId,
                                                    @RequestParam(required = false) Long categoryId) {

        return ResponseEntity.ok(itemService.getLostItemsByFilter(placeId, categoryId));
    }


    // 테스트용
    @PostMapping("/test-bulk-register")
    public ResponseEntity<String> registerTestItems(@AuthenticationPrincipal User user) {
        itemService.registerFixedTestItems(user.getUsername()); // 50개 생성
        return ResponseEntity.ok("테스트용 Item 50개 등록 완료");
    }
}
