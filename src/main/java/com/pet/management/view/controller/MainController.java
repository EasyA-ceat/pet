package com.pet.management.view.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    private Label statusLabel;

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

    private Timeline clockTimeline;

    @FXML
    public void initialize() {
        // 初始化时钟
        initializeClock();
        
        // 默认显示顾客管理界面
        showCustomerManagement();
        
        // 设置状态信息
        updateStatus("欢迎使用宠物管理系统");
    }

    private void initializeClock() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalDateTime now = LocalDateTime.now();
            currentDate.setText(now.format(dateFormatter));
            currentTime.setText(now.format(timeFormatter));
        }));
        
        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
        
        // 立即更新时间
        LocalDateTime now = LocalDateTime.now();
        currentDate.setText(now.format(dateFormatter));
        currentTime.setText(now.format(timeFormatter));
    }

    // 顾客管理
    @FXML
    public void handleCustomerManagement() {
        showCustomerManagement();
        updateStatus("顾客管理页面已加载");
    }

    // 财务管理
    @FXML
    public void handleFinanceManagement() {
        showFinanceManagement();
        updateStatus("财务管理页面已加载");
    }

    // 报表中心
    @FXML
    public void handleReports() {
        showReports();
        updateStatus("报表中心页面已加载");
    }

    // 员工管理
    @FXML
    public void handleClerkManagement() {
        showClerkManagement();
        updateStatus("员工管理页面已加载");
    }

    // 系统设置
    @FXML
    public void handleSettings() {
        showSettings();
        updateStatus("系统设置页面已加载");
    }

    // 关于
    @FXML
    public void handleAbout() {
        showAbout();
    }

    // 显示顾客管理界面
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

    // 显示财务管理界面
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

    // 显示报表中心界面
    private void showReports() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reports.fxml"));
            loader.setController(reportController);
            Parent reportView = loader.load();
            contentPane.getChildren().setAll(reportView);
            updateActiveNavButton("reports");
        } catch (IOException e) {
            showErrorDialog("加载报表中心页面失败", e.getMessage());
            e.printStackTrace();
        }
    }

    // 显示员工管理界面
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

    // 显示系统设置界面
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

    // 显示关于对话框
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

    // 更新导航按钮状态
    private void updateActiveNavButton(String activePage) {
        // 这里可以添加导航按钮的样式更新逻辑
        // 由于FXML中没有直接引用导航按钮，需要通过其他方式获取
    }

    // 更新状态信息
    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    // 显示错误对话框
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 显示成功对话框
    public void showSuccessDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("成功");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 显示确认对话框
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
