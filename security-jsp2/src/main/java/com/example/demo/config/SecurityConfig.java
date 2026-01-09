package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity //시큐어 어노테이션 활성화
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("./login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/custom-login?error=true")
                        .permitAll()
                )
                //로그아웃 설정 추가
                .logout(logout -> logout
                        .logoutUrl("/logout")//로그아웃 처리 URL(디폴트)
                        .logoutSuccessUrl("/") //로그아웃 후 이동할 URL
                        .invalidateHttpSession(true) //세션 무효화(기본값 true)
                        .deleteCookies("JSESSIONID")//쿠키 삭제
                        .permitAll());
        return http.build();
    }
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        //admin계정생성(ROLE_ADMIN)
        UserDetails admin = User.withUsername("admin")
                .password("12345")
                //.authorities("admin") //ROLE_ 접두어가 필요함 그래서 403에러 발생.
                .roles("ADMIN") //ROLE_USER 자동으로 추가됨
                .build();
        //manager계정 생성(ROLE_MANAGER)
        UserDetails manager = User.withUsername("manager")
                .password("12345")
                .roles("MANAGER")
                .build();
        //user계정 생성(ROLE_USER)
        UserDetails user = User.withUsername("user")
                .password("12345")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, manager, user);
    }
    //아래 코드가 없으면 user 12345로 로그인 안됨.
    //spring security 5이상에서는 비밀번호를 저장할 때 반드시 인코딩 방식이 명시되어야 함.
    //Spring Security 5+부터는 비밀번호 저장/검증 에 인코더 가 없으면 에러 발생
    // NoOpPasswordEncorder: 평문 그대로 비교함(암호화 없이 비교)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}