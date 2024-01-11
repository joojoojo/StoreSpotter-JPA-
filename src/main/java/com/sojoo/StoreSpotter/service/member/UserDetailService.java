package com.sojoo.StoreSpotter.service.member;

import com.sojoo.StoreSpotter.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 사용자 이메일로 사용자 정보 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userDetailService : " + email);
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }

}