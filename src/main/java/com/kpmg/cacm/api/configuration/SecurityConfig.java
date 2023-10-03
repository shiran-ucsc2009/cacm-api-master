package com.kpmg.cacm.api.configuration;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.cacm.api.dto.response.ApiErrorResponse;
import com.kpmg.cacm.api.dto.response.LoginResponse;
import com.kpmg.cacm.api.dto.response.LogoutResponse;
import com.kpmg.cacm.api.dto.response.model.ApiErrorDTO;
import com.kpmg.cacm.api.security.SpringSecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HTTP_RESPONSE_CONTENT_TYPE = "application/json";

    private final SpringSecurityUserService springSecurityUserService;

    private final ObjectMapper objectMapper;

    private final PasswordEncoder passwordEncoder;

    private final CustomLdapAuthenticationProvider customLdapAuthenticationProvider;

    @Value("${cacm.config.frontend.server}")
    private String frontendServer;

    private final CustomAuthenticationHandler customAuthenticationHandler;

    @Value("${ad.intergration}")
    private String IS_AD_TRUE;

    @Autowired
    public SecurityConfig(
            final SpringSecurityUserService springSecurityUserService,
            final ObjectMapper objectMapper,
            final PasswordEncoder passwordEncoder, CustomAuthenticationHandler customAuthenticationHandler
             ,CustomLdapAuthenticationProvider customLdapAuthenticationProvider) {
        super();
        this.springSecurityUserService = springSecurityUserService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.customLdapAuthenticationProvider = customLdapAuthenticationProvider;
        this.customAuthenticationHandler = customAuthenticationHandler;
    }

    @Autowired
    public final void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        if (IS_AD_TRUE.equals("True")) {
            try {
                auth.authenticationProvider(this.customLdapAuthenticationProvider);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        } else {
            auth.userDetailsService(this.springSecurityUserService).passwordEncoder(this.passwordEncoder);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(this.frontendServer));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Collections.singletonList("x-auth-token"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .authorizeRequests()
                               .antMatchers(
                        "/getUser",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                           "/**/user/reset-password-first-attempt"
                                           ).permitAll()
                .mvcMatchers(
                        "/css/**",
                        "/images/**",
                        "/fonts/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .successHandler(this.successHandler())
                .failureHandler(this.customAuthenticationHandler)
                .and()
            .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
            .exceptionHandling()
                .accessDeniedHandler(this.accessDeniedHandler())
                .authenticationEntryPoint(this.authenticationEntryPoint());


    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            response.getWriter().write(
                this.objectMapper.writeValueAsString(
                    LoginResponse.builder().build()
                )
            );
            response.setContentType(SecurityConfig.HTTP_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpStatus.OK.value());
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.getWriter().write(
                this.objectMapper.writeValueAsString(
                    LogoutResponse.builder().build()
                )
            );
            response.setContentType(SecurityConfig.HTTP_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpStatus.OK.value());
        };
    }

    private AuthenticationFailureHandler failureHandler() {

        return (request, response, exception) -> {
            System.out.println(exception);
            response.getWriter().write(
                this.objectMapper.writeValueAsString(
                    ApiErrorResponse.builder()
                        .errors(Collections.singletonList(ApiErrorDTO.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("Authentication Failed")
                            .build()
                        )
                    ).build()
                )
            );
            response.setContentType(SecurityConfig.HTTP_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            response.getWriter().write(
                this.objectMapper.writeValueAsString(
                    ApiErrorResponse.builder().errors(Collections.singletonList(
                        ApiErrorDTO.builder()
                            .code(HttpStatus.FORBIDDEN.value())
                            .message("Access Denied")
                            .build()
                        )
                    ).build()
                )
            );
            response.setContentType(SecurityConfig.HTTP_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            response.getWriter().write(
                this.objectMapper.writeValueAsString(
                    ApiErrorResponse.builder()
                        .errors(Collections.singletonList(ApiErrorDTO.builder()
                            .code(HttpStatus.UNAUTHORIZED.value())
                            .message("Not Authenticated")
                            .build()
                        )
                    ).build()
                )
            );
            response.setContentType(SecurityConfig.HTTP_RESPONSE_CONTENT_TYPE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        };
    }
}
