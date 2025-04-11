package com.capstonebe.capstonebe.place.service;

import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.place.dto.request.PlaceEditRequest;
import com.capstonebe.capstonebe.place.dto.request.PlaceRegisterRequest;
import com.capstonebe.capstonebe.place.dto.response.PlaceRegisterResponse;
import com.capstonebe.capstonebe.place.entity.Place;
import com.capstonebe.capstonebe.place.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PlaceService {

    PlaceRepository placeRepository;

    @Transactional
    public PlaceRegisterResponse registerPlace(PlaceRegisterRequest placeRegisterRequest) {

        Place place = Place.builder()
                .name(placeRegisterRequest.getName())
                .latitude(placeRegisterRequest.getLatitude())
                .longitude(placeRegisterRequest.getLongitude())
                .build();

        return PlaceRegisterResponse.fromEntity(placeRepository.save(place));
    }

    @Transactional
    public PlaceRegisterResponse editPlace(PlaceEditRequest placeEditRequest) {

        Place place = placeRepository.findById(placeEditRequest.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PLACE));

        place.update(
                placeEditRequest.getName(),
                placeEditRequest.getLatitude(),
                placeEditRequest.getLongitude()
        );

        return PlaceRegisterResponse.fromEntity(placeRepository.save(place));
    }

    @Transactional
    public void deletePlace(Long id) {

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PLACE));

        placeRepository.delete(place);
    }
}
