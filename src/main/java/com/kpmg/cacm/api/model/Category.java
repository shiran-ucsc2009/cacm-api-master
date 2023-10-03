package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends AbstractBaseEntity {

    @NotNull
    private String name;

    private String description;
}
