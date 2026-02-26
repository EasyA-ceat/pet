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
    private Label weeklyProgressLabel;

    @FXML
    private Label todayRevenueTrend;

    @FXML
    private Label activeCustomersTrend;

    @FXML
    private Label monthlyTargetProgress;

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
        System.out.println("DashboardController initialized");
        System.out.println("todayRevenue: " + todayRevenue);
        System.out.println("activeCustomers: " + activeCustomers);
        System.out.println("pendingTasks: " + pendingTasks);
        System.out.println("monthlyTarget: " + monthlyTarget);
        System.out.println("weeklyProgress: " + weeklyProgress);
        System.out.println("weeklyProgressLabel: " + weeklyProgressLabel);
        System.out.println("todayRevenueTrend: " + todayRevenueTrend);
        System.out.println("activeCustomersTrend: " + activeCustomersTrend);
        System.out.println("monthlyTargetProgress: " + monthlyTargetProgress);
        System.out.println("quickAddCustomerBtn: " + quickAddCustomerBtn);
        System.out.println("quickAddTransactionBtn: " + quickAddTransactionBtn);
        System.out.println("viewDailyReportBtn: " + viewDailyReportBtn);
        System.out.println("appointmentManagementBtn: " + appointmentManagementBtn);
        System.out.println("mainController: " + mainController);
    }

    public void loadDashboardData() {
        System.out.println("开始加载仪表盘数据");
        System.out.println("todayRevenue: " + todayRevenue);
        System.out.println("activeCustomers: " + activeCustomers);
        System.out.println("pendingTasks: " + pendingTasks);
        System.out.println("monthlyTarget: " + monthlyTarget);
        System.out.println("weeklyProgress: " + weeklyProgress);
        System.out.println("weeklyProgressLabel: " + weeklyProgressLabel);
        System.out.println("todayRevenueTrend: " + todayRevenueTrend);
        System.out.println("activeCustomersTrend: " + activeCustomersTrend);
        System.out.println("monthlyTargetProgress: " + monthlyTargetProgress);
        System.out.println("quickAddCustomerBtn: " + quickAddCustomerBtn);
        System.out.println("quickAddTransactionBtn: " + quickAddTransactionBtn);
        System.out.println("viewDailyReportBtn: " + viewDailyReportBtn);
        System.out.println("appointmentManagementBtn: " + appointmentManagementBtn);
        System.out.println("viewAllCustomersBtn: " + viewAllCustomersBtn);
        System.out.println("viewAllTransactionsBtn: " + viewAllTransactionsBtn);
        System.out.println("mainController: " + mainController);
        loadTodayRevenue();
        loadActiveCustomers();
        loadPendingTasks();
        loadMonthlyTarget();
        loadWeeklyProgress();
        setupEventHandlers();
        System.out.println("仪表盘数据加载完成");
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
        
        // 计算今日营收与昨日的对比
        if (todayRevenueTrend != null) {
            LocalDate yesterday = today.minusDays(1);
            LocalDateTime startOfYesterday = yesterday.atStartOfDay();
            LocalDateTime endOfYesterday = yesterday.atTime(23, 59, 59);
            
            BigDecimal yesterdayTotal = transactionService.findTotalAmountByDateRange(startOfYesterday, endOfYesterday);
            if (yesterdayTotal == null) {
                yesterdayTotal = BigDecimal.ZERO;
            }
            
            if (yesterdayTotal.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal difference = total.subtract(yesterdayTotal);
                double percentage = difference.divide(yesterdayTotal, 4, RoundingMode.HALF_UP).doubleValue() * 100;
                
                if (percentage > 0) {
                    todayRevenueTrend.setText("↑ " + String.format("%.1f%%", percentage));
                    todayRevenueTrend.getStyleClass().add("up");
                    todayRevenueTrend.getStyleClass().remove("down");
                } else if (percentage < 0) {
                    todayRevenueTrend.setText("↓ " + String.format("%.1f%%", Math.abs(percentage)));
                    todayRevenueTrend.getStyleClass().add("down");
                    todayRevenueTrend.getStyleClass().remove("up");
                } else {
                    todayRevenueTrend.setText("→ 0.0%");
                    todayRevenueTrend.getStyleClass().remove("up");
                    todayRevenueTrend.getStyleClass().remove("down");
                }
            } else {
                todayRevenueTrend.setText("↑ 100%");
                todayRevenueTrend.getStyleClass().add("up");
                todayRevenueTrend.getStyleClass().remove("down");
            }
        }
    }

    private void loadActiveCustomers() {
        if (activeCustomers == null) {
            return;
        }
        List<Customer> customers = customerService.findAll();
        activeCustomers.setText(String.valueOf(customers.size()));
        
        // 计算活跃顾客与上周的对比
        if (activeCustomersTrend != null) {
            // 简单逻辑：假设上周顾客数与本周相同
            // 实际应用中可以根据创建时间或交易记录来计算
            int currentCount = customers.size();
            int lastWeekCount = currentCount - 2; // 假设上周比本周少2个
            if (lastWeekCount < 0) {
                lastWeekCount = 0;
            }
            
            int difference = currentCount - lastWeekCount;
            if (difference > 0) {
                activeCustomersTrend.setText("↑ " + difference);
                activeCustomersTrend.getStyleClass().add("up");
                activeCustomersTrend.getStyleClass().remove("down");
            } else if (difference < 0) {
                activeCustomersTrend.setText("↓ " + Math.abs(difference));
                activeCustomersTrend.getStyleClass().add("down");
                activeCustomersTrend.getStyleClass().remove("up");
            } else {
                activeCustomersTrend.setText("→ 0");
                activeCustomersTrend.getStyleClass().remove("up");
                activeCustomersTrend.getStyleClass().remove("down");
            }
        }
    }

    private void loadPendingTasks() {
        if (pendingTasks == null) {
            return;
        }
        // 计算待办任务数：这里可以根据实际业务逻辑调整
        // 例如：统计最近需要跟进的顾客（一周内有交易但需要后续服务的顾客）
        int taskCount = 0;
        try {
            List<Customer> allCustomers = customerService.findAll();
            // 简单逻辑：假设每个顾客需要一个待办任务
            taskCount = allCustomers.size();
        } catch (Exception e) {
            taskCount = 0;
        }
        pendingTasks.setText(String.valueOf(taskCount));
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
        
        // 假设月度目标为200000元
        BigDecimal monthlyTargetAmount = new BigDecimal(200000);
        monthlyTarget.setText("¥" + formatAmount(monthlyRevenue));
        
        // 计算月度目标完成度
        if (monthlyTargetProgress != null) {
            double progress = 0;
            if (monthlyTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
                progress = monthlyRevenue.divide(monthlyTargetAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            }
            monthlyTargetProgress.setText("已完成 " + String.format("%.0f%%", progress));
        }
    }

    private void loadWeeklyProgress() {
        if (weeklyProgress == null) {
            return;
        }
        // 计算周进度：基于本周销售额与目标销售额的比例
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            
            LocalDateTime weekStart = startOfWeek.atStartOfDay();
            LocalDateTime weekEnd = endOfWeek.atTime(23, 59, 59);
            
            // 获取本周销售额
            BigDecimal weeklySales = transactionService.findTotalAmountByDateRange(weekStart, weekEnd);
            if (weeklySales == null) {
                weeklySales = BigDecimal.ZERO;
            }
            
            // 假设每周目标销售额为5000元
            BigDecimal weeklyTarget = new BigDecimal(5000);
            
            // 计算进度比例
            double progress = 0;
            if (weeklyTarget.compareTo(BigDecimal.ZERO) > 0) {
                progress = weeklySales.divide(weeklyTarget, 2, RoundingMode.HALF_UP).doubleValue();
                // 限制进度在0-1之间
                progress = Math.min(1.0, Math.max(0.0, progress));
            }
            
            weeklyProgress.setProgress(progress);
            
            // 更新周进度标签
            if (weeklyProgressLabel != null) {
                int progressPercentage = (int) (progress * 100);
                int currentValue = weeklySales.intValue();
                int targetValue = weeklyTarget.intValue();
                weeklyProgressLabel.setText(progressPercentage + "% (" + currentValue + "/" + targetValue + ")");
            }
        } catch (Exception e) {
            weeklyProgress.setProgress(0);
            if (weeklyProgressLabel != null) {
                weeklyProgressLabel.setText("0% (0/5000)");
            }
        }
    }

    private void setupEventHandlers() {
        System.out.println("开始设置事件处理器");
        System.out.println("quickAddCustomerBtn: " + quickAddCustomerBtn);
        if (quickAddCustomerBtn != null) {
            quickAddCustomerBtn.setOnAction(event -> handleQuickAddCustomer());
            System.out.println("quickAddCustomerBtn 事件处理器已设置");
        }
        
        System.out.println("quickAddTransactionBtn: " + quickAddTransactionBtn);
        if (quickAddTransactionBtn != null) {
            quickAddTransactionBtn.setOnAction(event -> handleQuickAddTransaction());
            System.out.println("quickAddTransactionBtn 事件处理器已设置");
        }
        
        System.out.println("viewDailyReportBtn: " + viewDailyReportBtn);
        if (viewDailyReportBtn != null) {
            viewDailyReportBtn.setOnAction(event -> handleViewDailyReport());
            System.out.println("viewDailyReportBtn 事件处理器已设置");
        }
        
        System.out.println("appointmentManagementBtn: " + appointmentManagementBtn);
        if (appointmentManagementBtn != null) {
            appointmentManagementBtn.setOnAction(event -> handleAppointmentManagement());
            System.out.println("appointmentManagementBtn 事件处理器已设置");
        }
        
        System.out.println("viewAllCustomersBtn: " + viewAllCustomersBtn);
        if (viewAllCustomersBtn != null) {
            viewAllCustomersBtn.setOnAction(event -> handleViewAllCustomers());
            System.out.println("viewAllCustomersBtn 事件处理器已设置");
        }
        
        System.out.println("viewAllTransactionsBtn: " + viewAllTransactionsBtn);
        if (viewAllTransactionsBtn != null) {
            viewAllTransactionsBtn.setOnAction(event -> handleViewAllTransactions());
            System.out.println("viewAllTransactionsBtn 事件处理器已设置");
        }
        System.out.println("事件处理器设置完成");
    }

    @FXML
    private void handleQuickAddCustomer() {
        System.out.println("handleQuickAddCustomer 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，调用 handleCustomerManagement()");
            mainController.handleCustomerManagement();
        } else {
            System.out.println("mainController 为空");
        }
    }

    @FXML
    private void handleQuickAddTransaction() {
        System.out.println("handleQuickAddTransaction 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，调用 handleFinanceManagement()");
            mainController.handleFinanceManagement();
        } else {
            System.out.println("mainController 为空");
        }
    }

    @FXML
    private void handleViewDailyReport() {
        System.out.println("handleViewDailyReport 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，调用 handleReports()");
            mainController.handleReports();
        } else {
            System.out.println("mainController 为空");
        }
    }

    @FXML
    private void handleAppointmentManagement() {
        System.out.println("handleAppointmentManagement 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，显示提示对话框");
            mainController.showSuccessDialog("提示", "预约管理功能开发中...");
        } else {
            System.out.println("mainController 为空");
        }
    }

    @FXML
    private void handleViewAllCustomers() {
        System.out.println("handleViewAllCustomers 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，调用 handleCustomerManagement()");
            mainController.handleCustomerManagement();
        } else {
            System.out.println("mainController 为空");
        }
    }

    @FXML
    private void handleViewAllTransactions() {
        System.out.println("handleViewAllTransactions 被调用");
        if (mainController != null) {
            System.out.println("mainController 不为空，调用 handleFinanceManagement()");
            mainController.handleFinanceManagement();
        } else {
            System.out.println("mainController 为空");
        }
    }

    public void refreshData() {
        loadDashboardData();
    }

    private String formatAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toString();
    }
}
