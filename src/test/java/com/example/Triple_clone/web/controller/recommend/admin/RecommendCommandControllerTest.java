package com.example.Triple_clone.web.controller.recommend.admin;

import com.example.Triple_clone.domain.recommend.domain.Recommendation;
import com.example.Triple_clone.domain.recommend.web.controller.RecommendCommandController;
import com.example.Triple_clone.domain.plan.domain.Location;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationCreateDto;
import com.example.Triple_clone.domain.recommend.web.dto.RecommendationUpdateDto;
import com.example.Triple_clone.domain.recommend.application.RecommendCommandService;
import com.example.Triple_clone.web.filter.JwtSecurityConfigForTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendCommandController.class)
@Import(JwtSecurityConfigForTest.class)
public class RecommendCommandControllerTest {

    @MockBean
    private RecommendCommandService recommendCommandService;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    MultipartFile file;

    @Test
    void 관리자_Controller_추천_장소_생성_테스트() throws Exception {
        // given
        RecommendationCreateDto request = new RecommendationCreateDto("test", "test", "test",  new Location(1D, 1D, "location"));
        Recommendation response = request.toEntity();
        when(recommendCommandService.createRecommendation(any(RecommendationCreateDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"test\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1,\"longitude\":1,\"name\":\"location\"}}"))
                .andReturn();

        // then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertThat(mvcResult.getResponse()
                .getContentAsString()
                .contains("\"title\":\"test\",\"notionUrl\":\"test\",\"subTitle\":\"test\",\"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}"))
                .isEqualTo(true);
    }

    @Test
    void 관리자_Controller_추천_장소_생성_실패_테스트_제목_null값() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":null, \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}}"))
                .andReturn();

        // then
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void 관리자_Controller_추천_장소_생성_실패_테스트_notion_null값() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"test\", \"notionUrl\":null, \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}}"))
                .andReturn();

        // then
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void 관리자_Controller_추천_장소_수정_테스트() throws Exception {
        // given
        Recommendation response = Recommendation.builder()
                .title("testUpdate")
                .notionUrl("test")
                .subTitle("test")
                .location( new Location(1D, 1D, "location"))
                .build();

        when(recommendCommandService.updateRecommendation(any(RecommendationUpdateDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .patch("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placeId\":\"1\", \"title\":\"testUpdate\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}}"))
                .andReturn();

        // then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertThat(mvcResult.getResponse()
                .getContentAsString()
                .contains("\"title\":\"testUpdate\""))
                .isEqualTo(true);
    }

    @Test
    void 관리자_Controller_추천_장소_삭제_테스트() throws Exception {
        //given
        when(recommendCommandService.deleteRecommendation(any(Long.class))).thenReturn(1L);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/recommend")
                        .param("placeId", "1"))
                .andExpect(status().isOk());
    }
}
