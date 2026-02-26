
package com.pet.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ApiReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/daily")
    public String getDailyReport() {
        return reportService.generateDailyReport();
    }

    @GetMapping("/monthly")
    public String getMonthlyReport() {
        return reportService.generateMonthlyReport();
    }

    @GetMapping("/annual")
    public String getAnnualReport() {
        return reportService.generateYearlyReport();
    }

    @GetMapping("/export/pdf")
    public String exportPdfReport() {
        return reportService.exportReportToPdf("报表", "报表内容");
    }

    @GetMapping("/export/excel")
    public String exportExcelReport() {
        return "Excel导出功能开发中...";
    }

    @GetMapping("/chart")
    public String getChartData(@RequestParam String type) {
        reportService.generateSalesChart(type);
        return "图表数据生成成功";
    }
}
