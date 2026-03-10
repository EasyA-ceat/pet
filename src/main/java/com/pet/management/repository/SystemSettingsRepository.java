package com.pet.management.repository;

import com.pet.management.model.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long>, JpaSpecificationExecutor<SystemSettings> {

    // 获取系统设置（只有一条记录）
    Optional<SystemSettings> findFirstByOrderByIdAsc();
}
