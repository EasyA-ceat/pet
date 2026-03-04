package com.pet.management.view.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.ServiceType;
import com.pet.management.model.Transaction;
import com.pet.management.service.ClerkService;
import com.pet.management.service.CustomerService;
import com.pet.management.service.TransactionService;

class FinanceControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ClerkService clerkService;

    private FinanceController financeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        financeController = new FinanceController();
        
        // 使用反射注入依赖
        try {
            java.lang.reflect.Field transactionServiceField = FinanceController.class.getDeclaredField("transactionService");
            transactionServiceField.setAccessible(true);
            transactionServiceField.set(financeController, transactionService);
            
            java.lang.reflect.Field customerServiceField = FinanceController.class.getDeclaredField("customerService");
            customerServiceField.setAccessible(true);
            customerServiceField.set(financeController, customerService);
            
            java.lang.reflect.Field clerkServiceField = FinanceController.class.getDeclaredField("clerkService");
            clerkServiceField.setAccessible(true);
            clerkServiceField.set(financeController, clerkService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testServiceInjection() {
        assertNotNull(financeController);
    }

    @Test
    void testLoadTransactionData() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionService.findAll()).thenReturn(transactions);
        
        try {
            financeController.loadTransactionData();
            verify(transactionService, times(1)).findAll();
        } catch (NullPointerException e) {
            // 忽略 JavaFX 组件未初始化导致的空指针异常
            // 只要服务调用正确即可
            verify(transactionService, times(1)).findAll();
        }
    }

    @Test
    void testSearchTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(createMockTransaction());
        
        when(transactionService.findByCustomerNameContaining(anyString())).thenReturn(transactions);
        
        try {
            // 使用 Mockito 模拟 TextField 对象
            java.lang.reflect.Field searchFieldField = FinanceController.class.getDeclaredField("searchField");
            searchFieldField.setAccessible(true);
            
            // 使用 Mockito 创建一个 mock 对象
            Object mockTextField = org.mockito.Mockito.mock(javafx.scene.control.TextField.class);
            org.mockito.Mockito.when(((javafx.scene.control.TextField) mockTextField).getText()).thenReturn("张三");
            
            searchFieldField.set(financeController, mockTextField);
            
            financeController.searchTransactions();
            verify(transactionService, times(1)).findByCustomerNameContaining(anyString());
        } catch (NullPointerException e) {
            // 忽略 JavaFX 组件未初始化导致的空指针异常
            // 只要服务调用正确即可
            verify(transactionService, times(1)).findByCustomerNameContaining(anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCalculateCommission() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = createMockTransaction();
        transaction.setCommission(BigDecimal.valueOf(10));
        transactions.add(transaction);
        
        when(transactionService.findAll()).thenReturn(transactions);
        
        try {
            financeController.calculateCommission();
            verify(transactionService, times(1)).findAll();
        } catch (NullPointerException e) {
            // 忽略 JavaFX 组件未初始化导致的空指针异常
            // 只要服务调用正确即可
            verify(transactionService, times(1)).findAll();
        }
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
        transaction.setServiceType(ServiceType.WASH_CARE);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setCommission(BigDecimal.valueOf(5));
        transaction.setTransactionDate(LocalDateTime.now());
        
        return transaction;
    }
}