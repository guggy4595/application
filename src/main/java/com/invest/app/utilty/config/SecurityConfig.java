package com.invest.app.utilty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.invest.app.utilty.exception.CustomAuthFailureHandler;
import com.invest.app.InvestService.account.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    AccountService accountService;

    @Autowired
    CustomAuthFailureHandler customFailureHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // 인증된 요청
            .authorizeHttpRequests()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/account/info").hasRole("USER")
            .antMatchers("/board/write") .hasRole("USER")
            // 모든 접근 허용
            .antMatchers("/","/css/**","/scripts/**","/plugin/**","/fonts/**").permitAll()
            .and()
            // 로그인 페이지 보안 설정
            .formLogin()
            // 로그인 페이지
            .loginPage("/account/login")
            // 로그인 처리 URL
            .loginProcessingUrl("/account/gologin")
            // 로그인 성공 시 이동 페이지
            .defaultSuccessUrl("/")// 로그인 성공시 이동할 URL
            .failureHandler(customFailureHandler) // 로그인 실패 핸들러
            // 로그인 시 입력받는 필드
            .usernameParameter("acid")
            .passwordParameter("acpw")
            .and()
            // 로그아웃 처리
            .logout()
            // 로그아웃 처리 URL
            .logoutRequestMatcher( new AntPathRequestMatcher("/account/logout"))
            // 로그아웃 이후 이동할 페이지
            .logoutSuccessUrl("/")
            // 세션 초기화
            .invalidateHttpSession( true )
            .and()
            // csrf : 서버에게 요청할수 있는 페이지 제한
            .csrf()
            // 로그인
            .ignoringAntMatchers("/account/gologin")
            // 회원가입
            .ignoringAntMatchers("/account/goSignup")
            // 중복확인
            .ignoringAntMatchers("/account/searchId")
            // 휴먼여부
            .ignoringAntMatchers("/account/idUserYn")
            // 글작성
            .ignoringAntMatchers("/board/save")
            .ignoringAntMatchers("/board/boardList")
            .ignoringAntMatchers("/board/getboard")
            .ignoringAntMatchers("/board/goUpdate")
            .ignoringAntMatchers("/board/delete")
            .and()
            // 오류페이지 전환
            .exceptionHandling()
            .accessDeniedPage("/error");
        return http.build();
    }
}
