package com.pet.management.service;

import com.pet.management.model.Transaction;
import com.pet.management.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Page<Transaction> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<Transaction> findByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    public List<Transaction> findByClerkId(Long clerkId) {
        return transactionRepository.findByClerkId(clerkId);
    }

    public List<Transaction> findByServiceTypeContaining(String serviceType) {
        return transactionRepository.findByServiceTypeContaining(serviceType);
    }

    public List<Transaction> findByCustomerNameContaining(String customerName) {
        return transactionRepository.findByCustomerNameContaining(customerName);
    }

    public BigDecimal findTotalAmountByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTotalAmountByDateRange(startDate, endDate);
    }

    public BigDecimal findTotalCommissionByClerkAndDateRange(Long clerkId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTotalCommissionByClerkAndDateRange(clerkId, startDate, endDate);
    }

    public List<Object[]> findServiceTypeStatistics() {
        return transactionRepository.findServiceTypeStatistics();
    }
}
