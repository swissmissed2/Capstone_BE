package com.capstonebe.capstonebe.matching.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.image.service.ImageService;
import com.capstonebe.capstonebe.item.dto.response.ItemListResponse;
import com.capstonebe.capstonebe.item.entity.Item;
import com.capstonebe.capstonebe.item.entity.ItemType;
import com.capstonebe.capstonebe.item.repository.ItemRepository;
import com.capstonebe.capstonebe.matching.dto.request.CreateMatchingRequest;
import com.capstonebe.capstonebe.matching.dto.response.AiMatchingResponse;
import com.capstonebe.capstonebe.matching.dto.response.MatchingResponse;
import com.capstonebe.capstonebe.matching.entity.Matching;
import com.capstonebe.capstonebe.matching.entity.MatchingState;
import com.capstonebe.capstonebe.matching.repository.MatchingRepository;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import com.capstonebe.capstonebe.notify.service.NotifyService;
import com.capstonebe.capstonebe.user.entity.User;
import com.capstonebe.capstonebe.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final NotifyService notifyService;

    @Transactional
    public List<MatchingResponse> createMatching(CreateMatchingRequest createRequest) {
        Item item = itemRepository.findById(createRequest.getItemId()).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_ITEM));

        List<Item> matchedItems = itemRepository.findAllById(createRequest.getMatchedItemIds());

        List<Matching> matchings = new ArrayList<>();

        if(item.getType() == ItemType.LOST_ITEM) {
            for (Item matchedItem : matchedItems) {
                Matching matching = Matching.builder()
                            .lostItem(item)
                            .foundItem(matchedItem)
                            .state(MatchingState.UNPROCESSED)
                            .build();

                matchings.add(matching);
            }
        } else if(item.getType() == ItemType.FOUND_ITEM){
            for (Item matchedItem : matchedItems) {
                Matching matching = Matching.builder()
                        .lostItem(matchedItem)
                        .foundItem(item)
                        .state(MatchingState.UNPROCESSED)
                        .build();

                matchings.add(matching);
            }
        }
        matchingRepository.saveAll(matchings);

        return matchings.stream()
                .map(MatchingResponse::from)
                .collect(Collectors.toList());
    }

    public Page<ItemListResponse> getMatchedItemsByUser(String email, Long id, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        Page<Item> items = itemRepository.findLostItemsMatchedToFoundItem(user.getId(), id, pageable);
        return items.map(item -> {
            String url = imageService.getImagePathByItemId(item.getId());
            return ItemListResponse.from(item, url);
        });
    }

    public void sendMatchingNotify(AiMatchingResponse response, List<String> emails) {

        String content = "등록하신 분실물과 유사한 습득물이 있습니다.";
        String url = "/api/items/" + response.getItemId();

        emails.forEach(email ->
                notifyService.send(email, NotifyType.MATCHING, content, url)
        );
    }

}
