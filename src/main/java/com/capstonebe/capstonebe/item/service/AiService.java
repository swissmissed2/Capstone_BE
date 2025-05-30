package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.request.AiDescriptionRequest;
import com.capstonebe.capstonebe.matching.dto.request.AiMatchingRequest;
import com.capstonebe.capstonebe.item.dto.response.AiDescriptionResponse;
import com.capstonebe.capstonebe.matching.dto.response.AiMatchingResponse;
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

    public AiDescriptionResponse requestDescriptionFromAI(List<String> imageUrls) {

        String aiUrl = "http://localhost:8000/api/v1/describe-item";

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

    public AiMatchingResponse requestMatchingFromAI(AiMatchingRequest request) {

        String aiUrl = "http://localhost:8000/api/v1/match-items";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AiMatchingResponse> response = restTemplate.postForEntity(
                aiUrl, entity, AiMatchingResponse.class
        );

        return response.getBody();
    }
}

