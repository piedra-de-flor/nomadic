package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadAllResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserWriteReviewRequestDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendForUserService {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    public RecommendForUserReadResponseDto findById(long placeId, long userId) {
        Optional<Place> place = placeRepository.findById(placeId);

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        boolean likeOrNot = exsitPlace.getLikes().contains(userId);

        return new RecommendForUserReadResponseDto(exsitPlace, likeOrNot);
    }

    public RecommendForUserReadAllResponseDto findAll(String orderType) {
        List<Place> places = placeRepository.findAll();
        List<Place> sortedPlaces;

        switch (orderType) {
            case "name" :
                sortedPlaces = places.stream()
                        .sorted(Comparator.comparing(Place::getTitle))
                        .toList();
                break;
            default:
                sortedPlaces = places.stream()
                        .sorted(Comparator.comparing(Place::getDate).reversed())
                        .toList();
        }

        return new RecommendForUserReadAllResponseDto(sortedPlaces);
    }

    public void like(long placeId, Long userId) {
        placeRepository.saveLike(userId, placeId);
    }

    public void writeReview(RecommendForUserWriteReviewRequestDto writeReviewRequestDto) {
        Optional<Place> place = placeRepository.findById(writeReviewRequestDto.placeId());
        Optional<User> user = userRepository.findById(writeReviewRequestDto.userId());

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        User writer = user.orElseThrow(() -> new IllegalArgumentException("no user entity"));

        reviewRepository.save(writeReviewRequestDto.toEntity(writer, exsitPlace));
    }
}
