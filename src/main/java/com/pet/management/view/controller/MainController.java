package com.pet.management.view.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

@Controller
public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private Label currentDate;

    @FXML
    private Label currentTime;

    @FXML
    private TextField globalSearchField;

    @FXML
    private Button quickAddCustomer;

    @FXML
    private Button quickAddTransaction;

    @FXML
    private Button navDashboard;
    
    @FXML
    private Button navCustomer;
    
    @FXML
    private Button navFinance;
    
    @FXML
    private Button navClerk;
    
    @FXML
    private Button navReport;
    
    @FXML
    private Button navSettings;

    @Autowired
    private CustomerController customerController;

    @Autowired
    private FinanceController financeController;

    @Autowired
    private ReportController reportController;

    @Autowired
    private SettingsController settingsController;

    @Autowired
    private ClerkController clerkController;

    @Autowired
    @Lazy
    private DashboardController dashboardController;

    private Timeline clockTimeline;

    @FXML
    public void initialize() {
        initializeClock();
        initializeToolbar();
        showDashboard();
        System.out.println("欢迎使用宠物管家");
    }

    private void initializeToolbar() {
        if (globalSearchField != null) {
            globalSearchField.setOnAction(event -> handleGlobalSearch());
        }
        
        if (quickAddCustomer != null) {
            quickAddCustomer.setOnAction(event -> handleQuickAddCustomer());
        }
        
        if (quickAddTransaction != null) {
            quickAddTransaction.setOnAction(event -> handleQuickAddTransaction());
        }
    }

    @FXML
    private void handleGlobalSearch() {
        String keyword = globalSearchField.getText();
        if (keyword != null && !keyword.trim().isEmpty()) {
            showCustomerManagement();
            showSuccessDialog("搜索", "正在搜索: " + keyword);
        }
    }

    @FXML
    private void handleQuickAddCustomer() {
        showCustomerManagement();
    }

    @FXML
    private void handleQuickAddTransaction() {
        showFinanceManagement();
    }

    private void initializeClock() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            currentDate.setText(now.format(dateFormatter));
            currentTime.setText(now.format(timeFormatter));
        }));
        
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
        
        LocalDateTime now = LocalDateTime.now();
        currentDate.setText(now.format(dateFormatter));
        currentTime.setText(now.format(timeFormatter));
    }

    @FXML
    public void handleDashboard() {
        showDashboard();
    }

    @FXML
    public void handleCustomerManagement() {
        showCustomerManagement();
    }

    @FXML
    public void handleFinanceManagement() {
        showFinanceManagement();
    }

    @FXML
    public void handleReports() {
        showReports();
    }

    @FXML
    public void handleClerkManagement() {
        showClerkManagement();
    }

    @FXML
    public void handleSettings() {
        showSettings();
    }

    @FXML
    public void handleAbout() {
        showAbout();
    }

    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            loader.setController(dashboardController);
            Parent dashboardView = loader.load();
            contentPane.getChildren().setAll(dashboardView);
            updateActiveNavButton("dashboard");
            // 加载 FXML 后手动初始化数据
            dashboardController.loadDashboardData();
        } catch (IOException e) {
            showErrorDialog("加载仪表盘失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCustomerManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer.fxml"));
            loader.setController(customerController);
            Parent customerView = loader.load();
            contentPane.getChildren().setAll(customerView);
            updateActiveNavButton("customer");
        } catch (IOException e) {
            showErrorDialog("加载顾客管理页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showFinanceManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/finance.fxml"));
            loader.setController(financeController);
            Parent financeView = loader.load();
            contentPane.getChildren().setAll(financeView);
            updateActiveNavButton("finance");
        } catch (IOException e) {
            showErrorDialog("加载财务管理页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
            loader.setController(reportController);
            Parent reportView = loader.load();
            contentPane.getChildren().setAll(reportView);
            updateActiveNavButton("reports");
        } catch (IOException e) {
            showErrorDialog("加载报表中心页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showClerkManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clerk.fxml"));
            loader.setController(clerkController);
            Parent clerkView = loader.load();
            contentPane.getChildren().setAll(clerkView);
            updateActiveNavButton("clerk");
        } catch (IOException e) {
            showErrorDialog("加载员工管理页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
            loader.setController(settingsController);
            Parent settingsView = loader.load();
            contentPane.getChildren().setAll(settingsView);
            updateActiveNavButton("settings");
        } catch (IOException e) {
            showErrorDialog("加载系统设置页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            Parent aboutView = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("关于");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(aboutView));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            showErrorDialog("加载关于对话框失败", e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateActiveNavButton(String activePage) {
        if (navDashboard != null) navDashboard.getStyleClass().remove("active");
        if (navCustomer != null) navCustomer.getStyleClass().remove("active");
        if (navFinance != null) navFinance.getStyleClass().remove("active");
        if (navClerk != null) navClerk.getStyleClass().remove("active");
        if (navReport != null) navReport.getStyleClass().remove("active");
        if (navSettings != null) navSettings.getStyleClass().remove("active");
        
        switch (activePage) {
            case "dashboard":
                if (navDashboard != null) navDashboard.getStyleClass().add("active");
                break;
            case "customer":
                if (navCustomer != null) navCustomer.getStyleClass().add("active");
                break;
            case "finance":
                if (navFinance != null) navFinance.getStyleClass().add("active");
                break;
            case "clerk":
                if (navClerk != null) navClerk.getStyleClass().add("active");
                break;
            case "reports":
                if (navReport != null) navReport.getStyleClass().add("active");
                break;
            case "settings":
                if (navSettings != null) navSettings.getStyleClass().add("active");
                break;
        }
    }

    public void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认");
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        return alert.showAndWait().map(buttonType -> 
            buttonType == javafx.scene.control.ButtonType.OK
        ).orElse(false);
    }
}
