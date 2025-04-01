package com.example.Triple_clone.repository;

import com.example.Triple_clone.domain.entity.AccommodationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESRepository extends ESAccommodationRepository, ElasticsearchRepository<AccommodationDocument, String> {
}
