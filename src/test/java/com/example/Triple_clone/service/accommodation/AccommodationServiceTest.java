package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.application.AccommodationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AccommodationServiceTest {

    @Mock
    private ESAccommodationRepository esRepository;

    @InjectMocks
    private AccommodationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ES 검색 조건 테스트: AccommodationDto 리스트 반환")
    void testSearchES() {
        Pageable pageable = PageRequest.of(0, 5);

        AccommodationDocument doc = new AccommodationDocument();
        doc.setLocal("서울");
        doc.setName("테스트숙소");
        doc.setCategory("호텔");
        doc.setScore(4.5);
        doc.setLentDiscountRate(10L);
        doc.setLentTime(2);
        doc.setLentOriginPrice(100000L);
        doc.setLentPrice(90000L);
        doc.setLentStatus(true);
        doc.setEnterTime("15:00");
        doc.setLodgmentDiscountRate(5L);
        doc.setLodgmentOriginPrice(200000L);
        doc.setLodgmentPrice(190000L);
        doc.setLodgmentStatus(false);

        Page<AccommodationDocument> mockPage = new PageImpl<>(List.of(doc));

        when(esRepository.searchByConditionsFromES(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                any(Pageable.class)
        )).thenReturn(mockPage);

        List<AccommodationDto> result = service.searchES(
                "서울", "테스트", "10", "50000", "150000", "호텔", "4.0", "true",
                "15:00", "100000", "250000", "false", pageable
        );

        assertThat(result).hasSize(1);
        AccommodationDto dto = result.get(0);
        assertThat(dto.name()).isEqualTo("테스트숙소");
        assertThat(dto.local()).isEqualTo("서울");
        assertThat(dto.category()).isEqualTo("호텔");
        assertThat(dto.lentPrice()).isEqualTo(90000L);
        assertThat(dto.lodgmentStatus()).isFalse();

        verify(esRepository, times(1)).searchByConditionsFromES(
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                any(Pageable.class)
        );
    }
}
