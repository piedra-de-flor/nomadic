package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import com.example.Triple_clone.service.support.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReviewFacadeServiceTest {

    private UserService userService;
    private ReviewService reviewService;
    private RecommendService recommendService;
    private FileManager fileManager;
    private ReviewFacadeService facadeService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        reviewService = mock(ReviewService.class);
        recommendService = mock(RecommendService.class);
        fileManager = mock(FileManager.class);
        facadeService = new ReviewFacadeService(userService, reviewService, recommendService, fileManager);
    }

    @Test
    @DisplayName("리뷰 작성 성공")
    void writeReviewSuccess() {
        RecommendWriteReviewDto dto = mock(RecommendWriteReviewDto.class);
        Member member = mock(Member.class);
        Recommendation recommendation = mock(Recommendation.class);
        Review review = mock(Review.class);

        when(dto.userId()).thenReturn(1L);
        when(dto.placeId()).thenReturn(100L);
        when(userService.findById(1L)).thenReturn(member);
        when(recommendService.findById(100L)).thenReturn(recommendation);
        when(dto.toEntity(member, recommendation, null)).thenReturn(review);

        facadeService.writeReview(dto);

        verify(reviewService).save(review);
        verify(recommendation).addReview(review);
    }

    @Test
    @DisplayName("리뷰 이미지 저장 성공")
    void setImageOfReview() {
        Long reviewId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        Review review = mock(Review.class);
        Image image = new Image("original.jpg", "stored.jpg");

        when(reviewService.findById(reviewId)).thenReturn(review);
        when(fileManager.uploadImage(file)).thenReturn(image);

        Long result = facadeService.setImageOfReview(reviewId, file);

        verify(review).setImage(image);
        assertThat(result).isEqualTo(reviewId);
    }

    @Test
    @DisplayName("리뷰 이미지 로딩 성공")
    void loadImageAsResource() {
        Long reviewId = 1L;
        Review review = mock(Review.class);
        Image image = new Image("original.jpg", "stored.jpg");
        byte[] data = new byte[]{1, 2, 3};

        when(reviewService.findById(reviewId)).thenReturn(review);
        when(review.getImage()).thenReturn(image);
        when(fileManager.loadImageAsResource("stored.jpg")).thenReturn(data);

        byte[] result = facadeService.loadImageAsResource(reviewId);

        assertThat(result).isEqualTo(data);
    }
}
