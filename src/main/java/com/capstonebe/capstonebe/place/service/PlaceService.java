package com.capstonebe.capstonebe.place.service;

import com.capstonebe.capstonebe.category.dto.response.CategoryResponse;
import com.capstonebe.capstonebe.global.exception.CustomErrorCode;
import com.capstonebe.capstonebe.global.exception.CustomException;
import com.capstonebe.capstonebe.place.dto.request.PlaceEditRequest;
import com.capstonebe.capstonebe.place.dto.request.PlaceRegisterRequest;
import com.capstonebe.capstonebe.place.dto.response.PlaceResponse;
import com.capstonebe.capstonebe.place.entity.Place;
import com.capstonebe.capstonebe.place.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {

    PlaceRepository placeRepository;

    @Transactional
    public PlaceResponse registerPlace(PlaceRegisterRequest placeRegisterRequest) {

        Place place = Place.builder()
                .name(placeRegisterRequest.getName())
                .latitude(placeRegisterRequest.getLatitude())
                .longitude(placeRegisterRequest.getLongitude())
                .build();

        return PlaceResponse.fromEntity(placeRepository.save(place));
    }

    @Transactional
    public PlaceResponse editPlace(PlaceEditRequest placeEditRequest) {

        Place place = placeRepository.findById(placeEditRequest.getId())
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PLACE));

        place.update(
                placeEditRequest.getName(),
                placeEditRequest.getLatitude(),
                placeEditRequest.getLongitude()
        );

        return PlaceResponse.fromEntity(placeRepository.save(place));
    }

    @Transactional
    public void deletePlace(Long id) {

        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_PLACE));

        placeRepository.delete(place);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAllPlaces() {

        List<Place> categories = placeRepository.findAll();

        return categories.stream()
                .map(PlaceResponse::fromEntity)
                .toList();
    }



    // 테스트용
    @Transactional
    public void registerTestPlaces() {
        List<Place> testPlaces = List.of(
                new Place("충무관", 37.554722, 126.970833),
                new Place("광개토관", 37.5563, 126.9236),
                new Place("학술정보원", 37.4979, 127.0276),
                new Place("광개토관", 35.1151, 129.0426),
                new Place("학술정보원", 33.5113, 126.4930)
        );

        placeRepository.saveAll(testPlaces);
    }
}
