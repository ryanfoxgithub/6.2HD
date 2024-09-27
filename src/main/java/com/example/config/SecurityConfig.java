package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/guest/**").hasAnyRole("ADMIN", "USER", "GUEST")
                .requestMatchers("/h2-console/**", "/aggregate", "/CSRF", "/home", "/sql", "/login", "/error", "/frontend", 
                		"/fetchFile", "/result", "/").permitAll() // Allow access to 'home' page without login
                .anyRequest().authenticated()  // Other requests need authentication
            )
            .formLogin(form -> form
                .loginPage("/login")  // Specify the login page URL
                .defaultSuccessUrl("/loginhome", true)  // Redirect to 'loginhome' after login
                .permitAll()
            )
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/h2-console/**", "/upload")  // Exclude certain endpoints from CSRF protection
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())  // Allow H2 console to be displayed in iframes
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("adminPass"))
                .roles("ADMIN").build());
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("userPass"))
                .roles("USER").build());
        manager.createUser(User.withUsername("guest")
                .password(passwordEncoder().encode("guestPass"))
                .roles("GUEST").build());
        return manager;
    }
}
