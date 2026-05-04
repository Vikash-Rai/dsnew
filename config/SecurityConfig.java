package com.equabli.collectprism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http,CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.authorizeRequests(authorizeRequests ->
        authorizeRequests
            .requestMatchers("/**").permitAll()  // Allow access to public paths
            .anyRequest().authenticated()  // Require authentication for all other requests
    ).httpBasic(Customizer.withDefaults()).authorizeRequests().and().csrf().disable().cors().configurationSource(corsConfigurationSource);

        return http.build();
    }
	
}
