package com.pet.management.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.PaymentMethod;
import com.pet.management.model.RechargeRecord;
import com.pet.management.repository.CustomerRepository;
import com.pet.management.repository.RechargeActivityRepository;
import com.pet.management.service.RechargeRecordService;

@RestController
@RequestMapping("/api/recharge-records")
public class ApiRechargeRecordController {

    @Autowired
    private RechargeRecordService rechargeRecordService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RechargeActivityRepository rechargeActivityRepository;

    @GetMapping
    public List<RechargeRecord> getAllRecords() {
        return rechargeRecordService.findAll();
    }

    @GetMapping("/customer/{customerId}")
    public List<RechargeRecord> getRecordsByCustomerId(@PathVariable Long customerId) {
        return rechargeRecordService.findByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public RechargeRecord getRecordById(@PathVariable Long id) {
        return rechargeRecordService.findById(id).orElse(null);
    }

    @PostMapping
    public RechargeRecord createRecord(@RequestBody Map<String, Object> payload) {
        // 获取客户ID
        Long customerId = Long.valueOf(payload.get("customerId").toString());
        
        // 获取充值金额
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        
        // 获取支付方式
        PaymentMethod paymentMethod = PaymentMethod.valueOf(payload.get("paymentMethod").toString());
        
        // 获取备注
        String notes = payload.containsKey("notes") ? (String) payload.get("notes") : null;
        
        return rechargeRecordService.recharge(customerId, amount, paymentMethod, notes);
    }
    
    @PostMapping("/recharge")
    public RechargeRecord recharge(@RequestBody Map<String, Object> payload) {
        // 获取客户ID
        Long customerId = Long.valueOf(payload.get("customerId").toString());
        
        // 获取充值金额
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        
        // 获取支付方式
        PaymentMethod paymentMethod = PaymentMethod.valueOf(payload.get("paymentMethod").toString());
        
        // 获取备注
        String notes = payload.containsKey("notes") ? (String) payload.get("notes") : null;
        
        return rechargeRecordService.recharge(customerId, amount, paymentMethod, notes);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        rechargeRecordService.deleteById(id);
    }
}
