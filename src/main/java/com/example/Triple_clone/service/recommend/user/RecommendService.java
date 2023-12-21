package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.entity.Review;
import com.example.Triple_clone.entity.User;
import com.example.Triple_clone.repository.PlaceRepository;
import com.example.Triple_clone.repository.ReviewRepository;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        Optional<Place> place = placeRepository.findById(placeId);

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        boolean likeOrNot = exsitPlace.getLikes().contains(userId);

        log.info("read place / Place : {}", exsitPlace);
        return new RecommendReadDto(exsitPlace, likeOrNot);
    }

    @Transactional(readOnly = true)
    public Page<RecommendReadDto> findAll(String orderType, Pageable pageable) {
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

        List<RecommendReadDto> dtos = placesPage.getContent().stream()
                .map(place -> new RecommendReadDto(place, false))
                .toList();

        log.info("readAll place / page : {}", pageable.getPageNumber());
        log.info("readAll place / elementAmount : {}", placesPage.getTotalElements());
        return new PageImpl<>(dtos, pageable, placesPage.getTotalElements());
    }

    @Transactional
    public void like(long placeId, Long userId) {
        Optional<Place> place = placeRepository.findById(placeId);

        if (place.isEmpty()) {
            throw new RuntimeException("no entity place");
        }

        Place exsitPlace = place.get();
        List<Long> likes = exsitPlace.getLikes();
        log.info("before like place / likes : {}", likes.size());

        if (likes.contains(userId)) {
            likes.remove(userId);
        } else {
            likes.add(userId);
        }

        log.info("after like place / likes : {}", likes.size());
    }

    @Transactional
    public void writeReview(RecommendWriteReviewDto writeReviewRequestDto) {
        Optional<Place> place = placeRepository.findById(writeReviewRequestDto.placeId());
        Optional<User> user = userRepository.findById(writeReviewRequestDto.userId());

        Place exsitPlace = place.orElseThrow(() -> new IllegalArgumentException("no place entity"));
        User writer = user.orElseThrow(() -> new IllegalArgumentException("no user entity"));
        log.info("before write review / reviews : {}", exsitPlace.getReviews().size());

        Review review = writeReviewRequestDto.toEntity(writer, exsitPlace);
        reviewRepository.save(review);
        exsitPlace.getReviews().add(review);
        log.info("after write review / reviews : {}", exsitPlace.getReviews().size());
    }
}
