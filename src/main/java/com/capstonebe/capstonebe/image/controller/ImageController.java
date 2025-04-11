package com.capstonebe.capstonebe.image.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.dto.request.ImageEditRequest;
import com.capstonebe.capstonebe.image.dto.request.ImageRegisterRequest;
import com.capstonebe.capstonebe.image.service.ImageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImageFile(@RequestParam MultipartFile multipartFile,
                                         @AuthenticationPrincipal User user) {

        if (user == null)
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);

        String imageUrl = imageService.uploadToS3(multipartFile);
        return ResponseEntity.ok(imageUrl);
    }

    // 이미지 객체 등록
    @PostMapping("/register")
    public ResponseEntity<?> registerImage(@RequestBody @Valid ImageRegisterRequest request,
                                           @AuthenticationPrincipal User user) {

        if (user == null)
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);

        return ResponseEntity.ok(imageService.saveImage(request, user.getUsername()));
    }

    @PatchMapping("/edit")
    // todo : 수정 필요
    public ResponseEntity<?> editImage(MultipartFile multipartFile,
                                       @RequestBody @Valid ImageEditRequest request,
                                       @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(imageService.editImage(multipartFile, request));
    }

    @DeleteMapping("{id}")
    // todo : 수정 필요
    public ResponseEntity<?> deleteFile(@PathVariable Long id, @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }

}
