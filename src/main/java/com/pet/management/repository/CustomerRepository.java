package com.pet.management.repository;

import com.pet.management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    List<Customer> findByCustomerNameContaining(String customerName);

    List<Customer> findByPetNameContaining(String petName);

    List<Customer> findByPetType(String petType);

    @Query("SELECT c FROM Customer c WHERE c.phone LIKE CONCAT('%', :phone, '%')")
    List<Customer> findByPhoneContaining(@Param("phone") String phone);

    @Query("SELECT c FROM Customer c LEFT JOIN c.transactions t GROUP BY c.id ORDER BY COUNT(t) DESC")
    List<Customer> findCustomersWithMostTransactions();
}
