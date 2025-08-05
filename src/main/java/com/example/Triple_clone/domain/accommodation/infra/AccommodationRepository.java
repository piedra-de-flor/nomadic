package com.example.Triple_clone.domain.accommodation.infra;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("SELECT DISTINCT a FROM Accommodation a LEFT JOIN FETCH a.rooms")
    List<Accommodation> findAllWithRooms();

    @Query("SELECT DISTINCT a FROM Accommodation a LEFT JOIN FETCH a.rooms WHERE a.id IN :ids")
    List<Accommodation> findByIdsWithRooms(@Param("ids") List<Long> ids);
    List<Accommodation> readAllByRegion(String region);
}
