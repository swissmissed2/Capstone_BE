package com.capstonebe.capstonebe.notify.dto;

import com.capstonebe.capstonebe.notify.entity.Notify;
import com.capstonebe.capstonebe.notify.entity.NotifyType;
import lombok.Builder;
import lombok.Getter;


@Getter
public class NotifyResponse {

    String id;

    String name;

    String content;

    String type;

    String url;

    String createdAt;

    @Builder
    public NotifyResponse(String id, String name, String content, String type, String url, String createdAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.type = type;
        this.url = url;
        this.createdAt = createdAt;
    }

    public static NotifyResponse fromEntity(Notify notify) {
        return NotifyResponse.builder()
                .content(notify.getContent())
                .id(notify.getId().toString())
                .name(notify.getReceiver().getName())
                .type(notify.getType() == NotifyType.MATCHING ? NotifyType.MATCHING.toString() : NotifyType.KEYWORD.toString())
                .url(notify.getRelatedUrl())
                .createdAt(notify.getCreateAt().toString())
                .build();

    }

}
