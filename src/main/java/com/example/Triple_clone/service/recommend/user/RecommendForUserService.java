package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendForUserReadResponseDto;
import com.example.Triple_clone.dto.recommend.user.RecommendForUserWriteReviewRequestDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.Review;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendForUserService {
    private final static int PAGE_SIZE = 5;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public RecommendForUserReadResponseDto findById(long placeId, long userId) {
        Optional<Place> place = placeRepository.findById(placeId);

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        boolean likeOrNot = exsitPlace.getLikes().contains(userId);

        return new RecommendForUserReadResponseDto(exsitPlace, likeOrNot);
    }

    public Page<RecommendForUserReadResponseDto> findAll(String orderType, Pageable pageable) {
        Page<Place> placesPage;
        Pageable customPageable;

        switch (orderType) {
            case "name":
                customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by("title").descending());
                placesPage = placeRepository.findAllByOrderByTitleDesc(customPageable);
                break;
            default:
                customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by("date").descending());
                placesPage = placeRepository.findAllByOrderByDateDesc(customPageable);
                break;
        }

        List<RecommendForUserReadResponseDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendForUserReadResponseDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    public void like(long placeId, Long userId) {
        placeRepository.saveLike(userId, placeId);
    }

    public void writeReview(RecommendForUserWriteReviewRequestDto writeReviewRequestDto) {
        Optional<Place> place = placeRepository.findById(writeReviewRequestDto.placeId());
        Optional<User> user = userRepository.findById(writeReviewRequestDto.userId());

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        User writer = user.orElseThrow(() -> new IllegalArgumentException("no user entity"));

        Review review = writeReviewRequestDto.toEntity(writer, exsitPlace);
        reviewRepository.save(review);
        exsitPlace.getReviews().add(review);
    }
}
