package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.dto.review.ReviewResponseDto;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReviewFacadeService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final RecommendService recommendService;
    private final FileManager fileManager;

    @Transactional
    public void writeReview(RecommendWriteReviewDto writeReviewRequestDto) {
        Recommendation recommendation = recommendService.findById(writeReviewRequestDto.placeId());
        Member member = userService.findById(writeReviewRequestDto.userId());
        Review review = writeReviewRequestDto.toEntity(member, recommendation);

        reviewService.save(review);
        recommendation.addReview(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long memberId) {
        Member member = userService.findById(memberId);
        Review review = reviewService.findById(reviewId);

        if (review.getMember().getId() == member.getId()) {
            reviewService.delete(reviewId);
        }
    }

    @Transactional
    public Long setImageOfReview(Long reviewId, MultipartFile image) {
        Review review = reviewService.findById(reviewId);
        Image reviewImage = fileManager.uploadImage(image);
        review.setImage(reviewImage);
        return reviewId;
    }

    public byte[] loadImageAsResource(Long reviewId) {
        Review review = reviewService.findById(reviewId);
        String path = review.getImage().getStoredFileName();
        return fileManager.loadImageAsResource(path);
    }
}
