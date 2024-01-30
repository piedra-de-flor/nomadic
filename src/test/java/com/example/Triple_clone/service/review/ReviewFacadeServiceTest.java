package com.example.Triple_clone.service.review;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.dto.recommend.user.RecommendWriteReviewDto;
import com.example.Triple_clone.service.membership.UserService;
import com.example.Triple_clone.service.recommend.user.RecommendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewFacadeServiceTest {
    @MockBean
    ReviewFacadeService reviewFacadeService;
    @Mock
    UserService userService;
    @Mock
    RecommendService recommendService;
    @Mock
    ReviewService reviewService;

    @Mock
    User user;
    @Mock
    Place place;

    @Test
    void 리뷰_작성_실패_유저_없음_테스트() {
        when(userService.findById(2)).thenThrow(NoSuchElementException.class);
        when(recommendService.getById(1)).thenReturn(place);

        reviewFacadeService = new ReviewFacadeService(userService, reviewService, recommendService);
        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(2, 1, "test", "test");

        Assertions.assertThrows(NoSuchElementException.class, ()
                -> reviewFacadeService.writeReview(dto));
    }

    @Test
    void 리뷰_작성_실패_장소_없음_테스트() {
        when(userService.findById(1)).thenReturn(user);
        when(recommendService.getById(2)).thenThrow(NoSuchElementException.class);

        reviewFacadeService = new ReviewFacadeService(userService, reviewService, recommendService);
        RecommendWriteReviewDto dto = new RecommendWriteReviewDto(1, 2, "test", "test");

        Assertions.assertThrows(NoSuchElementException.class, ()
                -> reviewFacadeService.writeReview(dto));
    }
}
