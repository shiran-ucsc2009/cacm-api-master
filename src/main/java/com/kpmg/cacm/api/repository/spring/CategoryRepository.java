package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findAllByIdAndDeletedFalse(Long id);

    List<Category> findAllByDeletedFalse();

    long countAllByDeletedFalse();
}
