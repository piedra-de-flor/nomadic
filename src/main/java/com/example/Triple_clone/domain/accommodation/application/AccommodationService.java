package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import com.example.Triple_clone.domain.accommodation.infra.ESAccommodationRepositoryImpl;
import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
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
            String searchKeyword,
            String category,
            Float ratingMin, Float ratingMax,
            String region,
            Integer dayusePriceMin, Integer dayusePriceMax,
            Boolean dayuseAvailable,
            Boolean hasDayuseDiscount,
            Integer stayPriceMin, Integer stayPriceMax,
            Boolean stayAvailable,
            Boolean hasStayDiscount,
            Integer roomPriceMin, Integer roomPriceMax,
            Integer roomCapacityMin, Integer roomCapacityMax,
            String roomCheckoutTime,
            SortOption sortOption,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), PAGE_SIZE, Sort.unsorted());
        List<AccommodationDto> response = new ArrayList<>();

        Page<AccommodationDocument> documents = esRepository.searchByConditionsFromES(
                searchKeyword,
                category,
                ratingMin, ratingMax,
                region,
                dayusePriceMin, dayusePriceMax,
                dayuseAvailable,
                hasDayuseDiscount,
                stayPriceMin, stayPriceMax,
                stayAvailable,
                hasStayDiscount,
                roomPriceMin, roomPriceMax,
                roomCapacityMin, roomCapacityMax,
                roomCheckoutTime,
                sortOption,
                customPageable
        );

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
