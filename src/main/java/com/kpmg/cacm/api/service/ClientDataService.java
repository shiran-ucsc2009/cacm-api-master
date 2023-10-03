package com.kpmg.cacm.api.service;

import java.util.List;
import java.util.Map;

public interface ClientDataService {

    List<Map<String, Object>> executeQuery(String sql);

    List<Map<String, Object>> executeQuery(String sql, Map<String, Object> subQueryParams);

    List<Map<String, Object>> executeClientQuery(String sql);

    List<Map<String, Object>> executeClientQuery(String sql, Map<String, Object> subQueryParams);

}
