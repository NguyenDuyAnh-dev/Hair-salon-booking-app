package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.entity.SalaryMonth;
import com.hairsalonbookingapp.hairsalon.model.request.RequestSalaryMonth;
import com.hairsalonbookingapp.hairsalon.model.request.RequestSalaryMonthForAnEmployee;
import com.hairsalonbookingapp.hairsalon.model.response.SalaryMonthListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.SalaryMonthResponse;
import com.hairsalonbookingapp.hairsalon.service.SalaryMonthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salaryMonth")
@CrossOrigin("*")
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class SalaryMonthAPI {
    @Autowired
    SalaryMonthService salaryMonthService;


    @PostMapping("{id}")
//    @PreAuthorize("hasAuthority('customer')")
    public ResponseEntity createSalaryMonth(@Valid @RequestBody RequestSalaryMonthForAnEmployee requestSalaryMonthForAnEmployee){
        SalaryMonthResponse salaryMonthResponse = salaryMonthService.createSalaryMonthForAnEmployee(requestSalaryMonthForAnEmployee);
        return ResponseEntity.ok(salaryMonthResponse);
    }

    @PostMapping
    public ResponseEntity createSalaryMonthForAll(){
        List<SalaryMonthResponse> responses = salaryMonthService.createSalaryForAllEmployees();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteSalaryMonth(@PathVariable int id){
        SalaryMonthResponse salaryMonthResponse = salaryMonthService.deleteSalaryMonth(id);
        return ResponseEntity.ok(salaryMonthResponse);
    }

    @GetMapping
    public ResponseEntity getAllSalaryMonth(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SalaryMonthListResponse salaryMonths = salaryMonthService.getAllSalaryMonth(page, size);
        return ResponseEntity.ok(salaryMonths);
    }

    @GetMapping("/employee")
    public ResponseEntity getAllSalaryMonthOfAnEmployee(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SalaryMonthListResponse salaryMonths = salaryMonthService.getAllSalaryMonthOfAnEmployee(page, size);
        return ResponseEntity.ok(salaryMonths);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity getAllSalaryMonthOfAnEmployee(@PathVariable String id){
        List<SalaryMonth> salaryMonths = salaryMonthService.getAllSalaryMonthOfAnEmployee(id);
        return ResponseEntity.ok(salaryMonths);
    }

    @GetMapping("{id}")
    public ResponseEntity getSalaryMonthInfo(@PathVariable int id){
        SalaryMonthResponse salaryMonthResponse = salaryMonthService.getInfoSalaryMonth(id);
        return ResponseEntity.ok(salaryMonthResponse);
    }
}
