package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.UserGroup;

public interface UserGroupService {

    List<UserGroup> findAll();

    UserGroup findById(Long id);

    UserGroup findByName(String groupName);

    void save(UserGroup userGroup);

    void update(UserGroup userGroup);

    void deleteById(Long id);

    long countAll();
}
