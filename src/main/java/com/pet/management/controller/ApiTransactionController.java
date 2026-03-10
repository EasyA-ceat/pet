
package com.pet.management.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.PaymentMethod;
import com.pet.management.model.ServiceType;
import com.pet.management.model.Transaction;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.repository.CustomerRepository;
import com.pet.management.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class ApiTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(ApiTransactionController.class);

    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ClerkRepository clerkRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public List<Transaction> getAllTransactions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("获取交易列表 - 用户: {}, 权限: {}", auth.getName(), auth.getAuthorities());
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TRANSACTION_WRITE')")
    public Transaction createTransaction(@RequestBody Map<String, Object> payload) {
        Transaction transaction = new Transaction();
        
        // 设置基本字段
        if (payload.containsKey("serviceType")) {
            String serviceTypeStr = (String) payload.get("serviceType");
            transaction.setServiceType(ServiceType.valueOf(serviceTypeStr));
        }
        if (payload.containsKey("paymentMethod")) {
            String paymentMethodStr = (String) payload.get("paymentMethod");
            transaction.setPaymentMethod(PaymentMethod.valueOf(paymentMethodStr));
        }
        if (payload.containsKey("storedValueUsed")) {
            Object storedValueObj = payload.get("storedValueUsed");
            if (storedValueObj instanceof Number) {
                transaction.setStoredValueUsed(BigDecimal.valueOf(((Number) storedValueObj).doubleValue()));
            }
        }
        if (payload.containsKey("amount")) {
            Object amountObj = payload.get("amount");
            if (amountObj instanceof Number) {
                transaction.setAmount(BigDecimal.valueOf(((Number) amountObj).doubleValue()));
            }
        }
        if (payload.containsKey("commission")) {
            Object commissionObj = payload.get("commission");
            if (commissionObj instanceof Number) {
                transaction.setCommission(BigDecimal.valueOf(((Number) commissionObj).doubleValue()));
            }
        }
        if (payload.containsKey("notes")) {
            transaction.setNotes((String) payload.get("notes"));
        }
        
        // 设置关联对象
        if (payload.containsKey("customerId")) {
            Object customerIdObj = payload.get("customerId");
            if (customerIdObj != null) {
                Long customerId = Long.valueOf(customerIdObj.toString());
                Customer customer = customerRepository.findById(customerId).orElse(null);
                transaction.setCustomer(customer);
            }
        }
        
        if (payload.containsKey("clerkId")) {
            Object clerkIdObj = payload.get("clerkId");
            if (clerkIdObj != null) {
                Long clerkId = Long.valueOf(clerkIdObj.toString());
                Clerk clerk = clerkRepository.findById(clerkId).orElse(null);
                transaction.setClerk(clerk);
            }
        }
        
        return transactionService.save(transaction);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_WRITE')")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        return transactionService.save(transaction);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRANSACTION_WRITE')")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public List<Transaction> searchTransactions(@RequestParam String keyword) {
        return transactionService.findByCustomerNameContaining(keyword);
    }

    @GetMapping("/commission")
    @PreAuthorize("hasAuthority('TRANSACTION_READ')")
    public BigDecimal calculateTotalCommission() {
        BigDecimal totalCommission = BigDecimal.ZERO;
        List<Transaction> transactions = transactionService.findAll();
        
        for (Transaction transaction : transactions) {
            if (transaction.getCommission() != null) {
                totalCommission = totalCommission.add(transaction.getCommission());
            }
        }
        
        return totalCommission;
    }
}
