package com.kpmg.cacm.api.service.impl;

import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.model.Category;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.repository.spring.CategoryRepository;
import com.kpmg.cacm.api.service.CategoryService;
import com.kpmg.cacm.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    @Autowired
    public CategoryServiceImpl(final CategoryRepository categoryRepository, final UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = java.lang.Exception.class)
    public void save(final Category category) {
        this.categoryRepository.save(category);
        final User currentUser = this.userService.getCurrentUser();
        currentUser.getCategories().add(category);
        this.userService.update(null, currentUser);
    }

    @Override
    public Category findById(final Long id) {
        return this.categoryRepository.findAllByIdAndDeletedFalse(id);
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAllByDeletedFalse();
    }

    @Override
    public void deleteById(final Long id) {
        final Category category = this.findById(id);
        category.setDeleted(true);
        category.setDeletionToken(new Date().getTime());
        this.save(category);
    }

    @Override
    public long countAll() {
        return this.categoryRepository.countAllByDeletedFalse();
    }

}
