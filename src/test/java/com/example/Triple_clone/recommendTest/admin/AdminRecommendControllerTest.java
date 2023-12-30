package com.example.Triple_clone.recommendTest.admin;

import com.example.Triple_clone.web.controller.recommend.admin.AdminRecommendController;
import com.example.Triple_clone.service.recommend.manager.AdminRecommendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminRecommendController.class)
public class AdminRecommendControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AdminRecommendService service;

    @Test
    void 관리자_Controller_추천_장소_생성_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"test\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void 관리자_Controller_추천_장소_수정_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/admin/recommend")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"placeId\":\"1\", \"title\":\"test\", \"notionUrl\":\"test\", \"subTitle\":\"test\", \"location\":\"test\", \"mainImage\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void 관리자_Controller_추천_장소_삭제_테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/recommend")
                        .param("placeId", "1"))
                .andExpect(status().isOk());
    }
}
