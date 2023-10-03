package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentComment extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    private Incident incident;

    @NotNull
    private String comment;

    @ManyToOne
    @NotNull
    private User user;
}
