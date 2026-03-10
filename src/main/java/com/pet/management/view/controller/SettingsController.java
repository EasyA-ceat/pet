package com.pet.management.view.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.pet.management.model.SystemSettings;
import com.pet.management.service.SystemSettingsService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

@Controller
public class SettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    @FXML
    private TextField systemNameField;
    @FXML
    private TextField databasePathField;
    @FXML
    private TextField backupPathField;
    @FXML
    private CheckBox autoBackupCheckBox;
    @FXML
    private Button backupButton;
    @FXML
    private Button restoreButton;
    @FXML
    private Button cleanButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button closeButton;
    @FXML
    private Label versionLabel;
    @FXML
    private Label developerLabel;
    @FXML
    private Label contactLabel;
    @FXML
    private Label copyrightLabel;

    private SystemSettings currentSettings;

    private static final String DEFAULT_SYSTEM_NAME = "宠物管理系统";

    @FXML
    public void initialize() {
        // 初始化系统设置界面
        loadSettings();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        if (backupButton != null) backupButton.setOnAction(event -> backupData());
        if (restoreButton != null) restoreButton.setOnAction(event -> restoreData());
        if (cleanButton != null) cleanButton.setOnAction(event -> cleanData());
        if (saveButton != null) saveButton.setOnAction(event -> saveSettings());
        if (resetButton != null) resetButton.setOnAction(event -> resetSettings());
        if (closeButton != null) closeButton.setOnAction(event -> close());
    }

    // 系统设置功能方法
    public void loadSettings() {
        try {
            currentSettings = systemSettingsService.getSettings();
            if (systemNameField != null) systemNameField.setText(currentSettings.getSystemName() != null ? currentSettings.getSystemName() : DEFAULT_SYSTEM_NAME);
            if (databasePathField != null) databasePathField.setText(currentSettings.getDatabasePath());
            if (backupPathField != null) backupPathField.setText(currentSettings.getBackupPath());
            if (autoBackupCheckBox != null) autoBackupCheckBox.setSelected(Boolean.TRUE.equals(currentSettings.getAutoBackupEnabled()));
        } catch (Exception e) {
            showAlert("错误", "加载系统设置失败: " + e.getMessage());
        }
    }

    public void saveSettings() {
        try {
            if (systemNameField != null) currentSettings.setSystemName(systemNameField.getText());
            if (databasePathField != null) currentSettings.setDatabasePath(databasePathField.getText());
            if (backupPathField != null) currentSettings.setBackupPath(backupPathField.getText());
            if (autoBackupCheckBox != null) currentSettings.setAutoBackupEnabled(autoBackupCheckBox.isSelected());

            systemSettingsService.saveSettings(currentSettings);
            showAlert("成功", "系统设置已保存");
        } catch (Exception e) {
            showAlert("错误", "保存系统设置失败: " + e.getMessage());
        }
    }

    public void resetSettings() {
        try {
            currentSettings = systemSettingsService.getSettings();
            if (systemNameField != null) systemNameField.setText(DEFAULT_SYSTEM_NAME);
            if (databasePathField != null) databasePathField.setText(currentSettings.getDatabasePath());
            if (backupPathField != null) backupPathField.setText(currentSettings.getBackupPath());
            if (autoBackupCheckBox != null) autoBackupCheckBox.setSelected(false);
            showAlert("成功", "系统设置已重置");
        } catch (Exception e) {
            showAlert("错误", "重置系统设置失败: " + e.getMessage());
        }
    }

    public void backupData() {
        try {
            String backupFileName = systemSettingsService.backupDatabase();
            showAlert("成功", "数据库备份成功: " + backupFileName);
        } catch (IOException e) {
            showAlert("错误", "备份数据库失败: " + e.getMessage());
        }
    }

    public void restoreData() {
        // 避免在测试中调用JavaFX UI组件
        if (databasePathField == null) {
            showAlert("警告", "UI组件未初始化，无法恢复数据");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择备份文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite数据库文件", "*.sqlite"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                systemSettingsService.restoreDatabase(selectedFile.getName());
                showAlert("成功", "数据库恢复成功");
            } catch (IOException e) {
                showAlert("错误", "恢复数据库失败: " + e.getMessage());
            }
        }
    }

    public void cleanData() {
        // 避免在测试中调用JavaFX UI组件
        if (databasePathField == null) {
            showAlert("警告", "UI组件未初始化，无法清理数据");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认清理");
        alert.setHeaderText("清理数据");
        alert.setContentText("确定要清理所有数据吗？此操作不可恢复！");
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    systemSettingsService.clearDatabase();
                    showAlert("成功", "数据清理成功");
                } catch (IOException e) {
                    showAlert("错误", "数据清理失败: " + e.getMessage());
                }
            }
        });
    }

    public void close() {
        // 关闭系统设置界面
        if (closeButton != null && closeButton.getScene() != null && closeButton.getScene().getWindow() != null) {
            closeButton.getScene().getWindow().hide();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
