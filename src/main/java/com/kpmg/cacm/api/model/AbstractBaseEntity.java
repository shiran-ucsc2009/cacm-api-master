package com.kpmg.cacm.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.security.SpringSecurityUserDetails;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
@Data
public abstract class AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date creationTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp;

    @Column(updatable = false)
    private Long createdBy;

    private Long updatedBy;

    @NotNull
    private boolean deleted;

    @NotNull
    private Long deletionToken = 0L;

    @PrePersist
    protected final void onCreate() {
        this.creationTimestamp = new Date();
        this.createdBy = AbstractBaseEntity.currentUserId();
    }

    @PreUpdate
    protected final void onUpdate() {
        this.updateTimestamp = new Date();
        this.updatedBy = AbstractBaseEntity.currentUserId();
    }

    private static Long currentUserId() {
        Long userId = 0L;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.isAuthenticated()) {
            try {
                userId = ((User) authentication.getPrincipal()).getId();
            }catch (java.lang.ClassCastException e){
                userId = ((SpringSecurityUserDetails) authentication.getPrincipal()).getUser().getId();
            }
        }
        return userId;
    }

}

