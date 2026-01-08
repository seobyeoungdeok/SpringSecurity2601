package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity //시큐어 어노테이션 활성화
public class SecurityConfig {
    //아래 설정은 SpringBootWebSecurityConfiguration.java  소스를 참고 합니다.
    //Spring Security의 필터 체인을 직접 구성하기
    //이 메서드 안에 설정한 규칙대로 모든 HTTP요청이 필터를 거치며
    //인증(로그인 했는가?)과 인가(권한이 있는가?) 결정됨
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.addFilterBefore(new CustomFilter(), BasicAuthenticationFilter.class);
        http    
                //CSRF비활성화 - 폼로그인(세션기반)에서는 원래 CSRF가 켜져있음.
                .csrf(AbstractHttpConfigurer::disable)
                //URL접근 권한(인가) 규칙 설정
                .authorizeHttpRequests((requests) -> requests
                // user/**경로는 로그인한 사용자만 접근이 가능해
                // ROLE_USER, ROLE_MANAGER, ROLE-ADMIN
                // 인증만 되어 있으면 통과 시켜줄게
                        .requestMatchers("/user/**").authenticated()
                // manager/** 경로는 MANAGER 또는 ADMIN역할만 접근이 가능함.
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                // admin/** 경로는 ADMIN역할만 접근 가능함
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                // 위 3가지 이외의 경우에는 모든 요청에 대해 접근 허용함(로그인 안해도 됨)
                        .requestMatchers("*").permitAll())
                //폼 로그인 기능 활성화
                //기본 로그인 페이지(/login)를 Spring Security가 자동 제공
                // 로그인 처리 URL도 기본적으로 POST/login
                // UsernamePasswordAuthenticationFilter가 이 요청 가로채서 인증 시도해줌
                .formLogin(Customizer.withDefaults())
                // HTTP Basic 인증 활성화
                //Postman에서 Authorization: Basic ... 헤더로 인증 가능
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    // 메모리에 사용자 계정 3개 만들기 설정임
    // DB연결 없이도 로그인/권한 테스트가 가능함
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        //admin계정생성(ROLE_ADMIN)
        UserDetails admin = User.withUsername("admin")
                .password("12345")
                //.authorities("admin") //ROLE_ 접두어가 필요함 그래서 403에러 발생.
                .roles("ADMIN") //ROLE_USER 자동으로 추가됨
                .build();
        UserDetails manager = User.withUsername("manager")
                .password("12345")
                .roles("MANAGER")
                .build();
        UserDetails user = User.withUsername("user")
                .password("12345")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, manager, user);
    }
    //아래 코드가 없으면 user 12345로 로그인 안됨.
    //spring security 5이상에서는 비밀번호를 저장할 때 반드시 인코딩 방식이 명시되어야 함.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
