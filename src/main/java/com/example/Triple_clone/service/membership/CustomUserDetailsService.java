package com.example.Triple_clone.service.membership;

import com.example.Triple_clone.domain.entity.User;
import com.example.Triple_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override // UserDetailsService 클래스의 loadUserByUsername 오버라이딩
    @Transactional
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findOneWithAuthoritiesByEmail(email)// 로그인 시 DB 유저정보와 권한정보를 가져옴
                .map(user -> createUser(email, user)) // 데이터베이스에서 가져온 정보를 기준으로 createUser 메서드 수행
                .orElseThrow(()-> new UsernameNotFoundException(email + "-> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String email,
                                                                          User user) {
        // DB 에서 가져온 유저가 활성화 상태가 아니라면
        if (!user.isEnabled()) {
            throw new RuntimeException(email + "-> 활성화되어 있지 않습니다.");
        }
        // 해당 유저가 활성화 상태라면
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream() // getAuthorities() : 유저의 권한정보
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())) //
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),  // 유저명
                user.getPassword(),  // 비밀번호를 가진
                grantedAuthorities); // 유저 객체를 리턴
    }


}