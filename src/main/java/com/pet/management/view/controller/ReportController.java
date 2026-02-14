package com.pet.management.view.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pet.management.service.ReportService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @FXML
    private Button dailyReportButton;

    @FXML
    private Button monthlyReportButton;

    @FXML
    private Button annualReportButton;

    @FXML
    private Button exportPdfButton;

    @FXML
    private Button exportExcelButton;

    @FXML
    private TextArea reportPreview;

    private String currentReportContent;

    @FXML
    public void initialize() {
        // 初始化报表中心界面
        reportPreview.setEditable(false);
    }

    // 生成日报表
    @FXML
    public void generateDailyReport() {
        currentReportContent = reportService.generateDailyReport();
        reportPreview.setText(currentReportContent);
    }

    // 生成月报表
    @FXML
    public void generateMonthlyReport() {
        currentReportContent = reportService.generateMonthlyReport();
        reportPreview.setText(currentReportContent);
    }

    // 生成年报表
    @FXML
    public void generateAnnualReport() {
        currentReportContent = reportService.generateYearlyReport();
        reportPreview.setText(currentReportContent);
    }

    // 导出报表为PDF
    @FXML
    public void exportPdfReport() {
        if (currentReportContent == null || currentReportContent.isEmpty()) {
            reportPreview.setText("请先生成报表再导出");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出PDF报表");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF文件", "*.pdf"));
        fileChooser.setInitialFileName("报表_" + System.currentTimeMillis() + ".pdf");

        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            String exportedPath = reportService.exportReportToPdf(currentReportContent, "报表");
            if (exportedPath != null) {
                reportPreview.setText("报表已成功导出到: " + exportedPath);
            } else {
                reportPreview.setText("报表导出失败");
            }
        }
    }

    // 导出报表为Excel
    @FXML
    public void exportExcelReport() {
        reportPreview.setText("Excel导出功能正在开发中...");
    }

    // 生成销售统计图表
    @FXML
    public void generateSalesChart() {
        reportService.generateSalesChart("monthly");
    }
}
