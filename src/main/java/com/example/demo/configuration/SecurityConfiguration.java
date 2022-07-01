package com.example.demo.configuration;

import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration{

    private  final MemberService memberService;

    @Bean
    UserAuthenticationFailureHandler getFailureHandler(){
        return new UserAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(memberService)
                .passwordEncoder(getPasswordEncoder());

        AuthenticationManager authenticationManager
                = authenticationManagerBuilder. build();

        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers("/", "/member/register", "/member/register-complete"
                        , "/member/email-auth", "/member/find_password", "/member/find_password_result"
                        ,"/member/reset_password").permitAll()
                .mvcMatchers("/admin/*").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/member/login")
                .failureHandler(getFailureHandler())
                .permitAll();
        http.authenticationManager(authenticationManager);
        http.exceptionHandling()
                .accessDeniedPage("/error/denied");
        http.logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
        http.httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
