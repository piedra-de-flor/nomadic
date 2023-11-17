package com.example.Triple_clone.service;

import com.example.Triple_clone.dto.RecommendForUserReadResponseDto;
import com.example.Triple_clone.entity.Place;
import com.example.Triple_clone.repository.RecommendForUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecommendForUserService {
    private final RecommendForUserRepository repository;

    RecommendForUserService(RecommendForUserRepository repository) {
        this.repository = repository;
    }

    public RecommendForUserReadResponseDto findById(long placeId, long userId) {
        Optional<Place> place = repository.findById(placeId);

        if (place.isEmpty()) {
            throw new RuntimeException("no entity place");
        }

        Place exsitPlace = place.get();
        boolean likeOrNot = exsitPlace.getLikes().contains(userId);

        return new RecommendForUserReadResponseDto(exsitPlace, likeOrNot);
    }

    public void like(long placeId, Long userId) {
        repository.saveLike(userId, placeId);
    }
}
