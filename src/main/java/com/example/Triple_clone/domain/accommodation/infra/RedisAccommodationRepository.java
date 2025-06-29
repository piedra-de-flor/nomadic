package com.example.Triple_clone.domain.accommodation.infra;

import com.example.Triple_clone.domain.accommodation.domain.RedisAccommodation;
import org.springframework.data.repository.CrudRepository;

public interface RedisAccommodationRepository extends CrudRepository<RedisAccommodation, Long> {
}

