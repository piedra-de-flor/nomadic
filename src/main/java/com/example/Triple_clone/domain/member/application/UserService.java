package com.example.Triple_clone.domain.member.application;

import com.example.Triple_clone.common.auth.JwtToken;
import com.example.Triple_clone.common.auth.JwtTokenProvider;
import com.example.Triple_clone.common.logging.logMessage.MemberLogMessage;
import com.example.Triple_clone.domain.member.domain.Member;
import com.example.Triple_clone.domain.member.infra.MemberRepository;
import com.example.Triple_clone.domain.member.web.dto.UserJoinRequestDto;
import com.example.Triple_clone.domain.member.web.dto.UserResponseDto;
import com.example.Triple_clone.domain.member.web.dto.UserUpdateDto;
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
            log.warn(MemberLogMessage.MEMBER_SIGN_UP_FAIL.format(signUpDto.email()));
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

    public UserResponseDto read(String email) {
        Member member = findByEmail(email);

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
                    log.warn(MemberLogMessage.MEMBER_SEARCH_FAILED_BY_ID.format(userId));
                    return new EntityNotFoundException("no user entity");
                });
    }

    public Member findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(MemberLogMessage.MEMBER_SEARCH_FAILED_BY_EMAIL.format(email));
                    return new EntityNotFoundException("no user entity");
                });
    }
}
