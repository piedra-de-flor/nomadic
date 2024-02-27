package com.example.Triple_clone.web.controller.recommend.admin;

import com.example.Triple_clone.domain.entity.Recommendation;
import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreateRecommendationDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdateRecommendationDto;
import com.example.Triple_clone.service.recommend.admin.AdminRecommendService;
import com.example.Triple_clone.web.filter.JwtSecurityConfigForTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminRecommendController.class)
@Import(JwtSecurityConfigForTest.class)
public class AdminRecommendControllerTest {

    @MockBean
    private AdminRecommendService adminRecommendService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 관리자_Controller_추천_장소_생성_테스트() throws Exception {
        // given
        AdminRecommendCreateRecommendationDto request = new AdminRecommendCreateRecommendationDto("test", "test", "test",  new Location(1D, 1D, "location"), "test");
        Recommendation response = request.toEntity();
        when(adminRecommendService.createRecommendation(any(AdminRecommendCreateRecommendationDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"test\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1,\"longitude\":1,\"name\":\"location\"}, \"mainImage\":\"test\"}"))
                .andReturn();

        // then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertThat(mvcResult.getResponse()
                .getContentAsString()
                .contains("\"title\":\"test\",\"notionUrl\":\"test\",\"subTitle\":\"test\",\"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"},\"mainImage\":\"test\""))
                .isEqualTo(true);
    }

    @Test
    void 관리자_Controller_추천_장소_생성_실패_테스트_제목_null값() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":null, \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}, \"mainImage\":\"test\"}"))
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
                        .content("{\"title\":\"test\", \"notionUrl\":null, \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}, \"mainImage\":\"test\"}"))
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
                .mainImage("test")
                .build();

        when(adminRecommendService.updateRecommendation(any(AdminRecommendUpdateRecommendationDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .patch("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placeId\":\"1\", \"title\":\"testUpdate\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":{\"latitude\":1.0,\"longitude\":1.0,\"name\":\"location\"}, \"mainImage\":\"test\"}"))
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
        when(adminRecommendService.deleteRecommendation(any(Long.class))).thenReturn(1L);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/recommend")
                        .param("placeId", "1"))
                .andExpect(status().isOk());
    }
}
