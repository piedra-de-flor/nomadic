package com.example.Triple_clone.domain.accommodation.application;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.Room;
import com.example.Triple_clone.domain.accommodation.infra.AccommodationRepository;
import com.example.Triple_clone.domain.accommodation.infra.RoomRepository;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationCreateDto;
import com.example.Triple_clone.domain.accommodation.web.dto.AccommodationDto;
import com.example.Triple_clone.domain.accommodation.web.dto.RoomAddDto;
import com.example.Triple_clone.domain.accommodation.web.dto.RoomUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AccommodationCommandService {
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    public AccommodationDto create(AccommodationCreateDto dto) {
        try {
            log.debug("숙소 생성 요청: name='{}'", dto.getName());
            Accommodation accommodation = dto.toEntity();
            Accommodation savedAccommodation = accommodationRepository.save(accommodation);

            log.info("숙소 생성 완료: id={}, name='{}'", savedAccommodation.getId(), savedAccommodation.getName());

            return AccommodationDto.from(savedAccommodation);

        } catch (Exception e) {
            log.error("숙소 생성 오류: name='{}', error='{}'", dto.getName(), e.getMessage(), e);
            throw new RuntimeException("숙소 생성에 실패했습니다: " + e.getMessage());
        }
    }

    public AccommodationDto update(long id, AccommodationDto dto) {
        try {
            log.debug("숙소 수정 요청: id={}, name='{}'", id, dto.getName());
            Accommodation accommodation = accommodationRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("숙소 수정 실패 - 존재하지 않음: id={}", id);
                        return new EntityNotFoundException("숙소를 찾을 수 없습니다: " + id);
                    });

            accommodation.update(
                    dto.getImage(),
                    dto.getName(),
                    dto.getCategory(),
                    dto.getGrade(),
                    dto.getRegion(),
                    dto.getAddress(),
                    dto.getLandmarkDistance(),
                    dto.getIntro(),
                    dto.getAmenities(),
                    dto.getInfo()
            );

            Accommodation updatedAccommodation = accommodationRepository.save(accommodation);

            log.info("숙소 수정 완료: id={}, name='{}'", updatedAccommodation.getId(), updatedAccommodation.getName());

            return AccommodationDto.from(updatedAccommodation);

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("숙소 수정 오류: id={}, error='{}'", id, e.getMessage(), e);
            throw new RuntimeException("숙소 수정에 실패했습니다: " + e.getMessage());
        }
    }

    public void delete(long id) {
        log.debug("숙소 삭제 요청: id={}", id);
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("객실 삭제 실패 - 존재하지 않음: id={}", id);
                    return new EntityNotFoundException("숙소를 찾을 수 없습니다: " + id);
                });

        accommodationRepository.deleteById(id);

        log.info("숙소 삭제 완료: id={}", id);
    }

    public void removeRoom(long accommodationId, long roomId) {
        log.debug("객실 삭제 요청: accommodationId={}, roomId={}", accommodationId, roomId);
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> {
                    log.warn("객실 삭제 실패 - 존재하지 않음: id={}", accommodationId);
                    return new EntityNotFoundException("숙소를 찾을 수 없습니다: " + accommodationId);
                });

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.warn("객실 삭제 실패 - 존재하지 않음: id={}", roomId);
                    return new EntityNotFoundException("객실을 찾을 수 없습니다: " + roomId);
                });

        if (accommodation.containsRoom(room)) {
            accommodation.removeRoom(room);
            roomRepository.deleteById(roomId);
            log.info("객실 삭제 완료: id={}", roomId);
        } else {
            throw new IllegalArgumentException("객실 삭제에 실패했습니다 : 객실 소유권 없음");
        }
    }

    public void addRoom(long accommodationId, RoomAddDto roomDto) {
        log.debug("객실 추가 요청: accommodationId={}", accommodationId);
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> {
                    log.warn("객실 추가 실패 - 존재하지 않음: id={}", accommodationId);
                    return new EntityNotFoundException("숙소를 찾을 수 없습니다: " + accommodationId);
                });

        Room room = roomDto.toEntity(accommodation);
        accommodation.addRoom(room);
        roomRepository.save(room);

        log.info("객실 추가 완료: name={}", roomDto.getName());
    }

    public void updateRoom(Long accommodationId, Long roomId, RoomUpdateDto roomDto) {
        log.debug("객실 수정 요청: accommodationId={}, roomId={}", accommodationId, roomId);
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> {
                    log.warn("객실 수정 실패 - 존재하지 않음: id={}", accommodationId);
                    return new EntityNotFoundException("숙소를 찾을 수 없습니다: " + accommodationId);
                });

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> {
                    log.warn("객실 수정 실패 - 존재하지 않음: id={}", roomId);
                    return new EntityNotFoundException("객실을 찾을 수 없습니다: " + roomId);
                });

        room.update(
                roomDto.getName(),
                roomDto.getDayusePrice(),
                roomDto.getDayuseSalePrice(),
                roomDto.getHasDayuseDiscount(),
                roomDto.getDayuseSoldout(),
                roomDto.getDayuseTime(),
                roomDto.getStayPrice(),
                roomDto.getStaySalePrice(),
                roomDto.getHasStayDiscount(),
                roomDto.getStaySoldout(),
                roomDto.getStayCheckinTime(),
                roomDto.getStayCheckoutTime(),
                roomDto.getCapacity(),
                roomDto.getMaxCapacity()
        );
    }
}