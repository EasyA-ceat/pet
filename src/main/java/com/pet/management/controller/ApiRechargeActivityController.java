package com.pet.management.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.RechargeActivity;
import com.pet.management.service.RechargeActivityService;

@RestController
@RequestMapping("/api/recharge-activities")
public class ApiRechargeActivityController {

    private static final Logger logger = LoggerFactory.getLogger(ApiRechargeActivityController.class);

    @Autowired
    private RechargeActivityService rechargeActivityService;

    @GetMapping
    @PreAuthorize("hasAuthority('RECHARGE_READ')")
    public List<RechargeActivity> getAllActivities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("获取储值活动列表 - 用户: {}, 权限: {}", auth.getName(), auth.getAuthorities());
        return rechargeActivityService.findAll();
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('RECHARGE_READ')")
    public List<RechargeActivity> getActiveActivities() {
        return rechargeActivityService.findActiveActivities();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('RECHARGE_READ')")
    public ResponseEntity<RechargeActivity> getActivityById(@PathVariable Long id) {
        return rechargeActivityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('RECHARGE_WRITE')")
    public ResponseEntity<?> createActivity(@RequestBody RechargeActivity activity) {
        try {
            RechargeActivity saved = rechargeActivityService.save(activity);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "创建活动失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('RECHARGE_WRITE')")
    public ResponseEntity<?> updateActivity(@PathVariable Long id, @RequestBody RechargeActivity activity) {
        try {
            return rechargeActivityService.findById(id).map(existingActivity -> {
                if (activity.getName() != null) {
                    existingActivity.setName(activity.getName());
                }
                if (activity.getMinAmount() != null) {
                    existingActivity.setMinAmount(activity.getMinAmount());
                }
                if (activity.getBonusAmount() != null) {
                    existingActivity.setBonusAmount(activity.getBonusAmount());
                }
                if (activity.getStartDate() != null) {
                    existingActivity.setStartDate(activity.getStartDate());
                }
                if (activity.getEndDate() != null) {
                    existingActivity.setEndDate(activity.getEndDate());
                }
                if (activity.getIsActive() != null) {
                    existingActivity.setIsActive(activity.getIsActive());
                }
                RechargeActivity saved = rechargeActivityService.save(existingActivity);
                return ResponseEntity.ok(saved);
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "更新活动失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('RECHARGE_WRITE')")
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        try {
            rechargeActivityService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "删除活动失败: " + e.getMessage()));
        }
    }
}
