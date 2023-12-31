package com.example.Triple_clone.web.controller.membership;

import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.service.membership.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> join(@RequestBody @Valid final UserJoinRequestDto userJoinRequestDto) {
        UserResponseDto responseDto = service.join(userJoinRequestDto);
        return ResponseEntity
                .created(URI.create("/user/" + responseDto.userId()))
                .body(responseDto);
    }

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
