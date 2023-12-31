package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import com.example.Triple_clone.domain.vo.RecommendOrderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {
    private final static int PAGE_SIZE = 5;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public RecommendReadDto findById(long placeId, long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        boolean likeOrNot = place.isLikedBy(userId);

        return new RecommendReadDto(place, likeOrNot);
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.by(RecommendOrderType.valueOf(orderType).property).descending());
        Page<Place> placesPage = placeRepository.findAllByOrderByTitleDesc(customPageable);

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    @Transactional
    public void like(long placeId, Long userId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("no place entity"));

        place.like(userId);
    }

    @Transactional
    public void writeReview(RecommendWriteReviewDto writeReviewRequestDto) {
        Place place = placeRepository.findById(writeReviewRequestDto.placeId())
                .orElseThrow(() -> new NoSuchElementException("no place entity"));
        User user = userRepository.findById(writeReviewRequestDto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        Review review = writeReviewRequestDto.toEntity(user, place);

        reviewRepository.save(review);
        place.addReview(review);
    }
}
