package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository repository;
    private final ESAccommodationRepositoryImpl esRepository;
    private final static int PAGE_SIZE = 5;

    public List<AccommodationDto> searchES(
            String local,
            String name,
            String discountRate,
            String startLentPrice,
            String endLentPrice,
            String category,
            String score,
            String lentStatus,
            String enterTime,
            String startLodgmentPrice,
            String endLodgmentPrice,
            String lodgmentStatus,
            Pageable pageable) {
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.unsorted());
        List<AccommodationDto> response = new ArrayList<>();

        Page<AccommodationDocument> documents = esRepository.searchByConditionsFromES(
                local, name, category, discountRate, startLentPrice, endLentPrice,
                score, lentStatus, startLodgmentPrice, endLodgmentPrice,enterTime, lodgmentStatus, customPageable);

        for (AccommodationDocument document : documents) {
            AccommodationDto dto = new AccommodationDto(document);
            response.add(dto);
        }

        return response;
    }

    public Accommodation findById(long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
