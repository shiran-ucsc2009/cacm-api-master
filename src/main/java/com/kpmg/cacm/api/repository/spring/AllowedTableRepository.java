package com.kpmg.cacm.api.repository.spring;

import java.util.List;

import com.kpmg.cacm.api.model.AllowedTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedTableRepository extends JpaRepository<AllowedTable, Long> {

    List<AllowedTable> findAllByDeletedFalse();

}
