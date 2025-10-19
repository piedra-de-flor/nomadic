package com.example.Triple_clone.common.exception;

import com.example.Triple_clone.common.error.GlobalErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DummyExceptionController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void handleCustomExceptionTest() throws Exception {
        mockMvc.perform(get("/exception/rest-api"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.UNAUTHORIZED.name()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void handleNoSuchElementExceptionTest() throws Exception {
        mockMvc.perform(get("/exception/no-element"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.RESOURCE_NOT_FOUND.name()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void handleMethodArgumentNotValidTest() throws Exception {
        mockMvc.perform(get("/exception/invalid-param")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INVALID_PARAMETER.name()));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void handleAllExceptionTest() throws Exception {
        mockMvc.perform(get("/exception/unknown"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(GlobalErrorCode.INTERNAL_SERVER_ERROR.name()));
    }
}
