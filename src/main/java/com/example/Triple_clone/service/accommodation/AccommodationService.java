package com.example.Triple_clone.service.accommodation;

import com.example.Triple_clone.domain.entity.Accommodation;
import com.example.Triple_clone.dto.accommodation.AccommodationDto;
import com.example.Triple_clone.repository.AccommodationRepository;
import com.example.Triple_clone.service.support.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository repository;
    //private final AccommodationDataParsingService parsingService;
    private final static int PAGE_SIZE = 5;

    @Transactional
    public List<AccommodationDto> saveAllAccommodations(String local) {
        List<AccommodationDto> response = new ArrayList<>();//parsingService.parseDatas(local);
        for (AccommodationDto dto : response) {
            Accommodation accommodation = dto.toEntity();
            repository.save(accommodation);
        }
        return response;
    }

    @Transactional(readOnly = true)
    public List<AccommodationDto> readAll(String local,
                                          String name,
                                          String lentDiscountRate,
                                          String startLentPrice,
                                          String endLentPrice,
                                          String category,
                                          String score,
                                          String lentStatus,
                                          String enterTime,
                                          String lodgmentDiscountRate,
                                          String startLodgmentPrice,
                                          String endLodgmentPrice,
                                          String lodgmentStatus,
                                          Pageable pageable) {
        Page<Accommodation> accommodationPage;
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.unsorted());
        List<AccommodationDto> response = new ArrayList<>();

        accommodationPage =repository.findAllByConditions(local, name, lentDiscountRate, startLentPrice, endLentPrice,
                category, score, lentStatus, enterTime, lodgmentDiscountRate, startLodgmentPrice, endLodgmentPrice, lodgmentStatus, customPageable);

        for (Accommodation accommodation : accommodationPage) {
            AccommodationDto dto = new AccommodationDto(accommodation);
            response.add(dto);
        }

        return response;
    }

    public Accommodation findById(long id) {
        return repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
