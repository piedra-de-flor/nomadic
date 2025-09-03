package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AutocompleteResult;
import com.example.Triple_clone.domain.accommodation.web.dto.SpellCheckResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationQueryServiceTest {

    @Mock AccommodationRepository repository;
    @Mock AccommodationSearchService searchService;

    @InjectMocks AccommodationQueryService service;

    @Test
    @DisplayName("ID로 숙소 조회 - 존재하면 반환")
    void 아이디로_숙소조회_성공() {
        when(repository.findById(1L)).thenReturn(Optional.of(
                Accommodation.builder().name("호텔").region("서울").address("주소").build()
        ));

        Accommodation found = service.findById(1L);

        assertThat(found.getName()).isEqualTo("호텔");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("ID로 숙소 조회 - 없으면 예외")
    void 아이디로_숙소조회_없음() {
        when(repository.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(2L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("자동완성 - 정상 위임 및 결과 반환")
    void 자동완성_성공() {
        when(searchService.getAutocomplete("강남", 5))
                .thenReturn(List.of(AutocompleteResult.builder().text("강남호텔").build()));

        List<AutocompleteResult> results = service.getSmartAutocomplete("강남", 5);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getText()).isEqualTo("강남호텔");
    }

    @Test
    @DisplayName("오타 교정 - 정상 위임 및 결과 반환")
    void 오타교정_성공() {
        SpellCheckResponse resp = SpellCheckResponse.builder()
                .originalQuery("감남")
                .correctedQuery("강남")
                .hasCorrection(true)
                .build();
        when(searchService.checkSpelling("감남")).thenReturn(resp);

        SpellCheckResponse out = service.checkSpelling("감남");
        assertThat(out.getCorrectedQuery()).isEqualTo("강남");
        assertThat(out.getHasCorrection()).isTrue();
    }

    @Test
    @DisplayName("정렬 검색 - 정상 위임 및 결과 반환")
    void 정렬검색_성공() {
        AccommodationDocument doc = AccommodationDocument.builder().id(1).name("강남호텔").build();
        when(searchService.searchWithSort("강남", SortOption.ID_ASC, 0, 10))
                .thenReturn(List.of(doc));

        List<AccommodationDocument> results = service.searchAccommodations("강남", SortOption.ID_ASC, 0, 10);
        assertThat(results).extracting(AccommodationDocument::getName).contains("강남호텔");
    }
}
