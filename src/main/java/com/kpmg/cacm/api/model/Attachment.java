package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.dto.constant.AttachmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Attachment extends AbstractBaseEntity {

    @NotNull
    private String name;

    @NotNull
    private AttachmentType attachmentType;

    @NotNull
    private Long entityId;

}
