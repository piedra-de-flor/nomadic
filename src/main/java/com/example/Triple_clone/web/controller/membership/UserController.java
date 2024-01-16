package com.example.Triple_clone.web.controller.membership;

import com.example.Triple_clone.dto.membership.*;
import com.example.Triple_clone.service.membership.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@RequestBody @Valid final UserJoinRequestDto userJoinRequestDto) {
        UserResponseDto responseDto = service.join(userJoinRequestDto);
        return ResponseEntity.ok(responseDto);
    }

/*  @PostMapping("/login")
    public JwtTokenDto signIn(@RequestBody LoginDto signInDto) {
      return service.login(signInDto);
    }*/

    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> read(@RequestParam long userId) {
        UserResponseDto responseDto = service.read(userId);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/user")
    public ResponseEntity<UserUpdateDto> update(@RequestBody final UserUpdateDto userUpdateDto) {
        service.update(userUpdateDto);
        return ResponseEntity.ok(userUpdateDto);
    }

    @DeleteMapping("/user")
    public ResponseEntity<Long> delete(@RequestParam long userId) {
        long deletedUserId = service.delete(userId);
        return ResponseEntity.ok(deletedUserId);
    }
}
