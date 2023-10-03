package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.Category;

public interface CategoryService {

    void save(Category category);

    Category findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);

    long countAll();

}
