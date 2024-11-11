package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.response.CustomerAccountInfo;
import com.hairsalonbookingapp.hairsalon.model.response.CustomerResponsePage;
import com.hairsalonbookingapp.hairsalon.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
public class CustomerAPI {

    @Autowired
    CustomerService customerService;

    @GetMapping("/customer")
    public ResponseEntity getAllCustomers(@RequestParam int page, @RequestParam(defaultValue = "2") int size){
        CustomerResponsePage customerResponsePage = customerService.getAllCustomerAccounts(page, size);
        return ResponseEntity.ok(customerResponsePage);
    }

    @GetMapping("/deleted")
    public ResponseEntity getAllBanedCustomers(@RequestParam int page, @RequestParam(defaultValue = "2") int size){
        CustomerResponsePage customerResponsePage = customerService.getAllBanedCustomerAccounts(page, size);
        return ResponseEntity.ok(customerResponsePage);
    }

    @PutMapping("/restart/{phone}")
    public ResponseEntity restartAccountCustomer(@PathVariable String phone){
        CustomerAccountInfo customerAccountInfo = customerService.restartCustomer(phone);
        return ResponseEntity.ok(customerAccountInfo);
    }

}
