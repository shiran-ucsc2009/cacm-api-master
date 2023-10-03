package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.Privilege;

public interface PrivilegeService {

    Privilege findById(Long id);

    List<Privilege> findAll();

}
