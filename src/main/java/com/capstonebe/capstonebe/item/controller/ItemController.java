package com.capstonebe.capstonebe.item.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.dto.request.FoundItemRegisterRequest;
import com.capstonebe.capstonebe.item.dto.request.ItemVerifyRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemEditRequest;
import com.capstonebe.capstonebe.item.dto.request.LostItemRegisterRequest;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.service.ItemService;
import com.capstonebe.capstonebe.security.JwtUtil;
import com.capstonebe.capstonebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 분실물 등록
    @PostMapping("/lost/register")
    public ResponseEntity<?> resisterLostItem(@RequestBody @Valid LostItemRegisterRequest request,
                                              @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(itemService.resisterLostItem(request, user.getUsername()));
    }

    // 습득물 등록
    @PostMapping("/found/register")
    public ResponseEntity<?> registerFoundItem(@RequestBody @Valid FoundItemRegisterRequest request) {

        return ResponseEntity.ok(itemService.registerFoundItem(request));
    }

    // 분실물 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editLostItem(@RequestBody @Valid LostItemEditRequest request,
                                          @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(itemService.editLostItem(request, user.getUsername()));
    }

    // 물건 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id, @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        itemService.deleteItem(id, user.getUsername());

        return ResponseEntity.ok().build();
    }

    // 물건 단일 목록 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {

        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // 물건 전체 목록 조회 또는 필터 적용하여 분실물 목록 조회 + 검색어 + 날짜
    @GetMapping("/list/{type}")
    public ResponseEntity<?> getItemsByFilter(@PathVariable ItemType type,
                                              @RequestParam(required = false) String place,
                                              @RequestParam(required = false) String category,
                                              @RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              Pageable pageable) {

        System.out.println("place = " + place);
        System.out.println("category = " + category);
        System.out.println("keyword = " + keyword);
        System.out.println("startDate = " + startDate);
        System.out.println("endDate = " + endDate);
        return ResponseEntity.ok(itemService.getItemsByFilter(type, place, category, keyword, startDate, endDate, pageable));
    }

    /**
     * 만료된 물건 목록 조회
     * @param pageable
     * @param token
     * @return
     */
    @GetMapping("/admin/expired")
    public ResponseEntity<?> getExpiredItems(Pageable pageable,
            @CookieValue(value = "jwt", required = false) String token) {

        validateAdmin(token);

        return ResponseEntity.ok(itemService.getExpiredItems(pageable));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<?> verifyItemByIdentifier(@PathVariable Long id,
                                                    @RequestBody @Valid ItemVerifyRequest request,
                                                    @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(itemService.verifyItemByIdentifier(id, request));
    }

    private void validateAdmin(String token) {

        if (token == null || !userService.isAdmin(jwtUtil.extractEmail(token))) {
            throw new CustomException(CustomErrorCode.IS_NOT_ADMIN);
        }
    }

}
