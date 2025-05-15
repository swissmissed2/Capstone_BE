package com.capstonebe.capstonebe.qr.controller;

import com.capstonebe.capstonebe.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    /**
     * qr 발급
     * @param lockerId
     * @param userId
     * @return
     * @throws Exception
     */
    @PostMapping("/{lockerId}")
    public ResponseEntity<?> issueQr(@PathVariable Long lockerId,
                                          @RequestParam Long userId) throws Exception {

        byte[] qrImage = qrService.issueQr(lockerId, userId);

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

        return ResponseEntity.ok(qrService.veryfyQr(token));
    }
}

