package com.pet.management.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.management.model.Customer;
import com.pet.management.model.PaymentMethod;
import com.pet.management.model.RechargeActivity;
import com.pet.management.model.RechargeRecord;
import com.pet.management.repository.RechargeRecordRepository;

@Service
@Transactional
public class RechargeRecordService {

    @Autowired
    private RechargeRecordRepository rechargeRecordRepository;

    @Autowired
    private RechargeActivityService rechargeActivityService;

    @Autowired
    private CustomerService customerService;

    public List<RechargeRecord> findAll() {
        return rechargeRecordRepository.findAll();
    }

    public Optional<RechargeRecord> findById(Long id) {
        return rechargeRecordRepository.findById(id);
    }

    public List<RechargeRecord> findByCustomerId(Long customerId) {
        return rechargeRecordRepository.findByCustomerIdOrderByCreateTimeDesc(customerId);
    }

    public List<RechargeRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rechargeRecordRepository.findByCreateTimeBetweenOrderByCreateTimeDesc(startDate, endDate);
    }

    public RechargeRecord recharge(Long customerId, BigDecimal amount, PaymentMethod paymentMethod, String notes) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // 查找适用的充值活动
        Optional<RechargeActivity> bestActivity = rechargeActivityService.findBestApplicableActivity(amount);
        BigDecimal bonusAmount = bestActivity.map(RechargeActivity::getBonusAmount).orElse(BigDecimal.ZERO);

        // 更新顾客余额和累计充值
        customer.setBalance(customer.getBalance().add(amount).add(bonusAmount));
        customer.setTotalRecharge(customer.getTotalRecharge().add(amount));
        customer.setIsVip(true); // 充值后自动成为VIP
        customerService.save(customer);

        // 创建充值记录
        RechargeRecord record = new RechargeRecord();
        record.setCustomer(customer);
        record.setAmount(amount);
        record.setBonusAmount(bonusAmount);
        record.setPaymentMethod(paymentMethod);
        record.setNotes(notes);
        bestActivity.ifPresent(record::setActivity);

        return rechargeRecordRepository.save(record);
    }

    public RechargeRecord save(RechargeRecord record) {
        return rechargeRecordRepository.save(record);
    }

    public void deleteById(Long id) {
        rechargeRecordRepository.deleteById(id);
    }
}
