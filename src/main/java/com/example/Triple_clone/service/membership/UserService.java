package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.Member;
import com.example.Triple_clone.dto.auth.JwtToken;
import com.example.Triple_clone.dto.membership.UserJoinRequestDto;
import com.example.Triple_clone.dto.membership.UserResponseDto;
import com.example.Triple_clone.dto.membership.UserUpdateDto;
import com.example.Triple_clone.repository.MemberRepository;
import com.example.Triple_clone.service.support.JwtTokenProvider;
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
import java.util.NoSuchElementException;

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
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
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
        Member member = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        return UserResponseDto.fromUser(member);
    }

    @Transactional
    public void update(UserUpdateDto userUpdateDto) {
        Member member = repository.findById(userUpdateDto.userId())
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        member.update(userUpdateDto.name(), userUpdateDto.password());
    }

    public long delete(long userId) {
        Member member = repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user entity"));

        repository.delete(member);
        return userId;
    }

    public Member findById(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("no user Entity"));
    }
}
