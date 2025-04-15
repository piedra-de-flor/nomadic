package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.entity.Accommodation;
import com.example.Triple_clone.domain.entity.AccommodationDocument;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.repository.AccommodationRepository;
import com.example.Triple_clone.repository.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.repository.ESRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<AccommodationDto> searchDB(String local,
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
        Page<Accommodation> accommodationPage;
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.unsorted());
        List<AccommodationDto> response = new ArrayList<>();

        accommodationPage = repository.searchByConditionsFromDB(local, name, discountRate, startLentPrice, endLentPrice,
                category, score, lentStatus, enterTime, startLodgmentPrice, endLodgmentPrice, lodgmentStatus, customPageable);

        for (Accommodation accommodation : accommodationPage) {
            AccommodationDto dto = new AccommodationDto(accommodation);
            response.add(dto);
        }

        return response;
    }

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

        if (documents.isEmpty()) {
            return response;
            /*log.info("no date in ES, search RDB");
            return searchDB(local, name, category, discountRate, startLentPrice, endLentPrice,
                    score, lentStatus, startLodgmentPrice, endLodgmentPrice,enterTime, lodgmentStatus, pageable);*/
        }

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
