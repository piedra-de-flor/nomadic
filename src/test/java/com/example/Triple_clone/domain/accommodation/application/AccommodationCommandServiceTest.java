package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.infra.RedisAccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationCreateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationCommandServiceTest {

    @Mock private AccommodationRepository accommodationRepository;
    @Mock private ESAccommodationRepository esAccommodationRepository;
    @InjectMocks private AccommodationCommandService service;

    @Test
    void create_shouldSaveToDbAndIndexToEsAndEvictRedis() {
        AccommodationCreateDto dto = new AccommodationCreateDto("강남호텔", "서울", 100000);
        Accommodation saved = new Accommodation(1, "강남호텔", "서울", 100000);

        when(accommodationRepository.save(any())).thenReturn(saved);

        String id = service.create(dto);

        assertThat(id).isEqualTo("acc-1");
        verify(accommodationRepository).save(any());
        verify(esAccommodationRepository).save(any());
        verify(redisAccommodationRepository).deleteById(id);
    }

    @Test
    void update_shouldUpdateAndReindex() {
        AccommodationUpdateDto dto = new AccommodationUpdateDto("acc-1", "리뉴얼호텔", "서울", 120000);
        Accommodation found = new Accommodation("acc-1", "강남호텔", "서울", 100000);

        when(accommodationRepository.findById("acc-1")).thenReturn(Optional.of(found));

        String id = service.update(dto);

        assertThat(id).isEqualTo("acc-1");
        verify(accommodationRepository).save(any());
        verify(esAccommodationRepository).save(any());
        verify(redisAccommodationRepository).deleteById(id);
    }

    @Test
    void delete_shouldRemoveFromDbEsAndRedis() {
        service.delete("acc-1");
        verify(accommodationRepository).deleteById("acc-1");
        verify(esAccommodationRepository).deleteById("acc-1");
        verify(redisAccommodationRepository).deleteById("acc-1");
    }
}
