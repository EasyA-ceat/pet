package com.pet.management.view.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.pet.management.model.Customer;
import com.pet.management.service.CustomerService;
import com.pet.management.service.TransactionService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

@Controller
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    @Lazy
    private MainController mainController;

    @FXML
    private ProgressBar weeklyProgress;

    @FXML
    private Label todayRevenue;

    @FXML
    private Label activeCustomers;

    @FXML
    private Label pendingTasks;

    @FXML
    private Label monthlyTarget;

    @FXML
    private Button quickAddCustomerBtn;

    @FXML
    private Button quickAddTransactionBtn;

    @FXML
    private Button viewDailyReportBtn;

    @FXML
    private Button appointmentManagementBtn;

    @FXML
    private Button viewAllCustomersBtn;

    @FXML
    private Button viewAllTransactionsBtn;

    @FXML
    public void initialize() {
        // 延迟加载数据，等待 FXML 注入完成
        // 数据加载将由外部调用 refreshData() 来完成
    }

    public void loadDashboardData() {
        loadTodayRevenue();
        loadActiveCustomers();
        loadPendingTasks();
        loadMonthlyTarget();
        loadWeeklyProgress();
        setupEventHandlers();
    }

    private void loadTodayRevenue() {
        if (todayRevenue == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        
        BigDecimal total = transactionService.findTotalAmountByDateRange(startOfDay, endOfDay);
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        todayRevenue.setText("¥" + formatAmount(total));
    }

    private void loadActiveCustomers() {
        if (activeCustomers == null) {
            return;
        }
        List<Customer> customers = customerService.findAll();
        activeCustomers.setText(String.valueOf(customers.size()));
    }

    private void loadPendingTasks() {
        if (pendingTasks == null) {
            return;
        }
        pendingTasks.setText("12");
    }

    private void loadMonthlyTarget() {
        if (monthlyTarget == null) {
            return;
        }
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);
        
        BigDecimal monthlyRevenue = transactionService.findTotalAmountByDateRange(startOfMonth, endOfMonth);
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }
        
        monthlyTarget.setText("¥" + formatAmount(monthlyRevenue));
    }

    private void loadWeeklyProgress() {
        if (weeklyProgress == null) {
            return;
        }
        weeklyProgress.setProgress(0.65);
    }

    private void setupEventHandlers() {
        if (quickAddCustomerBtn != null) {
            quickAddCustomerBtn.setOnAction(event -> handleQuickAddCustomer());
        }
        
        if (quickAddTransactionBtn != null) {
            quickAddTransactionBtn.setOnAction(event -> handleQuickAddTransaction());
        }
        
        if (viewDailyReportBtn != null) {
            viewDailyReportBtn.setOnAction(event -> handleViewDailyReport());
        }
        
        if (appointmentManagementBtn != null) {
            appointmentManagementBtn.setOnAction(event -> handleAppointmentManagement());
        }
        
        if (viewAllCustomersBtn != null) {
            viewAllCustomersBtn.setOnAction(event -> handleViewAllCustomers());
        }
        
        if (viewAllTransactionsBtn != null) {
            viewAllTransactionsBtn.setOnAction(event -> handleViewAllTransactions());
        }
    }

    @FXML
    private void handleQuickAddCustomer() {
        if (mainController != null) {
            mainController.handleCustomerManagement();
        }
    }

    @FXML
    private void handleQuickAddTransaction() {
        if (mainController != null) {
            mainController.handleFinanceManagement();
        }
    }

    @FXML
    private void handleViewDailyReport() {
        if (mainController != null) {
            mainController.handleReports();
        }
    }

    @FXML
    private void handleAppointmentManagement() {
        if (mainController != null) {
            mainController.showSuccessDialog("提示", "预约管理功能开发中...");
        }
    }

    @FXML
    private void handleViewAllCustomers() {
        if (mainController != null) {
            mainController.handleCustomerManagement();
        }
    }

    @FXML
    private void handleViewAllTransactions() {
        if (mainController != null) {
            mainController.handleFinanceManagement();
        }
    }

    public void refreshData() {
        loadDashboardData();
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
