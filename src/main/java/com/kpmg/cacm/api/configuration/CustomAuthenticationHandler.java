package com.kpmg.cacm.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpmg.cacm.api.dto.response.ApiErrorResponse;
import com.kpmg.cacm.api.dto.response.model.ApiErrorDTO;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class CustomAuthenticationHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {


    private ObjectMapper objectMapper;

    private static final String HTTP_RESPONSE_CONTENT_TYPE = "application/json";

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @Autowired
    public CustomAuthenticationHandler(ObjectMapper objectMapper, PasswordEncoder passwordEncoder, UserService userService) {
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println(request);
        if (exception.getClass().equals(CredentialsExpiredException.class)) {
            response.setStatus(HttpStatus.LOCKED.value());
            response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(ApiErrorResponse.builder()
                            .errors(Collections.singletonList(ApiErrorDTO.builder()
                                            .code(HttpStatus.LOCKED.value())
                                            .message("Password is expired. Please reset your Password ")
                                            .build()
                                                             )
                                   ).build()));

        } else if (exception.getClass().equals(BadCredentialsException.class)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
            response.getOutputStream()
                    .println(objectMapper.writeValueAsString(ApiErrorResponse.builder()
                            .errors(Collections.singletonList(ApiErrorDTO.builder()
                                            .code(HttpStatus.UNAUTHORIZED.value())
                                            .message("Authentication Failed")
                                            .build()
                                                             )
                                   ).build()));
        } else if (exception.getClass().equals(DisabledException.class)) {
            final User user = this.userService.findByUsername(request.getParameter("username"));
            if (this.passwordEncoder.matches(request.getParameter("password"), user.getPassword())) {
                response.setStatus(HttpStatus.LOCKED.value());
                response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(ApiErrorResponse.builder()
                                .errors(Collections.singletonList(ApiErrorDTO.builder()
                                                .code(HttpStatus.LOCKED.value())
                                                .message("Reset your Password !!")
                                                .build()
                                                                 )
                                       ).build()));
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(CustomAuthenticationHandler.HTTP_RESPONSE_CONTENT_TYPE);
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(ApiErrorResponse.builder()
                                .errors(Collections.singletonList(ApiErrorDTO.builder()
                                                .code(HttpStatus.UNAUTHORIZED.value())
                                                .message("Authentication Failed")
                                                .build()
                                                                 )
                                       ).build()));
            }


        } else {
            System.out.println("3");
            setDefaultFailureUrl("/re");
        }
    }

}
