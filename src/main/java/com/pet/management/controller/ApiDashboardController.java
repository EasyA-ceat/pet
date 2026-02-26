
package com.pet.management.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Customer;
import com.pet.management.service.CustomerService;
import com.pet.management.service.TransactionService;

@RestController
@RequestMapping("/api/dashboard")
public class ApiDashboardController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        // 今日营收
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        BigDecimal todayTotal = transactionService.findTotalAmountByDateRange(startOfDay, endOfDay);
        if (todayTotal == null) todayTotal = BigDecimal.ZERO;
        data.put("todayRevenue", "¥" + todayTotal.setScale(2, RoundingMode.HALF_UP).toString());
        
        // 今日营收趋势
        LocalDate yesterday = today.minusDays(1);
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        LocalDateTime endOfYesterday = yesterday.atTime(23, 59, 59);
        BigDecimal yesterdayTotal = transactionService.findTotalAmountByDateRange(startOfYesterday, endOfYesterday);
        if (yesterdayTotal == null) yesterdayTotal = BigDecimal.ZERO;
        
        String revenueTrend = "→ 0.0%";
        if (yesterdayTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = todayTotal.subtract(yesterdayTotal);
            double percentage = difference.divide(yesterdayTotal, 4, RoundingMode.HALF_UP).doubleValue() * 100;
            if (percentage > 0) {
                revenueTrend = "↑ " + String.format("%.1f%%", percentage);
            } else if (percentage < 0) {
                revenueTrend = "↓ " + String.format("%.1f%%", Math.abs(percentage));
            }
        } else if (todayTotal.compareTo(BigDecimal.ZERO) > 0) {
            revenueTrend = "↑ 100%";
        }
        data.put("todayRevenueTrend", revenueTrend);
        
        // 活跃顾客
        List<Customer> customers = customerService.findAll();
        data.put("activeCustomers", String.valueOf(customers.size()));
        
        // 活跃顾客趋势
        int currentCount = customers.size();
        int lastWeekCount = Math.max(0, currentCount - 2);
        int customerDiff = currentCount - lastWeekCount;
        String customerTrend = "→ 0";
        if (customerDiff > 0) {
            customerTrend = "↑ " + customerDiff;
        } else if (customerDiff < 0) {
            customerTrend = "↓ " + Math.abs(customerDiff);
        }
        data.put("activeCustomersTrend", customerTrend);
        
        // 待办任务
        data.put("pendingTasks", String.valueOf(customers.size()));
        
        // 月度目标
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);
        BigDecimal monthlyRevenue = transactionService.findTotalAmountByDateRange(startOfMonth, endOfMonth);
        if (monthlyRevenue == null) monthlyRevenue = BigDecimal.ZERO;
        data.put("monthlyTarget", "¥" + monthlyRevenue.setScale(2, RoundingMode.HALF_UP).toString());
        
        // 月度目标完成度
        BigDecimal monthlyTargetAmount = new BigDecimal("200000");
        double progress = 0;
        if (monthlyTargetAmount.compareTo(BigDecimal.ZERO) > 0) {
            progress = monthlyRevenue.divide(monthlyTargetAmount, 4, RoundingMode.HALF_UP).doubleValue() * 100;
        }
        data.put("monthlyTargetProgress", "已完成 " + String.format("%.0f%%", progress));
        
        // 周进度
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        LocalDateTime weekStart = startOfWeek.atStartOfDay();
        LocalDateTime weekEnd = endOfWeek.atTime(23, 59, 59);
        BigDecimal weeklySales = transactionService.findTotalAmountByDateRange(weekStart, weekEnd);
        if (weeklySales == null) weeklySales = BigDecimal.ZERO;
        BigDecimal weeklyTarget = new BigDecimal("5000");
        
        double weeklyProgress = 0;
        if (weeklyTarget.compareTo(BigDecimal.ZERO) > 0) {
            weeklyProgress = weeklySales.divide(weeklyTarget, 2, RoundingMode.HALF_UP).doubleValue();
            weeklyProgress = Math.min(1.0, Math.max(0.0, weeklyProgress));
        }
        data.put("weeklyProgress", weeklyProgress * 100);
        
        int progressPercentage = (int) (weeklyProgress * 100);
        int currentValue = weeklySales.intValue();
        int targetValue = weeklyTarget.intValue();
        data.put("weeklyProgressLabel", progressPercentage + "% (" + currentValue + "/" + targetValue + ")");
        
        return data;
    }
}
