package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.dto.constant.PrivilegeType;
import com.kpmg.cacm.api.model.PrivilegeMapping;
import com.kpmg.cacm.api.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeMappingRepository extends JpaRepository<PrivilegeMapping, Long> {

    @Query("SELECT pm FROM PrivilegeMapping pm WHERE (:userGroupId IS NULL OR pm.userGroup.id = :userGroupId) AND (:privilegeId IS NULL OR pm.privilege.id = :privilegeId) AND (:privilegeType IS NULL OR pm.privilege.privilegeType = :privilegeType) AND pm.deleted=FALSE")
    List<PrivilegeMapping> findAll(@Param("userGroupId") Long userGroupId, @Param("privilegeId") Long privilegeId, @Param("privilegeType") PrivilegeType privilegeType);

    List<PrivilegeMapping> findAllByUserGroupAndPrivilege_PrivilegeTypeAndEnabledTrueAndDeletedFalse(@Param("userGroupId") UserGroup userGroup, @Param("privilegeType") PrivilegeType privilegeType);

    PrivilegeMapping findAllById(@Param("id") Long id);
}
