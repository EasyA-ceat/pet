package com.pet.management.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Clerk;
import com.pet.management.model.ClerkServiceCommission;
import com.pet.management.model.ServiceType;
import com.pet.management.repository.ClerkRepository;
import com.pet.management.service.ClerkServiceCommissionService;

@RestController
@RequestMapping("/api/clerk-service-commissions")
public class ApiClerkServiceCommissionController {

    @Autowired
    private ClerkServiceCommissionService commissionService;
    
    @Autowired
    private ClerkRepository clerkRepository;

    @GetMapping
    public List<ClerkServiceCommission> getAllCommissions() {
        return commissionService.findAll();
    }

    @GetMapping("/clerk/{clerkId}")
    public List<ClerkServiceCommission> getCommissionsByClerkId(@PathVariable Long clerkId) {
        return commissionService.findByClerkId(clerkId);
    }

    @GetMapping("/{id}")
    public ClerkServiceCommission getCommissionById(@PathVariable Long id) {
        return commissionService.findById(id).orElse(null);
    }

    @PostMapping
    public ClerkServiceCommission createCommission(@RequestBody Map<String, Object> payload) {
        Long clerkId = Long.valueOf(payload.get("clerkId").toString());
        ServiceType serviceType = ServiceType.valueOf(payload.get("serviceType").toString());
        BigDecimal commissionRate = new BigDecimal(payload.get("commissionRate").toString());
        
        return commissionService.setCommission(clerkId, serviceType, commissionRate);
    }

    @PutMapping("/{id}")
    public ClerkServiceCommission updateCommission(@PathVariable Long id, @RequestBody ClerkServiceCommission commission) {
        commission.setId(id);
        return commissionService.save(commission);
    }

    @DeleteMapping("/{id}")
    public void deleteCommission(@PathVariable Long id) {
        commissionService.deleteById(id);
    }
    
    // 获取员工某服务类型的抽成比例（含默认值）
    @GetMapping("/rate")
    public BigDecimal getCommissionRate(
            @RequestParam Long clerkId, 
            @RequestParam ServiceType serviceType) {
        return commissionService.getCommissionRate(clerkId, serviceType);
    }
}
