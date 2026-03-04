package com.pet.management.service;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.PaymentMethod;
import com.pet.management.model.ServiceType;
import com.pet.management.model.Transaction;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.repository.CustomerRepository;
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
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ClerkRepository clerkRepository;
    
    @Autowired
    private ClerkServiceCommissionService clerkServiceCommissionService;

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

    public List<Transaction> findByServiceType(ServiceType serviceType) {
        return transactionRepository.findByServiceType(serviceType);
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
    
    /**
     * 创建交易，包含支付验证和抽成计算
     */
    public Transaction createTransaction(Transaction transaction) {
        // 验证储值支付
        if (transaction.getPaymentMethod() == PaymentMethod.STORED_VALUE) {
            Customer customer = transaction.getCustomer();
            if (customer == null) {
                throw new IllegalArgumentException("储值支付需要选择顾客");
            }
            BigDecimal storedValueUsed = transaction.getStoredValueUsed();
            if (storedValueUsed == null) {
                storedValueUsed = transaction.getAmount();
                transaction.setStoredValueUsed(storedValueUsed);
            }
            if (customer.getBalance().compareTo(storedValueUsed) < 0) {
                throw new IllegalArgumentException("余额不足");
            }
            // 扣除余额
            customer.setBalance(customer.getBalance().subtract(storedValueUsed));
            customerRepository.save(customer);
        }
        
        // 计算员工抽成
        calculateCommission(transaction);
        
        return save(transaction);
    }
    
    /**
     * 计算员工抽成
     */
    private void calculateCommission(Transaction transaction) {
        Clerk clerk = transaction.getClerk();
        ServiceType serviceType = transaction.getServiceType();
        BigDecimal amount = transaction.getAmount();
        
        if (clerk != null && serviceType != null && amount != null) {
            // 获取抽成比例（优先使用服务类型特定的抽成比例）
            BigDecimal commissionRate = clerkServiceCommissionService.getCommissionRate(clerk.getId(), serviceType);
            BigDecimal commission = amount.multiply(commissionRate);
            transaction.setCommission(commission);
        }
    }
}
