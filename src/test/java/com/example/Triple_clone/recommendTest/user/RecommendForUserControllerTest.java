package com.example.Triple_clone.recommendTest.user;

import com.example.Triple_clone.controller.recommend.user.RecommendForUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RecommendForUserController.class)
public class RecommendForUserControllerTest {
    @Autowired
    MockMvc mvc;
}
