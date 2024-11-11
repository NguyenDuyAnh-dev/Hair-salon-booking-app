package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.entity.ShiftEmployee;
import com.hairsalonbookingapp.hairsalon.model.response.AvailableSlot;
import com.hairsalonbookingapp.hairsalon.model.response.ShiftEmployeeResponse;
import com.hairsalonbookingapp.hairsalon.model.request.StylistShiftRequest;
import com.hairsalonbookingapp.hairsalon.model.response.AccountForEmployeeResponse;
import com.hairsalonbookingapp.hairsalon.model.response.ShiftEmployeeResponsePage;
import com.hairsalonbookingapp.hairsalon.service.ShiftEmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shiftEmployee")
@SecurityRequirement(name = "api")
public class ShiftEmployeeAPI {

    @Autowired
    ShiftEmployeeService shiftEmployeeService;

    /*// [STYLIST]
    @PostMapping("/shiftEmployee/{day}")
    public ResponseEntity createShiftEmployee(@PathVariable String day){
        ShiftEmployeeResponse shift = shiftEmployeeService.createNewShiftEmployee(day);
        return ResponseEntity.ok(shift);
    }

    // [STYLIST]
    @DeleteMapping("/shiftEmployee/{day}")
    public ResponseEntity deleteShiftEmployee(@PathVariable String day){
        List<String> shifts = shiftEmployeeService.deleteShiftEmployee(day);
        return ResponseEntity.ok(shifts);
    }

    // [STYLIST]
    @PutMapping("/shiftEmployee/restart/{day}")
    public ResponseEntity restartShiftEmployee(@PathVariable String day){
        ShiftEmployeeResponse shift = shiftEmployeeService.restartShiftEmployee(day);
        return ResponseEntity.ok(shift);
    }

    // [STYLIST]
    @GetMapping("/shiftEmployee")
    public ResponseEntity getAllShiftEmployee(){
        List<ShiftEmployeeResponse> shiftEmployeeList = shiftEmployeeService.getEmployeeShift();
        return ResponseEntity.ok(shiftEmployeeList);
    }

    // [MANAGER]
    @PutMapping("/shiftEmployee/completeAll/{day}")
    public ResponseEntity completeAllShiftEmployeeInDay(@PathVariable String day){
        List<String> shift = shiftEmployeeService.completeAllShiftEmployeeInDay(day);
        return ResponseEntity.ok(shift);
    }

    // [CUSTOMER]
    @GetMapping("/availableShiftEmployee/{stylistId}")
    public ResponseEntity getAvailableShiftEmployee(@PathVariable String stylistId){
        List<ShiftEmployeeResponse> shiftEmployeeList = shiftEmployeeService.getAvailableShiftEmployees(stylistId);
        return ResponseEntity.ok(shiftEmployeeList);
    }*/

    @PostMapping("/register")
    public ResponseEntity registerShift(@Valid @RequestBody StylistShiftRequest stylistShiftRequest){
        AccountForEmployeeResponse accountResponseForEmployee = shiftEmployeeService.registerShifts(stylistShiftRequest);
        return ResponseEntity.ok(accountResponseForEmployee);
    }

    @PostMapping("/createAll")
    public ResponseEntity generateAllShift(){
        List<ShiftEmployeeResponse> shiftEmployeeResponseList = shiftEmployeeService.generateAllShiftEmployees();
        return ResponseEntity.ok(shiftEmployeeResponseList);
    }

    @GetMapping
    public ResponseEntity getAllShifts(){
        List<ShiftEmployee> shiftEmployeeList = shiftEmployeeService.getAllShift();
        return ResponseEntity.ok(shiftEmployeeList);
    }


    @GetMapping("/available/{date}")
    public ResponseEntity getAvailableShifts(@PathVariable String date){
        List<AvailableSlot> availableSlotList = shiftEmployeeService.getAllAvailableSlots(date);
        return ResponseEntity.ok(availableSlotList);
    }

    @GetMapping("/slot/{date}/{hour}")
    public ResponseEntity getAvailableShiftsByHour(@PathVariable String date, @PathVariable String hour){
        List<AvailableSlot> availableSlotList = shiftEmployeeService.getAllAvailableSlotsByHour(hour, date);
        return ResponseEntity.ok(availableSlotList);
    }

    @GetMapping("/staff/{startDate}")
    public ResponseEntity getAllShiftsInWeek(@PathVariable String startDate, @RequestParam int page, @RequestParam int pageSize){
        ShiftEmployeeResponsePage shiftEmployeeResponsePage = shiftEmployeeService.getAllShiftEmployeesInWeek(startDate, page, pageSize);
        return ResponseEntity.ok(shiftEmployeeResponsePage);
    }

    @PostMapping("/tempShift")
    public ResponseEntity createTempShift(@RequestParam String stylistId, @RequestParam String date){
        ShiftEmployeeResponse shiftEmployeeResponse = shiftEmployeeService.createTempShift(stylistId, date);
        return ResponseEntity.ok(shiftEmployeeResponse);
    }

    @GetMapping("/stylist/{startDate}")
    public ResponseEntity getAllShiftsInWeekByStylist(@PathVariable String startDate, @RequestParam int page, @RequestParam int pageSize){
        ShiftEmployeeResponsePage shiftEmployeeResponsePage = shiftEmployeeService.getAllShiftEmployeesInWeekByStylist(startDate, page, pageSize);
        return ResponseEntity.ok(shiftEmployeeResponsePage);
    }

    @GetMapping("/staff/stylistId")
    public ResponseEntity getAllShiftsInWeekOfStylistByStaff(@RequestParam String stylistId, @RequestParam String startDate, @RequestParam int page, @RequestParam int pageSize){
        ShiftEmployeeResponsePage shiftEmployeeResponsePage = shiftEmployeeService.getAllShiftEmployeesInWeekByStaff(stylistId, startDate, page, pageSize);
        return ResponseEntity.ok(shiftEmployeeResponsePage);
    }

}
