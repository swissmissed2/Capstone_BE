package com.capstonebe.capstonebe.place.dto.response;


import com.capstonebe.capstonebe.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceRegisterResponse {

    private String name;

    private Double latitude;

    private Double longitude;

    @Builder
    public PlaceRegisterResponse(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static PlaceRegisterResponse fromEntity(Place place) {
        return PlaceRegisterResponse.builder()
                .name(place.getName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }
}
