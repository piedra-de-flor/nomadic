package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationCommandService {
    private final AccommodationRepository repository;

    public AccommodationDto create(AccommodationDto dto) {
        //구현해야함
    }

    public AccommodationDto update(long id, AccommodationDto dto) {
        //구현해야함
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}

