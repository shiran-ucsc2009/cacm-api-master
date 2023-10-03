package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.model.PrivilegeMapping;
import com.kpmg.cacm.api.model.UserGroup;

public interface PrivilegeMappingService {

    List<PrivilegeMapping> findAllByUserGroupAndPrivilegeType(UserGroup userGroup, PrivilegeType privilegeType);

    List<PrivilegeMapping> findAll(Long userGroupId, Long privilegeId, PrivilegeType privilegeType);

    PrivilegeMapping findById(Long id);

    void save(Long userGroupId, List<Long> enabledPrivilegeIds);

    List<PrivilegeMapping> findPrivilegeMappingsForCurrentUser(PrivilegeType privilegeType);
}
