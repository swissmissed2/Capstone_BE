package com.capstonebe.capstonebe.qr.controller;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.qr.dto.request.IssueQrRequest;
import com.capstonebe.capstonebe.qr.dto.request.SearchQrRequest;
import com.capstonebe.capstonebe.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    /**
     * qr 발급
     * @param request
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<?> issueQr(@RequestBody IssueQrRequest request,
                                     @AuthenticationPrincipal User user) throws Exception {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        byte[] qrImage = qrService.issueQr(request, user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }

    /**
     * qr 인증
     * @param token
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyQr(@RequestParam String token) {

        return ResponseEntity.ok(qrService.verifyQr(token));
    }

    /**
     * 아이템으로 qr 코드 조회
     * @param request
     * @param user
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getQrByItem(@RequestBody SearchQrRequest request,
                                         @AuthenticationPrincipal User user) {

        if (user == null) {
            throw new CustomException(CustomErrorCode.INVALID_TOKEN);
        }

        return ResponseEntity.ok(qrService.getQrByItem(request, user.getUsername()));
    }

}

