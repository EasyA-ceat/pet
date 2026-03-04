
package com.pet.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pet.management.model.Clerk;
import com.pet.management.service.ClerkService;

@RestController
@RequestMapping("/api/clerks")
public class ApiClerkController {

    @Autowired
    private ClerkService clerkService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Clerk> getAllClerks() {
        return clerkService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Clerk getClerkById(@PathVariable Long id) {
        return clerkService.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Clerk createClerk(@RequestBody Clerk clerk) {
        return clerkService.save(clerk);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Clerk updateClerk(@PathVariable Long id, @RequestBody Clerk clerk) {
        clerk.setId(id);
        return clerkService.save(clerk);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClerk(@PathVariable Long id) {
        clerkService.deleteById(id);
    }
}
