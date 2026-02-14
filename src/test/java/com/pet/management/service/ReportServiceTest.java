package com.pet.management.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pet.management.model.Clerk;
import com.pet.management.model.Customer;
import com.pet.management.model.Transaction;
import com.pet.management.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportService reportService;

    private List<Transaction> mockTransactions;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        mockTransactions = new ArrayList<>();
        Customer customer1 = new Customer("张三", "13800138001", "旺财", "狗", "金毛", 3);
        customer1.setId(1L);

        Customer customer2 = new Customer("李四", "13800138002", "咪咪", "猫", "波斯猫", 2);
        customer2.setId(2L);

        Clerk clerk1 = new Clerk("王五", "13800138003", new BigDecimal("0.1"));
        clerk1.setId(1L);

        Transaction transaction1 = new Transaction(customer1, clerk1, "洗澡", new BigDecimal("50"), new BigDecimal("5"));
        transaction1.setId(1L);
        transaction1.setTransactionDate(LocalDateTime.now().withHour(10).withMinute(0));

        Transaction transaction2 = new Transaction(customer2, clerk1, "美容", new BigDecimal("100"), new BigDecimal("10"));
        transaction2.setId(2L);
        transaction2.setTransactionDate(LocalDateTime.now().withHour(14).withMinute(0));

        mockTransactions.add(transaction1);
        mockTransactions.add(transaction2);
    }

    @Test
    void testGenerateDailyReport() {
        // 设置模拟数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(mockTransactions);

        // 测试日报表生成
        String reportContent = reportService.generateDailyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("日报表"));
        assertTrue(reportContent.contains("张三"));
        assertTrue(reportContent.contains("李四"));
        assertTrue(reportContent.contains("150"));
    }

    @Test
    void testGenerateMonthlyReport() {
        // 设置模拟数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(mockTransactions);

        // 测试月报表生成
        String reportContent = reportService.generateMonthlyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("月报表"));
        assertTrue(reportContent.contains("张三"));
        assertTrue(reportContent.contains("李四"));
        assertTrue(reportContent.contains("150"));
    }

    @Test
    void testGenerateYearlyReport() {
        // 设置模拟数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(mockTransactions);

        // 测试年报表生成
        String reportContent = reportService.generateYearlyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("年报表"));
        assertTrue(reportContent.contains("张三"));
        assertTrue(reportContent.contains("李四"));
        assertTrue(reportContent.contains("150"));
    }

    @Test
    void testExportReportToPdf() {
        // 测试PDF导出
        String reportContent = "测试报表内容";
        String reportType = "日报表";

        String filePath = reportService.exportReportToPdf(reportContent, reportType);

        assertNotNull(filePath);
        assertTrue(filePath.endsWith(".pdf"));
    }

    @Test
    void testExportReportToExcel() {
        // 测试Excel导出
        String reportType = "日报表";

        String filePath = reportService.exportReportToExcel(mockTransactions, reportType);

        assertNotNull(filePath);
        assertTrue(filePath.endsWith(".xlsx"));
    }

    @Test
    void testGenerateSalesChart() {
        // 测试生成销售图表
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(mockTransactions);

        // 测试不会抛出异常
        assertDoesNotThrow(() -> reportService.generateSalesChart("daily"));
        assertDoesNotThrow(() -> reportService.generateSalesChart("monthly"));
        assertDoesNotThrow(() -> reportService.generateSalesChart("yearly"));
    }

    @Test
    void testGenerateDailyReportEmpty() {
        // 设置空数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(new ArrayList<>());

        // 测试空数据的日报表
        String reportContent = reportService.generateDailyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("日报表"));
        assertTrue(reportContent.contains("0"));
    }

    @Test
    void testGenerateMonthlyReportEmpty() {
        // 设置空数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(new ArrayList<>());

        // 测试空数据的月报表
        String reportContent = reportService.generateMonthlyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("月报表"));
        assertTrue(reportContent.contains("0"));
    }

    @Test
    void testGenerateYearlyReportEmpty() {
        // 设置空数据
        when(transactionRepository.findByTransactionDateBetween(any(), any())).thenReturn(new ArrayList<>());

        // 测试空数据的年报表
        String reportContent = reportService.generateYearlyReport();

        assertNotNull(reportContent);
        assertTrue(reportContent.contains("年报表"));
        assertTrue(reportContent.contains("0"));
    }
}