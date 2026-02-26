package com.pet.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.management.model.Customer;
import com.pet.management.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return customerRepository.findAll();
        }
        // 搜索多个字段：顾客姓名、电话、宠物名
        List<Customer> byCustomerName = customerRepository.findByCustomerNameContaining(keyword);
        List<Customer> byPhone = customerRepository.findByPhoneContaining(keyword);
        List<Customer> byPetName = customerRepository.findByPetNameContaining(keyword);
        
        // 合并结果并去重
        java.util.Set<Customer> result = new java.util.HashSet<>();
        result.addAll(byCustomerName);
        result.addAll(byPhone);
        result.addAll(byPetName);
        
        return new java.util.ArrayList<>(result);
    }

    public List<Customer> findByCustomerNameContaining(String customerName) {
        return customerRepository.findByCustomerNameContaining(customerName);
    }

    public List<Customer> findByPetNameContaining(String petName) {
        return customerRepository.findByPetNameContaining(petName);
    }

    public List<Customer> findByPetType(String petType) {
        return customerRepository.findByPetType(petType);
    }

    public List<Customer> findByPhoneContaining(String phone) {
        return customerRepository.findByPhoneContaining(phone);
    }

    public List<Customer> findCustomersWithMostTransactions() {
        return customerRepository.findCustomersWithMostTransactions();
    }

    public long count() {
        return customerRepository.count();
    }
}
