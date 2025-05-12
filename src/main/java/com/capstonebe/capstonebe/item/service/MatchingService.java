package com.capstonebe.capstonebe.item.service;

import com.capstonebe.capstonebe.item.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.notify.service.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchingService {

    private final NotifyService notifyService;

    public void sendMatchingNotify(AiMatchingResponse response, List<String> emails) {

        String content = "등록하신 분실물과 유사한 습득물이 있습니다.";
        String url = "/api/items/" + response.getItemId();

        emails.forEach(email ->
                notifyService.send(email, NotifyType.MATCHING, content, url)
        );
    }

}
