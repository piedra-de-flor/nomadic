package com.example.Triple_clone.web.controller.membership;

import com.example.Triple_clone.dto.auth.JwtToken;
import com.example.Triple_clone.dto.membership.LoginDto;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.service.membership.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "유저 Controller", description = "USER API")
public class UserController {
    private final UserService service;
    private final String testJenkinsCI = "Successsss";

    @Operation(summary = "회원가입", description = "새로운 회원을 생성합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(
            @Parameter(description = "회원 가입 요청 정보", required = true)
            @RequestBody @Validated final UserJoinRequestDto userJoinRequestDto) {
        UserResponseDto responseDto = service.signUp(userJoinRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "로그인", description = "이메일와 비밀번호를 통해 로그인합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PostMapping("/sign-in")
    public JwtToken signIn(
            @Parameter(description = "로그인 요청 정보", required = true)
            @RequestBody @Validated LoginDto signInDto) {
      return service.signIn(signInDto.email(), signInDto.password());
    }

    @Operation(summary = "회원 조회", description = "기존의 회원을 단일 조회합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> read(
            @Parameter(description = "회원 조회 요청 정보", required = true)
            @RequestParam long userId) {
        UserResponseDto responseDto = service.read(userId);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "회원 수정", description = "기존의 회원 정보를 수정합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @PatchMapping("/user")
    public ResponseEntity<UserUpdateDto> update(
            @Parameter(description = "회원 수정 요청 정보", required = true)
            @RequestBody @Validated UserUpdateDto userUpdateDto) {
        service.update(userUpdateDto);
        return ResponseEntity.ok(userUpdateDto);
    }

    @Operation(summary = "회원 삭제", description = "기존의 회원을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @DeleteMapping("/user")
    public ResponseEntity<Long> delete(
            @Parameter(description = "회원 삭제 요청 정보", required = true)
            @RequestParam long userId) {
        long deletedUserId = service.delete(userId);
        return ResponseEntity.ok(deletedUserId);
    }
}
