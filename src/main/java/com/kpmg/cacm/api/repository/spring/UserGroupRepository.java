package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    UserGroup findByIdAndDeletedFalse(Long id);

    List<UserGroup> findAllByDeletedFalse();

    long countAllByDeletedFalse();

    UserGroup findByNameAndDeletedFalse(String groupName);
}
