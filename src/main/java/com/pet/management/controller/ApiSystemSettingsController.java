package com.pet.management.controller;

import com.pet.management.model.SystemSettings;
import com.pet.management.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class ApiSystemSettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    // 获取系统设置
    @GetMapping
    public ResponseEntity<SystemSettings> getSettings() {
        try {
            SystemSettings settings = systemSettingsService.getSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 保存系统设置
    @PostMapping
    public ResponseEntity<SystemSettings> saveSettings(@RequestBody SystemSettings settings) {
        try {
            SystemSettings savedSettings = systemSettingsService.saveSettings(settings);
            return ResponseEntity.ok(savedSettings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 手动备份数据库
    @PostMapping("/backup")
    public ResponseEntity<Map<String, Object>> backupDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            String backupFileName = systemSettingsService.backupDatabase();
            result.put("success", true);
            result.put("message", "备份成功");
            result.put("backupFileName", backupFileName);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "备份失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 恢复数据库
    @PostMapping("/restore")
    public ResponseEntity<Map<String, Object>> restoreDatabase(@RequestParam String backupFileName) {
        Map<String, Object> result = new HashMap<>();
        try {
            systemSettingsService.restoreDatabase(backupFileName);
            result.put("success", true);
            result.put("message", "恢复成功，系统将重启生效");
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "恢复失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 清除数据库
    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            systemSettingsService.clearDatabase();
            result.put("success", true);
            result.put("message", "数据清除成功，系统将重启生效");
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "清除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 获取备份文件列表
    @GetMapping("/backups")
    public ResponseEntity<Map<String, Object>> getBackupFiles() {
        Map<String, Object> result = new HashMap<>();
        try {
            String[] backupFiles = systemSettingsService.getBackupFiles();
            result.put("success", true);
            result.put("files", backupFiles);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "获取备份列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
