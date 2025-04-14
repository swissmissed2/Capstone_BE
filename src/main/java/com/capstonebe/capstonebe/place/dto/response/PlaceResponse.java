package com.capstonebe.capstonebe.place.dto.response;


import com.capstonebe.capstonebe.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlaceResponse {

    private String name;

    private Double latitude;

    private Double longitude;

    @Builder
    public PlaceResponse(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static PlaceResponse fromEntity(Place place) {
        return PlaceResponse.builder()
                .name(place.getName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }
}
