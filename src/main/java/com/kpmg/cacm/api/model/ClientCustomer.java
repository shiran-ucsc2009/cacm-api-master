package com.kpmg.cacm.api.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.kpmg.cacm.api.dto.constant.Priority;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientCustomer extends AbstractBaseEntity {

    private String customerId;

    private String customerName;

    private boolean customerFlag;

    @Enumerated(EnumType.STRING)
    private Priority priority;

}
