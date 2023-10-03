package com.kpmg.cacm.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.model.PrivilegeMapping;
import com.kpmg.cacm.api.model.UserGroup;
import com.kpmg.cacm.api.repository.spring.PrivilegeMappingRepository;
import com.kpmg.cacm.api.service.PrivilegeMappingService;
import com.kpmg.cacm.api.service.PrivilegeService;
import com.kpmg.cacm.api.service.UserGroupService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrivilegeMappingServiceImpl implements PrivilegeMappingService {

    private final PrivilegeMappingRepository privilegeMappingRepository;

    private final UserGroupService userGroupService;

    private final UserService userService;

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeMappingServiceImpl(
            final PrivilegeMappingRepository privilegeMappingRepository,
            final UserGroupService userGroupService,
            final UserService userService,
            final PrivilegeService privilegeService) {
        this.privilegeMappingRepository = privilegeMappingRepository;
        this.userGroupService = userGroupService;
        this.userService = userService;
        this.privilegeService = privilegeService;
    }

    @Override
    public List<PrivilegeMapping> findAllByUserGroupAndPrivilegeType(final UserGroup userGroup, final PrivilegeType privilegeType) {
        return this.privilegeMappingRepository.findAllByUserGroupAndPrivilege_PrivilegeTypeAndEnabledTrueAndDeletedFalse(userGroup, privilegeType);
    }

    @Override
    public List<PrivilegeMapping> findAll(final Long userGroupId, final Long privilegeId, final PrivilegeType privilegeType) {
        return this.privilegeMappingRepository.findAll(userGroupId, privilegeId, privilegeType);
    }

    @Override
    public List<PrivilegeMapping> findPrivilegeMappingsForCurrentUser(final PrivilegeType privilegeType) {
        return this.findAllByUserGroupAndPrivilegeType(this.userService.getCurrentUser().getUserGroup(), privilegeType);
    }

    @Override
    public PrivilegeMapping findById(Long id) {
        return this.privilegeMappingRepository.findAllById(id);
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void save(final Long userGroupId, final List<Long> enabledPrivilegeIds) {
        final List<Long> alreadyMappedPrivilegeIds = this.findAll(userGroupId, null, null)
                .stream()
                .map(privilegeMapping -> privilegeMapping.getPrivilege().getId())
                .collect(Collectors.toList());
        this.privilegeService.findAll().forEach(privilege -> {
            final PrivilegeMapping privilegeMapping;
            if(alreadyMappedPrivilegeIds.contains(privilege.getId())) {
                privilegeMapping = this.findAll(userGroupId, privilege.getId(), null).get(0);
            } else {
                privilegeMapping = new PrivilegeMapping();
                privilegeMapping.setUserGroup(this.userGroupService.findById(userGroupId));
                privilegeMapping.setPrivilege(privilege);
            }
            privilegeMapping.setEnabled(enabledPrivilegeIds.contains(privilege.getId()));
            this.privilegeMappingRepository.save(privilegeMapping);
        });
    }
}
