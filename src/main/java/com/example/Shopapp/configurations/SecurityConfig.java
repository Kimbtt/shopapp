package com.example.Shopapp.configurations;

import com.example.Shopapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    //User's detail object
    @Bean
    public UserDetailsService userDetailsService(){
        return (phoneNumber) -> userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(()->
                        new UsernameNotFoundException("Cannot find user with phone number: " + phoneNumber));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authPrivider = new DaoAuthenticationProvider();
        authPrivider.setUserDetailsService(userDetailsService());
        authPrivider.setPasswordEncoder(passwordEncoder());
        return authPrivider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
