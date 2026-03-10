package com.pet.management.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pet.management.model.Customer;
import com.pet.management.model.Photo;
import com.pet.management.service.CustomerService;
import com.pet.management.service.PhotoService;

@RestController
@RequestMapping("/api/customers")
public class ApiCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ApiCustomerController.class);

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private PhotoService photoService;

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public List<Customer> getAllCustomers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("获取顾客列表 - 用户: {}, 权限: {}", auth.getName(), auth.getAuthorities());
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerData) {
        // 先从数据库获取现有实体
        Customer existingCustomer = customerService.findById(id).orElse(null);
        if (existingCustomer == null) {
            return null;
        }
        
        // 只更新基本字段，不替换集合
        existingCustomer.setCustomerName(customerData.getCustomerName());
        existingCustomer.setPhone(customerData.getPhone());
        existingCustomer.setPetName(customerData.getPetName());
        existingCustomer.setPetType(customerData.getPetType());
        existingCustomer.setPetBreed(customerData.getPetBreed());
        existingCustomer.setPetAge(customerData.getPetAge());
        existingCustomer.setNotes(customerData.getNotes());
        
        return customerService.save(existingCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public List<Customer> searchCustomers(@RequestParam String keyword) {
        return customerService.search(keyword);
    }
    
    // 统计信息
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public ResponseEntity<Map<String, Object>> getCustomerStats() {
        List<Customer> allCustomers = customerService.findAll();
        Map<String, Object> stats = new HashMap<>();
        
        // 总顾客数
        stats.put("totalCustomers", allCustomers.size());
        
        // 本月新增顾客
        int recentCount = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        
        for (Customer customer : allCustomers) {
            if (customer.getCreateTime().isAfter(startOfMonth)) {
                recentCount++;
            }
        }
        stats.put("recentCustomers", recentCount);
        
        // 活跃顾客（简单处理为总数）
        stats.put("activeCustomers", allCustomers.size());
        
        return ResponseEntity.ok(stats);
    }
    
    // 照片管理
    @PostMapping("/{customerId}/photos")
    public ResponseEntity<Photo> uploadPhoto(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "photoType", defaultValue = "other") String photoType,
            @RequestParam(value = "notes", required = false) String notes) {
        try {
            // 将MultipartFile转换为File
            File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            
            Photo photo = photoService.uploadPhoto(tempFile, customerId, null, photoType, notes);
            
            // 删除临时文件
            tempFile.delete();
            
            return ResponseEntity.ok(photo);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{customerId}/photos")
    public List<Photo> getCustomerPhotos(@PathVariable Long customerId) {
        return photoService.findByCustomerId(customerId);
    }
    
    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        photoService.deleteById(photoId);
        return ResponseEntity.ok().build();
    }
}
