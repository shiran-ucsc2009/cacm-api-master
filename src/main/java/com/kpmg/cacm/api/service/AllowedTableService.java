package com.kpmg.cacm.api.service;

import java.util.List;

import com.kpmg.cacm.api.model.AllowedTable;

public interface AllowedTableService {

    List<AllowedTable> findAll();

    String encrypt(String name);

}

