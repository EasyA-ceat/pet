package com.pet.management.service;

import com.pet.management.model.SystemSettings;
import com.pet.management.repository.SystemSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    private static final DateTimeFormatter BACKUP_FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // 获取系统设置，如果不存在则创建默认设置
    public SystemSettings getSettings() {
        Optional<SystemSettings> settingsOpt = systemSettingsRepository.findFirstByOrderByIdAsc();
        if (settingsOpt.isPresent()) {
            return settingsOpt.get();
        }
        // 创建默认设置
        SystemSettings defaultSettings = new SystemSettings();
        // 从数据库URL中提取数据库路径
        if (datasourceUrl.startsWith("jdbc:sqlite:")) {
            String dbPath = datasourceUrl.substring("jdbc:sqlite:".length());
            defaultSettings.setDatabasePath(dbPath);
        }
        // 设置默认备份路径
        String userHome = System.getProperty("user.home");
        String defaultBackupPath = Paths.get(userHome, ".pet-management-system", "backup").toString();
        defaultSettings.setBackupPath(defaultBackupPath);
        return systemSettingsRepository.save(defaultSettings);
    }

    // 保存系统设置
    public SystemSettings saveSettings(SystemSettings settings) {
        if (settings.getId() == null) {
            SystemSettings existing = getSettings();
            settings.setId(existing.getId());
            settings.setCreateTime(existing.getCreateTime());
        }
        return systemSettingsRepository.save(settings);
    }

    // 手动备份数据库
    public String backupDatabase() throws IOException {
        SystemSettings settings = getSettings();
        String dbPath = settings.getDatabasePath();
        String backupPath = settings.getBackupPath();

        // 验证数据库文件存在
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            throw new IOException("数据库文件不存在: " + dbPath);
        }

        // 创建备份目录
        File backupDir = new File(backupPath);
        if (!backupDir.exists() && !backupDir.mkdirs()) {
            throw new IOException("无法创建备份目录: " + backupPath);
        }

        // 生成备份文件名
        String backupFileName = "pet_db_backup_" + LocalDateTime.now().format(BACKUP_FILE_FORMATTER) + ".sqlite";
        Path backupFilePath = Paths.get(backupPath, backupFileName);

        // 复制数据库文件
        Files.copy(Paths.get(dbPath), backupFilePath, StandardCopyOption.REPLACE_EXISTING);

        // 清理过期备份
        cleanExpiredBackups();

        return backupFileName;
    }

    // 恢复数据库
    public void restoreDatabase(String backupFileName) throws IOException {
        SystemSettings settings = getSettings();
        String dbPath = settings.getDatabasePath();
        String backupPath = settings.getBackupPath();

        Path backupFilePath = Paths.get(backupPath, backupFileName);
        if (!Files.exists(backupFilePath)) {
            throw new IOException("备份文件不存在: " + backupFileName);
        }

        // 复制备份文件覆盖当前数据库
        Files.copy(backupFilePath, Paths.get(dbPath), StandardCopyOption.REPLACE_EXISTING);
    }

    // 清除数据库数据（谨慎使用）
    public void clearDatabase() throws IOException {
        SystemSettings settings = getSettings();
        String dbPath = settings.getDatabasePath();

        File dbFile = new File(dbPath);
        if (dbFile.exists() && !dbFile.delete()) {
            throw new IOException("无法删除数据库文件: " + dbPath);
        }
    }

    // 自动备份任务（由定时任务调用）
    public void autoBackup() {
        try {
            SystemSettings settings = getSettings();
            if (Boolean.TRUE.equals(settings.getAutoBackupEnabled())) {
                backupDatabase();
            }
        } catch (Exception e) {
            // 记录日志，自动备份失败不影响正常业务
            e.printStackTrace();
        }
    }

    // 清理过期备份文件
    private void cleanExpiredBackups() throws IOException {
        SystemSettings settings = getSettings();
        String backupPath = settings.getBackupPath();
        Integer retentionDays = settings.getBackupRetentionDays();
        if (retentionDays == null || retentionDays <= 0) {
            return;
        }

        Path backupDir = Paths.get(backupPath);
        if (!Files.exists(backupDir)) {
            return;
        }

        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(retentionDays);

        try (Stream<Path> files = Files.list(backupDir)) {
            files.filter(Files::isRegularFile)
                 .filter(path -> path.getFileName().toString().startsWith("pet_db_backup_"))
                 .filter(path -> {
                     try {
                         String fileName = path.getFileName().toString();
                         String timeStr = fileName.substring("pet_db_backup_".length(), fileName.length() - ".sqlite".length());
                         LocalDateTime fileTime = LocalDateTime.parse(timeStr, BACKUP_FILE_FORMATTER);
                         return fileTime.isBefore(cutoffTime);
                     } catch (Exception e) {
                         return false;
                     }
                 })
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         // 忽略删除失败的文件
                     }
                 });
        }
    }

    // 获取所有备份文件列表
    public String[] getBackupFiles() throws IOException {
        SystemSettings settings = getSettings();
        String backupPath = settings.getBackupPath();

        File backupDir = new File(backupPath);
        if (!backupDir.exists()) {
            return new String[0];
        }

        return backupDir.list((dir, name) -> name.startsWith("pet_db_backup_") && name.endsWith(".sqlite"));
    }
}
