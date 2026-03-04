package com.pet.management.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pet.management.model.RechargeRecord;

@Repository
public interface RechargeRecordRepository extends JpaRepository<RechargeRecord, Long>, JpaSpecificationExecutor<RechargeRecord> {

    List<RechargeRecord> findByCustomerIdOrderByCreateTimeDesc(Long customerId);

    List<RechargeRecord> findByCreateTimeBetweenOrderByCreateTimeDesc(LocalDateTime startDate, LocalDateTime endDate);
}
