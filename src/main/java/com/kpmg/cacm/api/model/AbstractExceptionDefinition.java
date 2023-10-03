package com.kpmg.cacm.api.model;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.Priority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractExceptionDefinition extends AbstractBaseEntity {

    @NotNull
    private String name;

    private String description;

    @NotNull
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String query;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String subQuery;

    private int resolveDayCount;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User owner;

    @ManyToOne
    private User defaultAssignee;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToOne(cascade = CascadeType.MERGE)
    private ExceptionSchedule exceptionSchedule;

}
