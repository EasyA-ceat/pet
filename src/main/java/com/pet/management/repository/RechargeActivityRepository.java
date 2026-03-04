package com.pet.management.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pet.management.model.RechargeActivity;

@Repository
public interface RechargeActivityRepository extends JpaRepository<RechargeActivity, Long>, JpaSpecificationExecutor<RechargeActivity> {

    List<RechargeActivity> findByIsActiveTrue();

    @Query("SELECT ra FROM RechargeActivity ra WHERE ra.isActive = true " +
           "AND (ra.startDate IS NULL OR ra.startDate <= :now) " +
           "AND (ra.endDate IS NULL OR ra.endDate >= :now) " +
           "AND ra.minAmount <= :amount " +
           "ORDER BY ra.minAmount DESC")
    List<RechargeActivity> findApplicableActivities(@Param("now") LocalDateTime now, @Param("amount") BigDecimal amount);

    Optional<RechargeActivity> findFirstByIsActiveTrueAndMinAmountLessThanEqualOrderByMinAmountDesc(BigDecimal amount);
}
