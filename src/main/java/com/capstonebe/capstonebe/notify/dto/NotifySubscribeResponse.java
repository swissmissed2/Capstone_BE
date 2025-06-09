package com.capstonebe.capstonebe.notify.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class NotifySubscribeResponse {

    SseEmitter emitter;

    Boolean hasNotifications;

    @Builder
    public NotifySubscribeResponse(SseEmitter emitter, Boolean hasNotifications) {
        this.emitter = emitter;
        this.hasNotifications = hasNotifications;
    }

    public static NotifySubscribeResponse from(SseEmitter emitter, Boolean hasNotifications) {
        return NotifySubscribeResponse.builder()
                .emitter(emitter)
                .hasNotifications(hasNotifications)
                .build();
    }
}
