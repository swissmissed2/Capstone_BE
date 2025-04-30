package com.capstonebe.capstonebe.keword.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.keword.dto.request.KeywordRegisterRequest;
import com.capstonebe.capstonebe.keword.dto.response.KeywordResponse;
import com.capstonebe.capstonebe.keword.entity.Keyword;
import com.capstonebe.capstonebe.keword.repository.KeywordRepository;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    public KeywordResponse registerKeyword(KeywordRegisterRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        if (keywordRepository.existsByUserAndKeyword(user, request.getKeyword())) {
            throw new CustomException(CustomErrorCode.DUPLICATED_KEYWORD);
        }


        Keyword keyword = Keyword.builder()
                .keyword(request.getKeyword())
                .user(user)
                .build();

        // 동시성 이슈
        try {
            keywordRepository.save(keyword);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(CustomErrorCode.DUPLICATED_KEYWORD);
        }

        return KeywordResponse.fromEntity(keyword);
    }

    public void deleteKeyword(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        Keyword keyword = keywordRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_KEYWORD));

        keywordRepository.delete(keyword);
    }

    public List<KeywordResponse> getKeywordByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        List<Keyword> keywords = keywordRepository.findByUser(user);

        return keywords.stream()
                .map(KeywordResponse::fromEntity)
                .collect(Collectors.toList());
    }

}
