package com.example.Triple_clone.domain.accommodation.web.controller;

import com.example.Triple_clone.domain.accommodation.application.AccommodationCommandService;
import com.example.Triple_clone.domain.accommodation.application.AccommodationQueryService;
import com.example.Triple_clone.domain.accommodation.domain.AccommodationDocument;
import com.example.Triple_clone.domain.accommodation.domain.SortOption;
import com.example.Triple_clone.domain.accommodation.web.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccommodationController.class)
class AccommodationControllerTest {

    @Autowired MockMvc mvc;

    @MockBean AccommodationCommandService commandService;
    @MockBean AccommodationQueryService queryService;

    @Test
    @DisplayName("자동완성 API - 200 OK")
    void 자동완성_API() throws Exception {
        given(queryService.getSmartAutocomplete("강", 5))
                .willReturn(List.of(AutocompleteResult.builder().text("강남호텔").build()));

        mvc.perform(get("/accommodations/autocomplete")
                        .param("q", "강").param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("강남호텔"));
    }

    @Test
    @DisplayName("오타교정 API - 200 OK")
    void 오타교정_API() throws Exception {
        SpellCheckResponse resp = SpellCheckResponse.builder()
                .originalQuery("감남").correctedQuery("강남").hasCorrection(true).build();
        given(queryService.checkSpelling("감남")).willReturn(resp);

        mvc.perform(get("/accommodations/typo-correction").param("query", "감남"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correctedQuery").value("강남"))
                .andExpect(jsonPath("$.hasCorrection").value(true));
    }

    @Test
    @DisplayName("정렬 검색 API - 200 OK")
    void 정렬검색_API() throws Exception {
        given(queryService.searchAccommodations(eq("호텔"), eq(SortOption.ID_ASC), eq(0), eq(20)))
                .willReturn(List.of(AccommodationDocument.builder().id(1).name("A").build()));

        mvc.perform(get("/accommodations/search")
                        .param("q", "호텔")
                        .param("sort", "ID_ASC")
                        .param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("A"));
    }

    @Test
    @DisplayName("숙소 생성 API - 201 Created")
    void 숙소생성_API() throws Exception {
        AccommodationDto created = AccommodationDto.builder().id(1L).name("신규").region("서울").address("주소").build();
        given(commandService.create(any(AccommodationCreateDto.class))).willReturn(created);

        String body = """
                {
                  "name":"신규",
                  "region":"서울",
                  "address":"주소"
                }
                """;

        mvc.perform(post("/accommodations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("객실 추가 API - 201 Created")
    void 객실추가_API() throws Exception {
        String body = """
                {
                  "name":"디럭스",
                  "capacity":2,
                  "maxCapacity":3
                }
                """;

        mvc.perform(post("/accommodations/{id}/rooms", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("객실 수정/삭제 API - 200 OK")
    void 객실수정삭제_API() throws Exception {
        String patch = """
                { "name":"업데이트룸", "capacity":3, "maxCapacity":4 }
                """;

        mvc.perform(put("/accommodations/{aid}/rooms/{rid}", 1L, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patch))
                .andExpect(status().isOk());

        mvc.perform(delete("/accommodations/{aid}/rooms/{rid}", 1L, 2L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("숙소 삭제 API - 200 OK")
    void 숙소삭제_API() throws Exception {
        mvc.perform(delete("/accommodations/{id}", 9L))
                .andExpect(status().isOk());
    }
}
