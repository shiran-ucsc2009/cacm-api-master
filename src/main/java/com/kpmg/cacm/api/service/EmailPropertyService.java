package com.kpmg.cacm.api.service;

import com.kpmg.cacm.api.model.EmailProperty;

public interface EmailPropertyService {

    EmailProperty findByName(String name);
}
