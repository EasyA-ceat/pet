package com.pet.management.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.PaymentMethod;
import com.pet.management.model.ServiceType;

@RestController
@RequestMapping("/api/common")
public class ApiCommonController {

    @GetMapping("/service-types")
    public List<Map<String, String>> getServiceTypes() {
        List<Map<String, String>> result = new ArrayList<>();
        for (ServiceType type : ServiceType.values()) {
            Map<String, String> item = new HashMap<>();
            item.put("value", type.name());
            item.put("label", type.getDisplayName());
            result.add(item);
        }
        return result;
    }

    @GetMapping("/payment-methods")
    public List<Map<String, String>> getPaymentMethods() {
        List<Map<String, String>> result = new ArrayList<>();
        for (PaymentMethod method : PaymentMethod.values()) {
            Map<String, String> item = new HashMap<>();
            item.put("value", method.name());
            item.put("label", method.getDisplayName());
            result.add(item);
        }
        return result;
    }
}
