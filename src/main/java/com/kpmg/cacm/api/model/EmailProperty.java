package com.kpmg.cacm.api.model;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailProperty extends AbstractBaseEntity {

    private String name;

    private String senderIp;

    private String recipients;

}
