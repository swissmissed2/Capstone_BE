package com.capstonebe.capstonebe.qr.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.qr.dto.request.IssueQrRequest;
import com.capstonebe.capstonebe.qr.entity.Qr;
import com.capstonebe.capstonebe.qr.repository.QrRepository;
import com.capstonebe.capstonebe.returnrecord.service.ReturnRecordService;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final QrRepository qrRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReturnRecordService returnRecordService;

    @Transactional
    public byte[] issueQr(IssueQrRequest request, String email) throws Exception {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        String token = generateSha256Token(user.getId(), request.getLockerId());

        String qrUrl = "http://localhost:8081/api/qr/verify?token=" + token;
        byte[] qrImage = generateQrCode(qrUrl, 250, 250);

        String imageUrl = uploadQrImage(token, qrImage);

        LocalDateTime now = LocalDateTime.now();
        Qr qr = Qr.builder()
                .id(token)
                .user(user)
                .item(item)
                .lockerId(request.getLockerId())
                .used(false)
                .qrImageUrl(imageUrl)
                .expiresAt(now.plusDays(1))
                .build();

        qrRepository.save(qr);

        return qrImage;
    }

    public Boolean verifyQr(String token) {

        Qr qr = qrRepository.findById(token)
                .orElseThrow(() -> new CustomException(CustomErrorCode.QR_NOT_FOUND));

        if (qr.getUsed()) {
            throw new CustomException(CustomErrorCode.QR_ALREADY_USED);
        }

        if (qr.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomErrorCode.QR_EXPIRED);
        }

        qr.setUsed(Boolean.TRUE);

        qrRepository.save(qr);

        returnRecordService.registerReturnRecord(qr.getItem(), qr.getUser(), LocalDateTime.now());

        return Boolean.TRUE;
    }

    private byte[] generateQrCode(String content, int width, int height) throws WriterException, IOException {

        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    private String uploadQrImage(String token, byte[] qrImageData) {

        String fileName = "qr/" + token + ".png";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(qrImageData.length);
        metadata.setContentType("image/png");

        try (InputStream inputStream = new ByteArrayInputStream(qrImageData)) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(CustomErrorCode.IMAGE_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String generateSha256Token(Long userId, Long lockerId) {

        String input = userId + "-" + lockerId + "-" + System.currentTimeMillis();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // HEX 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(CustomErrorCode.QR_ISSUE_FAILED);
        }
    }

}
