package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.AuthErrorCode;
import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.dto.review.ReviewResponseDto;
import com.example.Triple_clone.dto.review.ReviewUpdateDto;
import com.example.Triple_clone.dto.review.RootReviewResponseDto;
import com.example.Triple_clone.domain.member.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import com.example.Triple_clone.service.support.FileManager;
import com.example.Triple_clone.web.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewFacadeService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final RecommendService recommendService;
    private final FileManager fileManager;

    @Transactional
    public void writeReview(RecommendWriteReviewDto writeReviewRequestDto, String email) {
        Recommendation recommendation = recommendService.findById(writeReviewRequestDto.placeId());
        Member member = userService.findByEmail(email);

        Review parent = null;
        if (writeReviewRequestDto.parentId() != null) {
            parent = reviewService.findById(writeReviewRequestDto.parentId());

            if (parent.getParent() != null) {
                log.warn("⚠️ 리뷰 작성 실패 - 대댓글 depth 제한: parentReview = {}", writeReviewRequestDto.parentId());
                throw new IllegalArgumentException("대댓글은 depth가 2이상 허용되지 않습니다.");
            }
        }

        Review review = writeReviewRequestDto.toEntity(member, recommendation, parent);

        reviewService.save(review);
        recommendation.addReview(review);
    }

    public Page<RootReviewResponseDto> getRootReviews(Long recommendationId, Pageable pageable) {
        return reviewService.getRootReviews(recommendationId, pageable);
    }

    public Page<ReviewResponseDto> getReplies(Long parentId, Pageable pageable) {
        return reviewService.getReplies(parentId, pageable);
    }

    public void deleteReview(Long reviewId, String email) {
        Member member = userService.findByEmail(email);
        Review review = reviewService.findById(reviewId);

        if (review.getMember().getId() == member.getId()) {
            reviewService.delete(review);
            return;
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
    }

    public ReviewResponseDto updateReview(ReviewUpdateDto updateDto, String email) {
        Review review = reviewService.findById(updateDto.reviewId());
        Member member = userService.findByEmail(email);

        if (review.getMember().getId() == member.getId()) {
            reviewService.update(review, updateDto.content());
            return new ReviewResponseDto(review);
        }

        throw new RestApiException(AuthErrorCode.AUTH_ERROR_CODE);
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
