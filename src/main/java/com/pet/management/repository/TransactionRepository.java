package com.pet.management.repository;

import com.pet.management.model.ServiceType;
import com.pet.management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByCustomerId(Long customerId);

    List<Transaction> findByClerkId(Long clerkId);

    List<Transaction> findByServiceType(ServiceType serviceType);

    @Query("SELECT t FROM Transaction t WHERE t.customer.customerName LIKE CONCAT('%', :customerName, '%')")
    List<Transaction> findByCustomerNameContaining(@Param("customerName") String customerName);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal findTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.commission) FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate AND t.clerk.id = :clerkId")
    BigDecimal findTotalCommissionByClerkAndDateRange(@Param("clerkId") Long clerkId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.serviceType, COUNT(t), SUM(t.amount) FROM Transaction t GROUP BY t.serviceType")
    List<Object[]> findServiceTypeStatistics();
}
