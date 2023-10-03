package com.kpmg.cacm.api.security;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.service.PrivilegeMappingService;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SpringSecurityUserDetails implements UserDetails {

    private static final long serialVersionUID = 2970590651296665733L;

    private final PrivilegeMappingService privilegeMappingService;

    @Getter
    private final User user;

    public SpringSecurityUserDetails(final PrivilegeMappingService privilegeMappingService, final User user) {
        super();
        this.privilegeMappingService = privilegeMappingService;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = this.privilegeMappingService
            .findAllByUserGroupAndPrivilegeType(this.user.getUserGroup(), PrivilegeType.API)
            .stream()
            .map(privilegeMapping -> new SimpleGrantedAuthority(privilegeMapping.getPrivilege().getName()))
            .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (LocalDate.now().equals(this.user.getExpiryDate()) || LocalDate.now().isAfter(this.user.getExpiryDate())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isEnabled() {
        return this.user.isFirstAttempt();
    }

}
