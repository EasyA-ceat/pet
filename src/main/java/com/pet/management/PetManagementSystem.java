package com.pet.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.pet.management.controller",
    "com.pet.management.service",
    "com.pet.management.repository",
    "com.pet.management.model"
})
public class PetManagementSystem {

    public PetManagementSystem() {
    }

    public static void main(String[] args) {
        SpringApplication.run(PetManagementSystem.class, args);
    }
}
