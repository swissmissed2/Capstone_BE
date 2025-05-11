package com.capstonebe.capstonebe.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.dto.request.ImageRegisterRequest;
import com.capstonebe.capstonebe.image.dto.response.ImageResponse;
import com.capstonebe.capstonebe.image.entity.Image;
import com.capstonebe.capstonebe.image.repository.ImageRepository;
import com.capstonebe.capstonebe.item.dto.request.AiDescriptionResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.item.service.AiService;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final AiService aiService;

    /**
     * 이미지 업로드하고 ai서버로 이미지 경로 전송하여 이미지에 대한 설명 받기
     * @param multipartFiles
     * @return
     */
    public AiDescriptionResponse uploadImages(List<MultipartFile> multipartFiles) {

            List<String> imageUuls =  multipartFiles.stream()
                    .map(this::uploadToS3)
                    .toList();

            return aiService.requestDescriptionFromAI(imageUuls);
    }

    public String uploadToS3(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException(CustomErrorCode.INVALID_IMAGE_FORMAT);
        }

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(CustomErrorCode.IMAGE_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Transactional
    public ImageResponse registerImage(ImageRegisterRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        List<Image> images = request.getPaths().stream()
                .map(path -> Image.builder()
                        .item(item)
                        .user(user)
                        .path(path)
                        .build())
                .toList();

        imageRepository.saveAll(images);

        return ImageResponse.from(images);
    }

    @Transactional
    public void deleteImage(Long id) {

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.IMAGE_NOT_FOUND));

        deleteInS3(image.getPath());
        imageRepository.delete(image);

        System.out.println("삭제 완료: " + image);
    }

    private void deleteInS3(String path) {

        String fileName = extractFileNameFromUrl(path);

        // S3에 파일이 존재하는지 확인
        boolean isExist = amazonS3.doesObjectExist(bucket, fileName);

        if (!isExist) {
            System.out.println("파일이 존재하지 않습니다: " + fileName);
            throw new CustomException(CustomErrorCode.IMAGE_DELETE_FAILED);
        }

        // S3에서 파일 삭제
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new CustomException(CustomErrorCode.IMAGE_CONVERT_FAILED);
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    @Transactional(readOnly = true)
    public String getImagePathByItemId(Long itemId) {
        return imageRepository.findByItemId(itemId)
                .map(Image::getPath)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public String getFirstImagePathByItem(Item item) {
        return imageRepository.findFirstByItemOrderByIdAsc(item)
                .map(Image::getPath)
                .orElse(null);
    }
}
