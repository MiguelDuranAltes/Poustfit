package com.hackudc.poustfit_server.security.config;

import com.hackudc.poustfit_server.security.exception_handler.MyAccessDeniedHandler;
import com.hackudc.poustfit_server.security.exception_handler.MyUnauthorizedEntryPointHandler;
import com.hackudc.poustfit_server.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final MyUnauthorizedEntryPointHandler myUnauthorizedEntryPointHandler;

    private final MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, MyUnauthorizedEntryPointHandler myUnauthorizedEntryPointHandler, MyAccessDeniedHandler myAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.myUnauthorizedEntryPointHandler = myUnauthorizedEntryPointHandler;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf( csrfConfigurer -> csrfConfigurer.disable())
                .sessionManagement( sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/my-account/sign-out").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tags/*").permitAll()
                        .requestMatchers("/**").authenticated())
                .exceptionHandling(
                        exceptionHandlingConfigurer -> {
                            exceptionHandlingConfigurer.authenticationEntryPoint(myUnauthorizedEntryPointHandler);
                            exceptionHandlingConfigurer.accessDeniedHandler(myAccessDeniedHandler);
                        }
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "ws://localhost:5173"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        // Para que funcione en todos los controladores hacemos un generico
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
