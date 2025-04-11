package com.capstonebe.capstonebe.place.controller;

import com.capstonebe.capstonebe.place.dto.request.PlaceEditRequest;
import com.capstonebe.capstonebe.place.dto.request.PlaceRegisterRequest;
import com.capstonebe.capstonebe.place.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    // 장소 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerPlace(@RequestBody @Valid PlaceRegisterRequest request) {
        return ResponseEntity.ok(placeService.registerPlace(request));
    }

    // 장소 수정
    @PatchMapping("/edit")
    public ResponseEntity<?> editPlace(@RequestBody @Valid PlaceEditRequest request) {
        return ResponseEntity.ok(placeService.editPlace(request));
    }

    // 장소 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.ok().build();
    }
}
