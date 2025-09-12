package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SpellCheckResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationSearchServiceTest {

    @Mock ESAccommodationRepository esRepository;

    @InjectMocks AccommodationSearchService service;

    @Test
    @DisplayName("자동완성 - 빈/공백 쿼리면 빈 리스트")
    void 자동완성_빈쿼리() {
        assertThat(service.getAutocomplete("   ", 5)).isEmpty();
        verifyNoInteractions(esRepository);
    }

    @Test
    @DisplayName("자동완성 - 정상 위임")
    void 자동완성_성공() {
        when(esRepository.searchAccommodationNames("강", 3))
                .thenReturn(List.of(AutocompleteResult.builder().text("강남").build()));

        List<AutocompleteResult> results = service.getAutocomplete("강", 3);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getText()).isEqualTo("강남");
    }

    @Test
    @DisplayName("정렬 검색 - 정상 위임")
    void 정렬검색_성공() {
        when(esRepository.searchAccommodationsWithSort("호텔", SortOption.RATING_DESC, 0, 2))
                .thenReturn(List.of(AccommodationDocument.builder().id(1).name("A").build()));

        List<AccommodationDocument> results = service.searchWithSort("호텔", SortOption.RATING_DESC, 0, 2);
        assertThat(results).extracting(AccommodationDocument::getName).contains("A");
    }

    @Test
    @DisplayName("오타 교정 - 동일 텍스트면 보정 없음")
    void 오타교정_동일문구() {
        SpellCheckResponse out = service.checkSpelling("강남");
        assertThat(out.getHasCorrection()).isFalse();
        assertThat(out.getCorrectedQuery()).isEqualTo("강남");
    }

    @Test
    @DisplayName("오타 교정 - 보정 발생 시 유사 추천 포함")
    void 오타교정_보정발생() {
        when(esRepository.findBestMatch("감남")).thenReturn("강남");
        when(esRepository.getSimilarAccommodationNames("강남", 3)).thenReturn(List.of("강남 호텔", "강남 스테이"));

        SpellCheckResponse out = service.checkSpelling("감남");

        assertThat(out.getHasCorrection()).isTrue();
        assertThat(out.getCorrectedQuery()).isEqualTo("강남");
        assertThat(out.getSuggestions()).contains("강남 호텔", "강남 스테이");
    }
}
