package com.kpmg.cacm.api.security;

import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.service.PrivilegeMappingService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpringSecurityUserService implements UserDetailsService {

    private final UserService userService;

    private final PrivilegeMappingService privilegeMappingService;

    @Autowired
    public SpringSecurityUserService(
            final UserService userService,
            final PrivilegeMappingService privilegeMappingService) {
        this.userService = userService;
        this.privilegeMappingService = privilegeMappingService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        return new SpringSecurityUserDetails(this.privilegeMappingService, user);
    }
}
