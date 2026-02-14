package com.pet.management.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pet.management.model.Transaction;
import com.pet.management.repository.TransactionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;

    private static final String REPORT_DIR = System.getProperty("user.home") + "/.pet-management-system/reports/";

    public ReportService() {
        File reportDir = new File(REPORT_DIR);
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
    }

    /**
     * 生成日报表
     */
    public String generateDailyReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startOfDay, endOfDay);

        return generateReport("日报表", transactions, startOfDay, endOfDay);
    }

    /**
     * 生成月报表
     */
    public String generateMonthlyReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startOfMonth, endOfMonth);

        return generateReport("月报表", transactions, startOfMonth, endOfMonth);
    }

    /**
     * 生成年报表
     */
    public String generateYearlyReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfYear = today.withDayOfYear(1).atStartOfDay();
        LocalDateTime endOfYear = today.withDayOfYear(today.lengthOfYear()).atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startOfYear, endOfYear);

        return generateReport("年报表", transactions, startOfYear, endOfYear);
    }

    /**
     * 生成报表内容
     */
    private String generateReport(String title, List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder report = new StringBuilder();

        report.append(title).append("\n");
        report.append("------------------------------------------------------------------------\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        report.append("日期范围: ").append(startDate.format(formatter)).append(" 至 ").append(endDate.format(formatter)).append("\n\n");

        report.append("交易统计:\n");
        report.append("  交易数量: ").append(transactions.size()).append("\n");

        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.append("  总金额: ").append(totalAmount.setScale(2, RoundingMode.HALF_UP)).append(" 元\n");

        BigDecimal totalCommission = transactions.stream()
                .map(Transaction::getCommission)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.append("  总提成: ").append(totalCommission.setScale(2, RoundingMode.HALF_UP)).append(" 元\n\n");

        if (!transactions.isEmpty()) {
            report.append("交易详情:\n");
            report.append("------------------------------------------------------------------------\n");
            report.append(String.format("%-10s %-15s %-10s %-12s %-10s\n", "ID", "顾客", "宠物", "服务类型", "金额"));
            report.append("------------------------------------------------------------------------\n");

            for (Transaction transaction : transactions) {
                report.append(String.format("%-10d %-15s %-10s %-12s %-10.2f\n",
                        transaction.getId(),
                        transaction.getCustomer().getCustomerName(),
                        transaction.getCustomer().getPetName(),
                        transaction.getServiceType(),
                        transaction.getAmount()));
            }
        }

        return report.toString();
    }

    /**
     * 导出报表为PDF
     */
    public String exportReportToPdf(String reportContent, String reportType) {
        String fileName = reportType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".pdf";
        String filePath = REPORT_DIR + fileName;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph(reportContent));

            document.close();

            return filePath;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导出报表为Excel
     */
    public String exportReportToExcel(List<Transaction> transactions, String reportType) {
        String fileName = reportType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        String filePath = REPORT_DIR + fileName;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("交易记录");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "顾客姓名", "宠物姓名", "服务类型", "交易日期", "金额", "提成"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                // 设置表头样式
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(style);
            }

            // 填充数据
            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                Row dataRow = sheet.createRow(i + 1);

                dataRow.createCell(0).setCellValue(transaction.getId());
                dataRow.createCell(1).setCellValue(transaction.getCustomer().getCustomerName());
                dataRow.createCell(2).setCellValue(transaction.getCustomer().getPetName());
                dataRow.createCell(3).setCellValue(transaction.getServiceType());
                dataRow.createCell(4).setCellValue(transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                dataRow.createCell(5).setCellValue(transaction.getAmount().doubleValue());
                dataRow.createCell(6).setCellValue(transaction.getCommission().doubleValue());
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 保存Excel文件
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成销售统计图表
     */
    public String generateSalesChart(String reportType) {
        LocalDateTime startDate, endDate;

        switch (reportType) {
            case "daily":
                startDate = LocalDate.now().atStartOfDay();
                endDate = LocalDate.now().atTime(23, 59, 59);
                break;
            case "monthly":
                startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);
                break;
            case "yearly":
                startDate = LocalDate.now().withDayOfYear(1).atStartOfDay();
                endDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()).atTime(23, 59, 59);
                break;
            default:
                startDate = LocalDate.now().atStartOfDay();
                endDate = LocalDate.now().atTime(23, 59, 59);
        }

        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        // 按服务类型分组统计
        Map<String, BigDecimal> salesByService = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getServiceType,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        if (salesByService.isEmpty()) {
            return null;
        }

        // 生成柱状图
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("销售统计").xAxisTitle("服务类型").yAxisTitle("销售额").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setHasAnnotations(true);

        chart.addSeries("销售额",
                new ArrayList<>(salesByService.keySet()),
                salesByService.values().stream().map(BigDecimal::doubleValue).collect(Collectors.toList()));

        // 保存图表为图片
        String fileName = "chart_" + reportType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".png";
        String filePath = REPORT_DIR + fileName;
        
        try {
            org.knowm.xchart.BitmapEncoder.saveBitmap(chart, filePath, org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取报表保存目录
     */
    public String getReportDirectory() {
        return REPORT_DIR;
    }
}
