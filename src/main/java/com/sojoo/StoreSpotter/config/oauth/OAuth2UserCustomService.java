//package com.sojoo.StoreSpotter.config.oauth;
//
//import com.sojoo.StoreSpotter.entity.Member.Member;
//import com.sojoo.StoreSpotter.repository.member.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Service
//public class OAuth2UserCustomService extends DefaultOAuth2UserService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        // 요청을 바탕으로 유저 정보를 담은 객체 반환
//        OAuth2User user = super.loadUser(userRequest);
//        saveOrUpdate(user);
//        return user;
//    }
//
//    private Member saveOrUpdate(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
//        Member member = memberRepository.findByMemberEmail(email)
//                .map(entity -> entity.update(name))
//                .orElse(Member.builder()
//                        .memberEmail(email)
//                        .nickname(name)
//                        .build());
//        return memberRepository.save(member);
//    }
//}
