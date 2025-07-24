package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationCommandService {
    private final AccommodationRepository repository;

    public AccommodationDto create(AccommodationDto dto) {
        Accommodation accommodation = dto.toEntity();
        Accommodation saved = repository.save(accommodation);

        return new AccommodationDto(saved);
    }

    public AccommodationDto update(long id, AccommodationDto dto) {
        Accommodation accommodation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no accommodation entity"));
        accommodation.update(dto);
        return dto;
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}

