/* PRODUCT : cacm-api
 * PROJECT : cacm-api
 * PACKAGE : com.kpmg.cacm.api.configuration
 * ************************************************************************************************
 *
 * Copyright(C) 2020 KPMG Technology Service All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF KPMG Technology solution.
 *
 * This copy of the Source Code is intended for KPMG Technology solution's internal use only
 * and is intended for view by persons duly authorized by the management of KPMG Technology solution. No
 * part of this file may be reproduced or distributed in any form or by any
 * means without the written approval of the Management of KPMG Technology solution.
 *
 * *************************************************************************************************
 *
 * REVISIONS:
 * Author : sdhanasena
 * Date : 8/25/2020 - 10:21 AM
 * Since : version 1.0
 * Description :
 * ************* */
package com.kpmg.cacm.api.configuration;

import com.kpmg.cacm.api.ad.AdDto;
import com.kpmg.cacm.api.ad.LdapAdUserExaminer;
import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.security.SpringSecurityUserDetails;
import com.kpmg.cacm.api.security.SpringSecurityUserService;
import com.kpmg.cacm.api.service.PrivilegeMappingService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: sdhanasena
 * Date : 8/25/2020
 */
@Component
public class CustomLdapAuthenticationProvider implements AuthenticationProvider {

    @Value("${ad.domain1}")
    private String AD_DOMAIN;

    @Value("${ad.url}")
    private String AD_URL;

    @Value("${ad.username}")
    private String AD_USERNAME;

    @Value("${ad.password}")
    private String AD_PASSWORD;

    private final UserService userService;

    private final PrivilegeMappingService privilegeMappingService;

    private final SpringSecurityUserService springSecurityUserService;

    private final LdapAdUserExaminer ldapExaminer;

    public CustomLdapAuthenticationProvider(UserService userService, PrivilegeMappingService privilegeMappingService, SpringSecurityUserService springSecurityUserService, LdapAdUserExaminer ldapExaminer) {
        this.userService = userService;
        this.privilegeMappingService = privilegeMappingService;
        this.springSecurityUserService = springSecurityUserService;
        this.ldapExaminer = ldapExaminer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        /**
         * Here implements the LDAP authentication
         * and return authenticated for example
         */
        if (authenticationManager(authentication.getName()).authenticate(authentication).isAuthenticated()) {
            AdDto userData = null;
            User user=null;
            try {
                userData = ldapExaminer.getUserBasicAttributesByUserName(authentication.getName(),
                        ldapExaminer.getLdapContext(AD_USERNAME, AD_PASSWORD));
            } catch (Exception e) {
                throw new UsernameNotFoundException("Username Not Found from AD");
            }

//            if (userData == null) {
//                user = this.userService.findByUsername(authentication.getName());
//            } else if (userData.getEmployeeNumber() != null) {
//                user = this.userService.findByUsername(userData.getEmployeeNumber());
//            }else{
//                user = this.userService.findByUsername(authentication.getName());
//            }

             user = this.userService.findByUsername(authentication.getName());

            if (user == null) {
                throw new UsernameNotFoundException("Username Not Found");
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(),
                    getAuthorities(user));

            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        } else {
            throw new BadCredentialsException("External system authentication failed");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // @Bean
    public AuthenticationManager authenticationManager(String username) {
        return new ProviderManager(Collections.singletonList(activeDirectoryLdapAuthenticationProvider(username)));
    }

    //@Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider(String username) {
        ActiveDirectoryLdapAuthenticationProvider
                provider = new ActiveDirectoryLdapAuthenticationProvider(AD_DOMAIN, AD_URL);
        provider.setSearchFilter("sAMAccountName="+username);
        // provider.setSearchFilter("description="+username);
        provider.setConvertSubErrorCodesToExceptions(true);
        provider.setUseAuthenticationRequestCredentials(true);
        return provider;
    }


    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        final List<GrantedAuthority> authorities = this.privilegeMappingService
                .findAllByUserGroupAndPrivilegeType(user.getUserGroup(), PrivilegeType.API)
                .stream()
                .map(privilegeMapping -> new SimpleGrantedAuthority(privilegeMapping.getPrivilege().getName()))
                .collect(Collectors.toList());
        return authorities;
    }
}