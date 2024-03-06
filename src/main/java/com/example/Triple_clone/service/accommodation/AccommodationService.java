package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.entity.Accommodation;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.repository.AccommodationRepository;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository repository;
    private final FileManager fileManager;

    @Transactional
    public List<AccommodationDto> saveAllAccommodations(String local) {
        List<AccommodationDto> response = fileManager.readHotelsFromFile(local);
        for (AccommodationDto dto : response) {
            Accommodation accommodation = dto.toEntity();
            repository.save(accommodation);
        }
        return response;
    }

    public List<AccommodationDto> readAll(String local) {
        List<Accommodation> accommodations = repository.readAllByLocal(local);
        List<AccommodationDto> response = new ArrayList<>();

        for (Accommodation accommodation : accommodations) {
            AccommodationDto dto = new AccommodationDto(accommodation);
            response.add(dto);
        }

        return response;
    }
}
