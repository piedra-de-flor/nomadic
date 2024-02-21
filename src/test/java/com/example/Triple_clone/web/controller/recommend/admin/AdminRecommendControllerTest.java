package com.example.Triple_clone.web.controller.recommend.admin;

import com.example.Triple_clone.domain.entity.Place;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendCreatePlaceDto;
import com.example.Triple_clone.dto.recommend.admin.AdminRecommendUpdatePlaceDto;
import com.example.Triple_clone.service.recommend.admin.AdminRecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class AdminRecommendControllerTest {

    @MockBean
    private AdminRecommendService adminRecommendService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 관리자_Controller_추천_장소_생성_테스트() throws Exception {
        // given
        AdminRecommendCreatePlaceDto request = new AdminRecommendCreatePlaceDto("test", "test", "test", "test", "test");
        Place response = request.toEntity();
        when(adminRecommendService.createPlace(any(AdminRecommendCreatePlaceDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "ADMIN")
                        .content("{\"title\":\"test\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
                .andReturn();

        // then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertThat(mvcResult.getResponse()
                .getContentAsString()
                .contains("\"title\":\"test\",\"notionUrl\":\"test\",\"subTitle\":\"test\",\"location\":\"test\",\"mainImage\":\"test\""))
                .isEqualTo(true);
    }

    @Test
    void 관리자_Controller_추천_장소_생성_실패_테스트_제목_null값() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "ADMIN")
                        .content("{\"title\":null, \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
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
                        .header("Authorization", "ADMIN")
                        .content("{\"title\":\"test\", \"notionUrl\":null, \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
                .andReturn();

        // then
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void 관리자_Controller_추천_장소_수정_테스트() throws Exception {
        // given
        Place response = Place.builder()
                .title("testUpdate")
                .notionUrl("test")
                .subTitle("test")
                .location("test")
                .mainImage("test")
                .build();

        when(adminRecommendService.updatePlace(any(AdminRecommendUpdatePlaceDto.class))).thenReturn(response);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .patch("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "ADMIN")
                        .content("{\"placeId\":\"1\", \"title\":\"testUpdate\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
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
        when(adminRecommendService.deletePlace(any(Long.class))).thenReturn(1L);

        //when && then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/recommend")
                        .header("Authorization", "ADMIN")
                        .param("placeId", "1"))
                .andExpect(status().isOk());
    }
}
