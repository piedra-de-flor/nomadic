package com.example.Triple_clone.web.exception;

import com.example.Triple_clone.domain.vo.GlobalErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/exception")
class DummyExceptionController {

    @GetMapping("/rest-api")
    public void restApiException() {
        throw new RestApiException(GlobalErrorCode.UNAUTHORIZED);
    }

    @GetMapping("/no-element")
    public void noSuchElement() {
        throw new NoSuchElementException("No value present");
    }

    @GetMapping("/invalid-param")
    public void invalidParam(@Valid @RequestBody DummyRequest request) {
    }

    @GetMapping("/unknown")
    public void unknownException() {
        throw new RuntimeException("Unknown error");
    }

    static class DummyRequest {
        @NotBlank
        public String name;
    }
}
