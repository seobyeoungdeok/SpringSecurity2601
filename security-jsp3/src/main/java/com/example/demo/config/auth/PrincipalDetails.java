package com.example.demo.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/*
시큐리티가 /login주소요청이 오면 낚아채서 로그인을 진행함
로그인 진행이 완료되면 시큐리티가 가진 session을 만들어줌
같은 세션 공간인데 시큐리티만의 공간이 있음.(Security ContextHolder에 키를 저장)
object -> Authentication타입 객체만 들어갈 수 있다.
Authentication안에 User정보가 있어야 함
User타입 -> UserDetails타입 객체만 들어갈 수 있다.
Security Session영역에 들어갈 수 있는 객체는 Authentication만 들어갈 수 있음.
Authentication안에서 UserDetails를 꺼낼 수 있다.(사용자 정보-세션에 있던 정보)
Authentication객체는 PrincipalDetailsService에서 만들어줌
 */
public class PrincipalDetails implements UserDetails {
    //우리 서비스 사용자 엔티티
    //DB에서 조회한 사용자 정보
    private com.example.demo.model.User user;
    //생성자 : 로그인시 DB에서 조회된 User받아서 PrincipalDetails로 감싼다.
    public PrincipalDetails(com.example.demo.model.User user) {
        this.user = user;
    }
    // 점검해야 할 포인트
    // DB설계시 role문자열 형식
    // Spring Security에서 .hasRole("ADMIN"), .hasRole("USER")은 내부적으로
    // "ROLE_ADMIN", "ROLE_USER"
    // 권장: DB에 "ROLE_ADMIN"형태로 저장하거나, getAuthority()에서 "ROLE_"를 붙여서 반환
    // return "ROLE_"+user.getRole()
    //시큐리티가 이 사용자가 어떤 권한을 가졌는지 알아야 한다.
    //로그인 성공시 토큰안에 권한 목록을 채울 때 사용됨
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();//ROLE_ADMIN, ROLE_USER, ROLE_MANAGER
            }
        });
        return collect;
    }//end of getAuthorites

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    public String getEmail(){
        return user.getEmail();
    }
    // 계정 관련 상태 체크용 메서드
    // true: 만료 아님(로그인 가능)
    // false: 계정 만료(로그인 불가)
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
    // true:잠김 아님(로그인 가능)
    // false : 잠김(로그인 불가) - 로그인 실패 5회
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    // true:  비번 만료 아님
    // false : 비번 만료 (로그인 불가) - 90일 마다 변경 정책
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    // 계정 활성화  여부(사용 가능 여부)
    // true: 사용 가능(로그인 가능)
    // false : 비활성화(로그인 불가) - 휴면계정, 탈퇴, 이메일 미인증
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
