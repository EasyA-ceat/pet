package com.pet.management;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.pet.management")
public class PetManagementSystem {

    public PetManagementSystem() {
    }

    public static void main(String[] args) {
        // 这个方法由JavaFX的MainApplication间接调用
        // 实际的Spring Boot应用会在init()方法中启动
    }
}
