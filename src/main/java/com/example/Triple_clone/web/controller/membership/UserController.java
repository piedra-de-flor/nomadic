package com.example.Triple_clone.web.controller.membership;

import com.example.Triple_clone.dto.membership.LoginDto;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.service.membership.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    private final String testJenkinsCI = "Successsss";

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody @Validated final UserJoinRequestDto userJoinRequestDto) {
        UserResponseDto responseDto = service.join(userJoinRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public UserResponseDto signIn(@RequestBody @Validated LoginDto signInDto) {
      return service.login(signInDto);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> read(@RequestParam long userId) {
        UserResponseDto responseDto = service.read(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserUpdateDto> update(@RequestBody @Validated UserUpdateDto userUpdateDto) {
        service.update(userUpdateDto);
        return ResponseEntity.ok(userUpdateDto);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Long> delete(@RequestParam long userId) {
        long deletedUserId = service.delete(userId);
        return ResponseEntity.ok(deletedUserId);
    }
}
