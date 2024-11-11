package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.response.*;
import com.hairsalonbookingapp.hairsalon.model.request.FindEmployeeRequest;
import com.hairsalonbookingapp.hairsalon.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
public class EmployeeAPI {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity getEmployeeByRole(@Valid @RequestBody FindEmployeeRequest findEmployeeRequest, @RequestParam int page, @RequestParam(defaultValue = "2") int size){
        EmployeeResponsePage employeeResponsePage = employeeService.getEmployeeByRole(findEmployeeRequest, page, size);
        return ResponseEntity.ok(employeeResponsePage);
    }

    @GetMapping("/stylist")
    public ResponseEntity getAllStylist(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
//        List<StylistInfo> stylistInfos = employeeService.getAllAvailableStylist();
//        return ResponseEntity.ok(stylistInfos);
        StylistListResponse stylistListResponse = employeeService.getAllAvailableStylist(page, size);
        return ResponseEntity.ok(stylistListResponse);
    }

    @GetMapping("/employee")
    public ResponseEntity getAllEmployees(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
//        List<EmployeeInfo> employeeInfoList = employeeService.getAllEmployees();
//        return ResponseEntity.ok(employeeInfoList);
        EmployeeResponsePage employeeListResponse = employeeService.getAllEmployees(page, size);
        return ResponseEntity.ok(employeeListResponse);
    }

    @GetMapping("/stylist/workDayNull")
    public ResponseEntity checkStylistHasNull(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
//        List<String> stylistIdList = employeeService.getStylistsThatWorkDaysNull();
//        return ResponseEntity.ok(stylistIdList);
        StylistWorkDayNullListResponse stylistWorkDayNullListResponse = employeeService.getStylistsThatWorkDaysNull(page, size);
        return ResponseEntity.ok(stylistWorkDayNullListResponse);
    }

    @GetMapping("/employee/deleted")
    public ResponseEntity getAllBanedEmployees(@RequestParam int page, @RequestParam(defaultValue = "2") int size){
        EmployeeResponsePage employeeResponsePage = employeeService.getAllBanedEmployees(page, size);
        return ResponseEntity.ok(employeeResponsePage);
    }

    @PutMapping("/employee/restart/{id}")
    public ResponseEntity restartAccountEmployee(@PathVariable String id){
        EmployeeInfo employeeInfo = employeeService.restartEmployee(id);
        return ResponseEntity.ok(employeeInfo);
    }

}
