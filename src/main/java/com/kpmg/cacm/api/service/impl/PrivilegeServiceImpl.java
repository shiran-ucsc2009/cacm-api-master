package com.kpmg.cacm.api.service.impl;

import java.util.List;

import com.kpmg.cacm.api.model.Privilege;
import com.kpmg.cacm.api.repository.spring.PrivilegeRepository;
import com.kpmg.cacm.api.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeServiceImpl(final PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public Privilege findById(final Long id) {
        return this.privilegeRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Privilege> findAll() {
        return this.privilegeRepository.findAllByDeletedFalse();
    }

}
