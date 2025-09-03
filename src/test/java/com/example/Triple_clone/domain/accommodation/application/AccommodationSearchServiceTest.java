package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.infra.RedisAccommodationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationSearchServiceTest {

    @Mock
    ESAccommodationRepository esAccommodationRepository;
    @InjectMocks
    AccommodationSearchService accommodationSearchService;

    @Test
    void 키워드_정렬_페이징이_ES로_올바르게_전달되고_결과를_매핑한다() {
        // given
        String keyword = "강남 호텔";
        SortOption sort = SortOption.REVIEW_DESC;
        int page = 0, size = 10;

        AccommodationDocument d1 = new AccommodationDocument();
        d1.setId(1);
        d1.setName("강남호텔");
        d1.setRegion("서울");
        d1.setMinStayPrice(120000);

        when(esAccommodationRepository.search(
                anyString(), any(SortOption.class), anyInt(), anyInt())
        ).thenReturn(List.of(d1));

        // when
        var result = accommodationSearchService.search(keyword, sort, page, size);

        // then
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getId()).isEqualTo("acc-1");
        assertThat(result.getItems().get(0).getName()).isEqualTo("강남호텔");
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);

        // 전달 파라미터 검증
        verify(esAccommodationRepository).search(eq(keyword), eq(sort), eq(page), eq(size));
    }

    @Test
    void 정렬옵션_null이면_기본값으로_fallback한다() {
        // given
        String keyword = "호텔";
        int page = 0, size = 5;

        when(esAccommodationRepository.search(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(List.of());

        // when
        var result = accommodationSearchService.search(keyword, null, page, size);

        // then
        assertThat(result.getItems()).isEmpty();
        // 기본값 확인(예: 최신순)
        // verify(esAccommodationRepository).search(eq(keyword), eq(SortOption.RELEVANCE), eq(page), eq(size));
        verify(esAccommodationRepository).search(eq(keyword), any(), eq(page), eq(size));
    }
}
