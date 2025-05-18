package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.entity.Review;
import com.example.Triple_clone.domain.vo.Image;
import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.dto.review.ReviewResponseDto;
import com.example.Triple_clone.dto.review.ReviewUpdateDto;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import com.example.Triple_clone.service.support.FileManager;
import com.example.Triple_clone.web.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
    @DisplayName("부모 없는 리뷰 작성 성공")
    void writeReview_success() {
        RecommendWriteReviewDto dto = mock(RecommendWriteReviewDto.class);
        Recommendation recommendation = mock(Recommendation.class);
        Member member = mock(Member.class);
        Review review = mock(Review.class);

        when(dto.placeId()).thenReturn(100L);
        when(dto.parentId()).thenReturn(null);
        when(dto.toEntity(member, recommendation, null)).thenReturn(review);

        when(recommendService.findById(100L)).thenReturn(recommendation);
        when(userService.findByEmail("test")).thenReturn(member);

        facadeService.writeReview(dto, "test");

        verify(reviewService).save(review);
        verify(recommendation).addReview(review);
    }

    @Test
    @DisplayName("대댓글 작성 성공 (depth 1까지 허용)")
    void writeReply_success() {
        RecommendWriteReviewDto dto = mock(RecommendWriteReviewDto.class);
        Recommendation recommendation = mock(Recommendation.class);
        Member member = mock(Member.class);
        Review parent = mock(Review.class);
        Review child = mock(Review.class);

        when(dto.placeId()).thenReturn(100L);
        when(dto.parentId()).thenReturn(10L);
        when(dto.toEntity(member, recommendation, parent)).thenReturn(child);

        when(recommendService.findById(100L)).thenReturn(recommendation);
        when(userService.findByEmail("test")).thenReturn(member);
        when(reviewService.findById(10L)).thenReturn(parent);
        when(parent.getParent()).thenReturn(null);

        facadeService.writeReview(dto, "test");

        verify(reviewService).save(child);
        verify(recommendation).addReview(child);
    }

    @Test
    @DisplayName("대댓글의 대댓글은 예외 발생 (depth 2 제한)")
    void writeReply_depthLimitExceeded_throwsException() {
        RecommendWriteReviewDto dto = mock(RecommendWriteReviewDto.class);
        Recommendation recommendation = mock(Recommendation.class);
        Member member = mock(Member.class);
        Review parent = mock(Review.class);

        Review grandParent = mock(Review.class);

        when(dto.placeId()).thenReturn(100L);
        when(dto.parentId()).thenReturn(10L);

        when(recommendService.findById(100L)).thenReturn(recommendation);
        when(userService.findByEmail("test")).thenReturn(member);
        when(reviewService.findById(10L)).thenReturn(parent);
        when(parent.getParent()).thenReturn(grandParent);

        assertThrows(IllegalArgumentException.class, () -> facadeService.writeReview(dto, "test"));
    }

    @Test
    @DisplayName("대댓글 작성 성공")
    void writeReplyReviewSuccess() {
        Member member = new Member("test", "test", "test", new ArrayList<>());
        Recommendation recommendation = new Recommendation("test", "test", "test", new Location());
        Review parentReview = new Review(member, recommendation, "부모 댓글");

        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(
                100L,
                "대댓글입니다.",
                parentReview.getId()
        );

        when(userService.findByEmail(member.getEmail())).thenReturn(member);
        when(recommendService.findById(100L)).thenReturn(recommendation);
        when(reviewService.findById(anyLong())).thenReturn(parentReview);

        facadeService.writeReview(dto, member.getEmail());

        assertEquals(1, parentReview.getChildren().size());
        assertEquals("대댓글입니다.", parentReview.getChildren().get(0).getContent());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReviewSuccess() {
        Long reviewId = 1L;
        String memberEmail = "test";

        Member member = mock(Member.class);
        Review review = mock(Review.class);

        when(member.getId()).thenReturn(1L);
        when(review.getMember()).thenReturn(member);
        when(review.getMember().getId()).thenReturn(1L);
        when(userService.findByEmail(memberEmail)).thenReturn(member);
        when(reviewService.findById(reviewId)).thenReturn(review);

        facadeService.deleteReview(reviewId, memberEmail);

        verify(reviewService).delete(review);
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 작성자 불일치")
    void deleteReviewFail_Unauthorized() {
        Long reviewId = 1L;
        String memberEmail = "test";
        Long otherMemberId = 99L;

        Member member = mock(Member.class);
        Member otherMember = mock(Member.class);
        Review review = mock(Review.class);

        when(member.getId()).thenReturn(1L);
        when(otherMember.getId()).thenReturn(otherMemberId);
        when(review.getMember()).thenReturn(otherMember);
        when(userService.findByEmail(memberEmail)).thenReturn(member);
        when(reviewService.findById(reviewId)).thenReturn(review);

        assertThatThrownBy(() -> facadeService.deleteReview(reviewId, memberEmail))
                .isInstanceOf(RestApiException.class);

        verify(reviewService, never()).delete(any());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void updateReviewSuccess() {
        Long reviewId = 1L;
        String email = "test";
        String newContent = "Updated content";

        Member member = mock(Member.class);
        Review review = mock(Review.class);
        ReviewUpdateDto updateDto = new ReviewUpdateDto(reviewId, newContent);

        when(member.getEmail()).thenReturn(email);
        when(member.getId()).thenReturn(10L);
        when(review.getMember()).thenReturn(member);
        when(review.getId()).thenReturn(reviewId);
        when(review.getContent()).thenReturn(newContent);
        when(userService.findByEmail(email)).thenReturn(member);
        when(reviewService.findById(reviewId)).thenReturn(review);

        ReviewResponseDto response = facadeService.updateReview(updateDto, member.getEmail());

        verify(reviewService).update(review, newContent);
        assertThat(response.getId()).isEqualTo(reviewId);
        assertThat(response.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 작성자 불일치")
    void updateReviewFail_Unauthorized() {
        Long reviewId = 1L;
        String email = "test";
        Long memberId = 10L;
        Long otherMemberId = 99L;

        String newContent = "Updated content";
        Member member = mock(Member.class);
        Member otherMember = mock(Member.class);
        Review review = mock(Review.class);
        ReviewUpdateDto updateDto = new ReviewUpdateDto(reviewId, newContent);

        when(member.getId()).thenReturn(memberId);
        when(otherMember.getId()).thenReturn(otherMemberId);
        when(review.getMember()).thenReturn(otherMember);
        when(userService.findByEmail(email)).thenReturn(member);
        when(reviewService.findById(reviewId)).thenReturn(review);

        assertThatThrownBy(() -> facadeService.updateReview(updateDto, email))
                .isInstanceOf(RestApiException.class);

        verify(reviewService, never()).update(any(), any());
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
