package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadAllResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserWriteReviewRequestDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendForUserService {
    private final PlaceRepository recommendRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public RecommendForUserService(PlaceRepository recommendRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.recommendRepository = recommendRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public RecommendForUserReadResponseDto findById(long placeId, long userId) {
        Optional<Place> place = recommendRepository.findById(placeId);

        if (place.isEmpty()) {
            throw new RuntimeException("no entity place");
        }

        Place exsitPlace = place.get();
        boolean likeOrNot = exsitPlace.getLikes().contains(userId);

        return new RecommendForUserReadResponseDto(exsitPlace, likeOrNot);
    }

    public RecommendForUserReadAllResponseDto findAll(String orderType) {
        List<Place> places = recommendRepository.findAll();
        List<Place> sortedPlaces;

        switch (orderType) {
            case "name" :
                sortedPlaces = places.stream()
                        .sorted(Comparator.comparing(Place::getTitle))
                        .toList();
                break;
            default:
                sortedPlaces = places.stream()
                        .sorted(Comparator.comparing(Place::getDate))
                        .toList();
        }

        return new RecommendForUserReadAllResponseDto(sortedPlaces);
    }

    public void like(long placeId, Long userId) {
        recommendRepository.saveLike(userId, placeId);
    }

    public void writeReview(RecommendForUserWriteReviewRequestDto writeReviewRequestDto) {
        Optional<Place> place = recommendRepository.findById(writeReviewRequestDto.placeId());
        Optional<User> user = userRepository.findById(writeReviewRequestDto.userId());

        if (place.isEmpty() || user.isEmpty()) {
            throw new RuntimeException("no entity place");
        }

        Place exsitPlace = place.get();
        User writer = user.get();

        reviewRepository.save(writeReviewRequestDto.toEntity(writer, exsitPlace));
        recommendRepository.saveReview(writeReviewRequestDto.toEntity(writer, exsitPlace));
    }
}
