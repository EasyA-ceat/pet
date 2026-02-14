package com.pet.management.view.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

@Controller
public class SettingsController {

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

    private static final String SETTINGS_FILE = System.getProperty("user.home") + "/.pet-management-system/settings.properties";
    private static final String DEFAULT_DATABASE_PATH = System.getProperty("user.home") + "/.pet-management-system/pet_db.sqlite";
    private static final String DEFAULT_BACKUP_PATH = System.getProperty("user.home") + "/.pet-management-system/backup";
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
        // 加载系统设置
        Properties properties = new Properties();
        File settingsFile = new File(SETTINGS_FILE);

        if (settingsFile.exists()) {
            try {
                properties.load(Files.newInputStream(settingsFile.toPath()));
                if (systemNameField != null) systemNameField.setText(properties.getProperty("systemName", DEFAULT_SYSTEM_NAME));
                if (databasePathField != null) databasePathField.setText(properties.getProperty("databasePath", DEFAULT_DATABASE_PATH));
                if (backupPathField != null) backupPathField.setText(properties.getProperty("backupPath", DEFAULT_BACKUP_PATH));
                if (autoBackupCheckBox != null) autoBackupCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty("autoBackup", "false")));
            } catch (IOException e) {
                showAlert("错误", "加载系统设置失败: " + e.getMessage());
            }
        } else {
            // 使用默认设置
            if (systemNameField != null) systemNameField.setText(DEFAULT_SYSTEM_NAME);
            if (databasePathField != null) databasePathField.setText(DEFAULT_DATABASE_PATH);
            if (backupPathField != null) backupPathField.setText(DEFAULT_BACKUP_PATH);
            if (autoBackupCheckBox != null) autoBackupCheckBox.setSelected(false);
        }
    }

    public void saveSettings() {
        // 保存系统设置
        Properties properties = new Properties();
        if (systemNameField != null) properties.setProperty("systemName", systemNameField.getText());
        if (databasePathField != null) properties.setProperty("databasePath", databasePathField.getText());
        if (backupPathField != null) properties.setProperty("backupPath", backupPathField.getText());
        if (autoBackupCheckBox != null) properties.setProperty("autoBackup", String.valueOf(autoBackupCheckBox.isSelected()));

        try {
            // 确保配置文件目录存在
            Path settingsDir = Paths.get(SETTINGS_FILE).getParent();
            if (settingsDir != null && !Files.exists(settingsDir)) {
                Files.createDirectories(settingsDir);
            }
            properties.store(Files.newOutputStream(Paths.get(SETTINGS_FILE)), "系统设置");
            showAlert("成功", "系统设置已保存");
        } catch (IOException e) {
            showAlert("错误", "保存系统设置失败: " + e.getMessage());
        }
    }

    public void resetSettings() {
        // 重置系统设置
        if (systemNameField != null) systemNameField.setText(DEFAULT_SYSTEM_NAME);
        if (databasePathField != null) databasePathField.setText(DEFAULT_DATABASE_PATH);
        if (backupPathField != null) backupPathField.setText(DEFAULT_BACKUP_PATH);
        if (autoBackupCheckBox != null) autoBackupCheckBox.setSelected(false);
        showAlert("成功", "系统设置已重置");
    }

    public void backupData() {
        // 备份数据
        String backupPath = (backupPathField != null) ? backupPathField.getText() : DEFAULT_BACKUP_PATH;
        String databasePath = (databasePathField != null) ? databasePathField.getText() : DEFAULT_DATABASE_PATH;

        try {
            // 确保备份目录存在
            Path backupDir = Paths.get(backupPath);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            // 备份数据库文件
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String backupFileName = "pet_db_backup_" + timestamp + ".sqlite";
            Path backupFile = backupDir.resolve(backupFileName);

            Files.copy(Paths.get(databasePath), backupFile, StandardCopyOption.REPLACE_EXISTING);
            showAlert("成功", "数据库备份成功: " + backupFile.toAbsolutePath());
        } catch (IOException e) {
            showAlert("错误", "备份数据库失败: " + e.getMessage());
        }
    }

    public void restoreData() {
        // 恢复数据
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
                String databasePath = databasePathField.getText();
                Files.copy(selectedFile.toPath(), Paths.get(databasePath), StandardCopyOption.REPLACE_EXISTING);
                showAlert("成功", "数据库恢复成功");
            } catch (IOException e) {
                showAlert("错误", "恢复数据库失败: " + e.getMessage());
            }
        }
    }

    public void cleanData() {
        // 清理数据
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
                // 清理数据逻辑
                String databasePath = databasePathField.getText();
                File databaseFile = new File(databasePath);

                if (databaseFile.exists()) {
                    if (databaseFile.delete()) {
                        showAlert("成功", "数据清理成功");
                    } else {
                        showAlert("错误", "数据清理失败");
                    }
                } else {
                    showAlert("警告", "数据库文件不存在");
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
