package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.request.AiDescriptionResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public AiDescriptionResponse requestDescriptionFromAI(List<String> imageUrls) {
        String aiUrl = "http://localhost:8080/api/v1/describe-item";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("image_urls", imageUrls);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AiDescriptionResponse> response = restTemplate.postForEntity(
                aiUrl, entity, AiDescriptionResponse.class
        );

        return response.getBody();
    }
}

