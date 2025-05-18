package com.example.Triple_clone.service.recommend.user;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.dto.recommend.user.RecommendReadDto;
import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.repository.RecommendationRepository;
import com.example.Triple_clone.service.membership.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Recommendation recommendation1;

    @Mock
    private Recommendation recommendation2;

    @InjectMocks
    private RecommendService service;

    @Mock
    Pageable pageable;


    @Test
    void 서비스_레이어_장소_단일_조회_테스트() {
        Member member = mock(Member.class);

        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation1));
        when(memberRepository.findByEmail("test")).thenReturn(Optional.ofNullable(member));
        when(recommendation1.getId()).thenReturn(1L);
        when(member.getId()).thenReturn(1L);

        RecommendReadDto responseDto = service.findById(1L, "test");

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(recommendation1.getId());
    }

    @Test
    void 서비스_레이어_장소_단일_조회_실패_테스트() {
        when(recommendationRepository.findById(2L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> service.findById(2L, "test"));
    }

    @Test
    void 서비스_레이어_장소_전체_조회_날짜순_테스트() {
        List<Recommendation> list = new ArrayList<>();
        list.add(recommendation1);
        list.add(recommendation2);
        Page<Recommendation> page = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());

        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), 5, Sort.by("date").descending());
        when(recommendationRepository.findAllByOrderByDateDesc(customPageable)).thenReturn(page);

        when(recommendation1.getDate()).thenReturn(LocalDateTime.of(2023, 11, 1, 0, 0));
        when(recommendation2.getDate()).thenReturn(LocalDateTime.of(2023, 10, 15, 0, 0));

        Page<RecommendReadDto> responsePage = service.findAll("date", pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).date()).isEqualTo(recommendation1.getDate());
    }

    @Test
    void 서비스_레이어_장소_전체_조회_이름순_테스트() {
        List<Recommendation> list = new ArrayList<>();
        list.add(recommendation1);
        list.add(recommendation2);
        Page<Recommendation> page = new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());

        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), 5, Sort.by("title").descending());
        when(recommendationRepository.findAllByOrderByTitleDesc(customPageable)).thenReturn(page);

        when(recommendation1.getTitle()).thenReturn("AAA");
        when(recommendation2.getTitle()).thenReturn("BBB");

        Page<RecommendReadDto> responsePage = service.findAll("title", pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent().size()).isEqualTo(2);
        assertThat(responsePage.getContent().get(0).title()).isEqualTo(recommendation1.getTitle());
    }


    @Test
    void 서비스_레이어_장소_좋아요_테스트() {
        when(recommendation1.getId()).thenReturn(1L);
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation1));

        service.like(recommendation1.getId(), 1L);
        service.saveLike();

        verify(recommendation1, times(1)).like(1L);
    }

    @Test
    void 서비스_레이어_장소_좋아요_실패_테스트() {
        when(recommendation1.getId()).thenThrow(NoSuchElementException.class);

        Assertions.assertThrows(NoSuchElementException.class, () ->  service.like(recommendation1.getId(), 1L));
    }
}
