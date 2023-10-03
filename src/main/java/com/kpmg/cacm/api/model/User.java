package com.kpmg.cacm.api.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "`user`")
public class User extends AbstractBaseEntity {

    @NotNull
    private String name;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private String empNumber;

    private String email;

    private String telephone;

    @NotNull
    @ManyToOne
    private UserGroup userGroup;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Category> categories;

    private boolean isFirstAttempt;

    private LocalDate expiryDate;

}
