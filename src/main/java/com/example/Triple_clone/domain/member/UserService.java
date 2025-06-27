package com.example.Triple_clone.domain.member;

import com.example.Triple_clone.domain.member.Member;
import com.example.Triple_clone.domain.vo.LogMessage;
import com.example.Triple_clone.dto.auth.JwtToken;
import com.example.Triple_clone.domain.member.UserJoinRequestDto;
import com.example.Triple_clone.domain.member.UserResponseDto;
import com.example.Triple_clone.domain.member.UserUpdateDto;
import com.example.Triple_clone.domain.member.MemberRepository;
import com.example.Triple_clone.service.support.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository repository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signUp(UserJoinRequestDto signUpDto) {
        if (repository.findByEmail(signUpDto.email()).isPresent()) {
            log.warn("⚠️ 사용자 가입 실패 - 중복된 이메일 이메일: {}", signUpDto.email());
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpDto.password());
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        Member savedMember = repository.save(signUpDto.toEntity(encodedPassword, roles));
        return UserResponseDto.fromUser(savedMember);
    }

    @Transactional
    public JwtToken signIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public UserResponseDto read(long userId) {
        Member member = findById(userId);

        return UserResponseDto.fromUser(member);
    }

    @Transactional
    public void update(UserUpdateDto userUpdateDto) {
        Member member = findById(userUpdateDto.userId());

        member.update(userUpdateDto.name(), userUpdateDto.password());
    }

    public long delete(String email) {
        Member member = findByEmail(email);

        repository.delete(member);
        return member.getId();
    }

    public Member findById(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> {
                    log.warn(LogMessage.USER_NOT_FOUND_ID.format(userId));
                    return new EntityNotFoundException("no user entity");
                });
    }

    public Member findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(LogMessage.USER_NOT_FOUND_EMAIL.format(email));
                    return new EntityNotFoundException("no user entity");
                });
    }
}
