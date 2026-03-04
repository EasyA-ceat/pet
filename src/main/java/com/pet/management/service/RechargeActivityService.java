package com.pet.management.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.management.model.RechargeActivity;
import com.pet.management.repository.RechargeActivityRepository;

@Service
@Transactional
public class RechargeActivityService {

    @Autowired
    private RechargeActivityRepository rechargeActivityRepository;

    public List<RechargeActivity> findAll() {
        return rechargeActivityRepository.findAll();
    }

    public Optional<RechargeActivity> findById(Long id) {
        return rechargeActivityRepository.findById(id);
    }

    public List<RechargeActivity> findActiveActivities() {
        return rechargeActivityRepository.findByIsActiveTrue();
    }

    public Optional<RechargeActivity> findBestApplicableActivity(BigDecimal amount) {
        return rechargeActivityRepository.findFirstByIsActiveTrueAndMinAmountLessThanEqualOrderByMinAmountDesc(amount);
    }

    public List<RechargeActivity> findApplicableActivities(BigDecimal amount) {
        return rechargeActivityRepository.findApplicableActivities(LocalDateTime.now(), amount);
    }

    public RechargeActivity save(RechargeActivity activity) {
        return rechargeActivityRepository.save(activity);
    }

    public void deleteById(Long id) {
        rechargeActivityRepository.deleteById(id);
    }
}
