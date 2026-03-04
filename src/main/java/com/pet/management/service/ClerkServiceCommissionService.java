package com.pet.management.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.management.model.Clerk;
import com.pet.management.model.ClerkServiceCommission;
import com.pet.management.model.ServiceType;
import com.pet.management.repository.ClerkServiceCommissionRepository;

@Service
@Transactional
public class ClerkServiceCommissionService {

    @Autowired
    private ClerkServiceCommissionRepository clerkServiceCommissionRepository;

    @Autowired
    private ClerkService clerkService;

    public List<ClerkServiceCommission> findAll() {
        return clerkServiceCommissionRepository.findAll();
    }

    public Optional<ClerkServiceCommission> findById(Long id) {
        return clerkServiceCommissionRepository.findById(id);
    }

    public List<ClerkServiceCommission> findByClerkId(Long clerkId) {
        return clerkServiceCommissionRepository.findByClerkId(clerkId);
    }

    public Optional<ClerkServiceCommission> findByClerkIdAndServiceType(Long clerkId, ServiceType serviceType) {
        return clerkServiceCommissionRepository.findByClerkIdAndServiceType(clerkId, serviceType);
    }

    public BigDecimal getCommissionRate(Long clerkId, ServiceType serviceType) {
        // 先查找特定服务类型的抽成比例
        Optional<ClerkServiceCommission> specificCommission = findByClerkIdAndServiceType(clerkId, serviceType);
        if (specificCommission.isPresent()) {
            return specificCommission.get().getCommissionRate();
        }
        
        // 如果没有找到特定的，使用员工的默认抽成比例
        return clerkService.findById(clerkId)
                .map(Clerk::getCommissionRate)
                .orElse(BigDecimal.valueOf(0.05)); // 默认5%
    }

    public ClerkServiceCommission save(ClerkServiceCommission commission) {
        return clerkServiceCommissionRepository.save(commission);
    }

    public ClerkServiceCommission setCommission(Long clerkId, ServiceType serviceType, BigDecimal rate) {
        Clerk clerk = clerkService.findById(clerkId)
                .orElseThrow(() -> new IllegalArgumentException("Clerk not found"));

        Optional<ClerkServiceCommission> existing = findByClerkIdAndServiceType(clerkId, serviceType);
        
        ClerkServiceCommission commission;
        if (existing.isPresent()) {
            commission = existing.get();
            commission.setCommissionRate(rate);
        } else {
            commission = new ClerkServiceCommission(clerk, serviceType, rate);
        }
        
        return save(commission);
    }

    public void deleteById(Long id) {
        clerkServiceCommissionRepository.deleteById(id);
    }

    public void deleteByClerkId(Long clerkId) {
        clerkServiceCommissionRepository.deleteByClerkId(clerkId);
    }
}
