package com.pet.management.config;

import com.pet.management.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private SystemSettingsService systemSettingsService;

    // 每天凌晨2点执行自动备份
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledAutoBackup() {
        systemSettingsService.autoBackup();
    }
}
