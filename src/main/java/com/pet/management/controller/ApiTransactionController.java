
package com.pet.management.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Transaction;
import com.pet.management.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class ApiTransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id).orElse(null);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        return transactionService.save(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
    }

    @GetMapping("/search")
    public List<Transaction> searchTransactions(@RequestParam String keyword) {
        return transactionService.findByCustomerNameContaining(keyword);
    }

    @GetMapping("/commission")
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
