package com.pet.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private RechargeActivityService rechargeActivityService;

    @GetMapping
    public List<RechargeActivity> getAllActivities() {
        return rechargeActivityService.findAll();
    }

    @GetMapping("/active")
    public List<RechargeActivity> getActiveActivities() {
        return rechargeActivityService.findActiveActivities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RechargeActivity> getActivityById(@PathVariable Long id) {
        return rechargeActivityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody RechargeActivity activity) {
        try {
            RechargeActivity saved = rechargeActivityService.save(activity);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "创建活动失败: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<?> deleteActivity(@PathVariable Long id) {
        try {
            rechargeActivityService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "删除活动失败: " + e.getMessage()));
        }
    }
}
