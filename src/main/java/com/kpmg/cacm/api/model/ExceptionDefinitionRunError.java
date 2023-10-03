package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionDefinitionRunError extends AbstractBaseEntity {

    @ManyToOne
    @NotNull
    private ExceptionDefinitionRun exceptionDefinitionRun;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String stackTrace;

    private String traceId;

}
