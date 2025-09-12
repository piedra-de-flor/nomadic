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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccommodationCommandServiceTest {

    @Mock AccommodationRepository accommodationRepository;
    @Mock RoomRepository roomRepository;

    @InjectMocks AccommodationCommandService service;

    @Test
    @DisplayName("숙소 생성 - 성공")
    void 숙소_생성_성공() {
        AccommodationCreateDto dto = AccommodationCreateDto.builder()
                .name("강남호텔").region("서울").address("서울 어딘가")
                .category("호텔").grade("5성급")
                .build();

        Accommodation saved = Accommodation.builder()
                .image(dto.getImage())
                .name(dto.getName())
                .category(dto.getCategory())
                .grade(dto.getGrade())
                .rating(0.0F)
                .reviewCount(0)
                .region(dto.getRegion())
                .address(dto.getAddress())
                .landmarkDistance(dto.getLandmarkDistance())
                .intro(dto.getIntro())
                .amenities(dto.getAmenities())
                .info(dto.getInfo())
                .build();

        when(accommodationRepository.save(any(Accommodation.class))).thenReturn(saved);

        AccommodationDto result = service.create(dto);

        assertThat(result.getName()).isEqualTo("강남호텔");
        verify(accommodationRepository).save(any(Accommodation.class));
    }

    @Test
    @DisplayName("숙소 수정 - 존재하면 업데이트 성공")
    void 숙소_수정_성공() {
        long id = 1L;
        Accommodation origin = Accommodation.builder()
                .name("원래이름").region("서울").address("주소")
                .category("호텔").grade("4성")
                .build();

        when(accommodationRepository.findById(id)).thenReturn(Optional.of(origin));
        when(accommodationRepository.save(any(Accommodation.class))).thenAnswer(inv -> inv.getArgument(0));

        AccommodationDto patch = AccommodationDto.builder()
                .name("수정이름")
                .image("img")
                .category("리조트")
                .grade("5성")
                .region("부산")
                .address("부산 주소")
                .landmarkDistance("해운대 인근")
                .intro("소개")
                .amenities("편의시설")
                .info("정보")
                .build();

        AccommodationDto updated = service.update(id, patch);

        assertThat(updated.getName()).isEqualTo("수정이름");
        assertThat(updated.getRegion()).isEqualTo("부산");
        verify(accommodationRepository).findById(id);
        verify(accommodationRepository).save(any(Accommodation.class));
    }

    @Test
    @DisplayName("숙소 수정 - 없으면 EntityNotFoundException")
    void 숙소_수정_엔티티없음() {
        long id = 9L;
        when(accommodationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, AccommodationDto.builder().name("x").build()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("숙소 삭제 - 성공")
    void 숙소_삭제_성공() {
        long id = 10L;
        when(accommodationRepository.findById(id)).thenReturn(Optional.of(Accommodation.builder().name("a").build()));

        service.delete(id);

        verify(accommodationRepository).deleteById(id);
    }

    @Test
    @DisplayName("객실 추가 - 성공")
    void 객실_추가_성공() {
        long accId = 1L;
        Accommodation acc = Accommodation.builder().name("호텔").region("서울").address("주소").build();
        when(accommodationRepository.findById(accId)).thenReturn(Optional.of(acc));

        RoomAddDto roomAdd = RoomAddDto.builder()
                .name("디럭스").capacity(2).maxCapacity(3)
                .stayPrice(100000).staySalePrice(90000)
                .build();

        service.addRoom(accId, roomAdd);

        verify(roomRepository).save(any(Room.class));
        assertThat(acc.getRooms()).extracting("name").contains("디럭스");
    }

    @Test
    @DisplayName("객실 삭제 - 소유권 없으면 IllegalArgumentException")
    void 객실_삭제_소유권없음() {
        long accId = 1L; long roomId = 2L;
        Accommodation acc = Accommodation.builder().name("호텔").region("서울").address("주소").build();
        Room room = Room.builder().name("타인객실").build();

        when(accommodationRepository.findById(accId)).thenReturn(Optional.of(acc));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        assertThatThrownBy(() -> service.removeRoom(accId, roomId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("소유권");

        verify(roomRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("객실 수정 - 성공")
    void 객실_수정_성공() {
        long accId = 1L; long roomId = 2L;
        Accommodation acc = Accommodation.builder().name("호텔").region("서울").address("주소").build();
        Room room = Room.builder().name("디럭스").capacity(2).maxCapacity(3).build();

        when(accommodationRepository.findById(accId)).thenReturn(Optional.of(acc));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        RoomUpdateDto patch = RoomUpdateDto.builder()
                .name("슈페리어").capacity(3).maxCapacity(4).stayPrice(120000).build();

        service.updateRoom(accId, roomId, patch);

        assertThat(room.getName()).isEqualTo("슈페리어");
        assertThat(room.getCapacity()).isEqualTo(3);
    }
}
