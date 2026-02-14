package com.pet.management.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.Transaction;
import com.pet.management.repository.TransactionRepository;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService();
        // 使用反射注入mock
        try {
            java.lang.reflect.Field field = TransactionService.class.getDeclaredField("transactionRepository");
            field.setAccessible(true);
            field.set(transactionService, transactionRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFindAll() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionRepository.findAll()).thenReturn(transactions);
        
        List<Transaction> result = transactionService.findAll();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Transaction transaction = createMockTransaction();
        
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        Transaction saved = transactionService.save(transaction);
        
        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDeleteById() {
        doNothing().when(transactionRepository).deleteById(anyLong());
        
        transactionService.deleteById(1L);
        
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        Transaction transaction = createMockTransaction();
        
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        
        Optional<Transaction> result = transactionService.findById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByCustomerId() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionRepository.findByCustomerId(anyLong())).thenReturn(transactions);
        
        List<Transaction> result = transactionService.findByCustomerId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    void testFindByClerkId() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionRepository.findByClerkId(anyLong())).thenReturn(transactions);
        
        List<Transaction> result = transactionService.findByClerkId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByClerkId(1L);
    }

    @Test
    void testFindByServiceTypeContaining() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionRepository.findByServiceTypeContaining(anyString())).thenReturn(transactions);
        
        List<Transaction> result = transactionService.findByServiceTypeContaining("洗澡");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByServiceTypeContaining("洗澡");
    }

    @Test
    void testFindByCustomerNameContaining() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionRepository.findByCustomerNameContaining(anyString())).thenReturn(transactions);
        
        List<Transaction> result = transactionService.findByCustomerNameContaining("张三");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository, times(1)).findByCustomerNameContaining("张三");
    }

    @Test
    void testFindTotalAmountByDateRange() {
        BigDecimal total = BigDecimal.valueOf(1000);
        
        when(transactionRepository.findTotalAmountByDateRange(any(), any())).thenReturn(total);
        
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        BigDecimal result = transactionService.findTotalAmountByDateRange(start, end);
        
        assertNotNull(result);
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result));
        verify(transactionRepository, times(1)).findTotalAmountByDateRange(start, end);
    }

    @Test
    void testFindTotalCommissionByClerkAndDateRange() {
        BigDecimal commission = BigDecimal.valueOf(100);
        
        when(transactionRepository.findTotalCommissionByClerkAndDateRange(anyLong(), any(), any())).thenReturn(commission);
        
        LocalDateTime start = LocalDateTime.now().minusDays(7);
        LocalDateTime end = LocalDateTime.now();
        BigDecimal result = transactionService.findTotalCommissionByClerkAndDateRange(1L, start, end);
        
        assertNotNull(result);
        assertEquals(0, BigDecimal.valueOf(100).compareTo(result));
        verify(transactionRepository, times(1)).findTotalCommissionByClerkAndDateRange(1L, start, end);
    }

    private Transaction createMockTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("张三");
        customer.setPetName("旺财");
        
        Clerk clerk = new Clerk();
        clerk.setId(1L);
        clerk.setClerkName("李四");
        clerk.setCommissionRate(BigDecimal.valueOf(0.05));
        
        transaction.setCustomer(customer);
        transaction.setClerk(clerk);
        transaction.setServiceType("宠物洗澡");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCommission(BigDecimal.valueOf(5));
        transaction.setTransactionDate(LocalDateTime.now());
        
        return transaction;
    }
}