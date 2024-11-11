package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*") // cho phep tat ca truy cap, ket noi FE va BE vs nhau
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class DashboardAPI {

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity getDashboardStats(){
        Map<String, Object> stats = dashboardService.getDashbroadStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity getMonthlyRevenue(){
        Map<String, Object> revenueMonthly = dashboardService.getMonthlyRevenue();
        return ResponseEntity.ok(revenueMonthly);
    }
}
