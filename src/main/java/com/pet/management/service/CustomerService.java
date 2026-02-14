package com.pet.management.service;

import com.pet.management.model.Customer;
import com.pet.management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return customerRepository.findByCustomerNameContaining(keyword);
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
}
