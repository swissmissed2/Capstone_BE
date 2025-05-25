package com.capstonebe.capstonebe.place.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.place.dto.request.PlaceEditRequest;
import com.capstonebe.capstonebe.place.dto.request.PlaceRegisterRequest;
import com.capstonebe.capstonebe.place.service.PlaceService;
import com.capstonebe.capstonebe.security.JwtUtil;
import com.capstonebe.capstonebe.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
@AllArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // todo : 관리자 인증
    // 장소 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerPlace(@RequestBody @Valid PlaceRegisterRequest request,
                                           @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        return ResponseEntity.ok(placeService.registerPlace(request));
    }

    // 장소 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editPlace(@RequestBody @Valid PlaceEditRequest request,
                                       @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        return ResponseEntity.ok(placeService.editPlace(request));
    }

    // 장소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id,
                                         @CookieValue(value = "jwt", required = false) String token) {

        //validateAdmin(token);

        placeService.deletePlace(id);

        return ResponseEntity.ok().build();
    }

    // 장소 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllPlaces() {

        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    private void validateAdmin(String token) {

        if (token == null || !userService.isAdmin(jwtUtil.extractEmail(token))) {
            throw new CustomException(CustomErrorCode.IS_NOT_ADMIN);
        }
    }

}
