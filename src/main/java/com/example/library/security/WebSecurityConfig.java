package com.example.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final CustomerDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(CustomerDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests()
                .requestMatchers("/css/**")
                .permitAll()
                .requestMatchers("/users/login", "/users/registerForm", "/users/register", "/", "/users/loginWithError","/users/logoutPage")
                .anonymous()
                .requestMatchers("/users/logout", "/users/redirectTo","/accessDenied").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers("/library/**").hasRole("CUSTOMER")
                .requestMatchers("/book-manager/**").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/users/login")
                .usernameParameter("email")
                .failureForwardUrl("/users/loginWithError")
                .defaultSuccessUrl("/users/redirectTo", true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/users/logoutPage");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}