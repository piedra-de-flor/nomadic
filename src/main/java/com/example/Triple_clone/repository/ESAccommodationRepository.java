package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.AccommodationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ESAccommodationRepository extends ElasticsearchRepository<AccommodationDocument, Long> {
    List<AccommodationDocument> findByLocal(String local);

    List<AccommodationDocument> findByLocalAndCategory(String local, String category);

    List<AccommodationDocument> findByLentPriceBetween(Long minPrice, Long maxPrice);
}
