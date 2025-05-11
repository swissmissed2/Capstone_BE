package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.image.entity.Image;
import com.capstonebe.capstonebe.image.repository.ImageRepository;
import com.capstonebe.capstonebe.item.dto.request.AiDescriptionRequest;
import com.capstonebe.capstonebe.item.dto.request.AiMatchingRequest;
import com.capstonebe.capstonebe.item.dto.response.AiDescriptionResponse;
import com.capstonebe.capstonebe.item.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class AiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ImageRepository imageRepository;

    public AiDescriptionResponse requestDescriptionFromAI(List<String> imageUrls) {

        String aiUrl = "http://localhost:8080/api/v1/describe-item";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AiDescriptionRequest body = AiDescriptionRequest.builder()
                .imageUrls(imageUrls)
                .build();

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AiDescriptionResponse> response = restTemplate.postForEntity(
                aiUrl, entity, AiDescriptionResponse.class
        );

        return response.getBody();
    }

    public AiMatchingResponse requestMatchingFromAI(Item item) {

        String aiUrl = "http://localhost:8080/api/v1/match-items";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<String> imageUrls = imageRepository.findAllByItem(item)
                .stream()
                .map(Image::getPath)
                .toList();

        AiMatchingRequest body = AiMatchingRequest.builder()
                .itemId(item.getId())
                .imageUrls(imageUrls)
                .category(item.getCategory().getName())
                .type(item.getType().getName())
                .build();

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AiMatchingResponse> response = restTemplate.postForEntity(
                aiUrl, entity, AiMatchingResponse.class
        );

        return response.getBody();
    }
}

