package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (:group IS NULL OR u.userGroup = :group) AND (:category IS NULL OR :category MEMBER OF u.categories) AND u.deleted = false")
    List<User> findAllByUserGroupAndCategoryAndDeletedFalse(@Param("group") UserGroup group, @Param("category") Category category);

    User findByUsernameAndDeletedFalse(@NotNull String username);

    User findByIdAndDeletedFalse(@NotNull Long id);
}
