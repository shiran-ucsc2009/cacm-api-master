package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.exceptions.PreConditionFailedException;
import com.kpmg.cacm.api.model.UserGroup;
import com.kpmg.cacm.api.repository.spring.UserGroupRepository;
import com.kpmg.cacm.api.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserGroupServiceImpl(final UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }

    @Override
    public List<UserGroup> findAll() {
        return this.userGroupRepository.findAllByDeletedFalse();
    }

    @Override
    public UserGroup findById(final Long id) {
        return this.userGroupRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public UserGroup findByName(String groupName) {
        return this.userGroupRepository.findByNameAndDeletedFalse(groupName);
    }

    @Override
    public void save(final UserGroup userGroup) {
        if (this.findByName(userGroup.getName()) != null ) {
            throw new PreConditionFailedException("Group name already exists");
        }
        this.userGroupRepository.save(userGroup);
    }

    @Override
    public void update(final UserGroup userGroup) {
        if(!this.findByName(userGroup.getName()).getId().equals(userGroup.getId())) {
            throw new PreConditionFailedException("Group name already exists");
        }
        this.userGroupRepository.save(userGroup);
    }

    @Override
    public void deleteById(final Long id) {
        final UserGroup userGroup = this.findById(id);
        userGroup.setDeleted(true);
        userGroup.setDeletionToken(new Date().getTime());
        this.userGroupRepository.save(userGroup);
    }

    @Override
    public long countAll() {
        return this.userGroupRepository.countAllByDeletedFalse();
    }

}
