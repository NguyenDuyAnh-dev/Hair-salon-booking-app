package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.request.ShiftWeekRequest;
import com.hairsalonbookingapp.hairsalon.model.response.ShiftWeekResponse;
import com.hairsalonbookingapp.hairsalon.model.request.ShiftWeekUpdate;
import com.hairsalonbookingapp.hairsalon.service.ShiftWeekService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
public class ShiftWeekAPI {

    @Autowired
    ShiftWeekService shiftWeekService;

    // [MANAGER]
    @PostMapping("/shiftInWeek")
    public ResponseEntity createNewShiftWeek(@Valid @RequestBody ShiftWeekRequest shiftWeekRequest){
        ShiftWeekResponse shift = shiftWeekService.createWeekShift(shiftWeekRequest);
        return ResponseEntity.ok(shift);
    }

    // [MANAGER]
    @PutMapping("/shiftInWeek/{day}")
    public ResponseEntity updateShiftInWeek(@Valid @RequestBody ShiftWeekUpdate shiftWeekUpdate, @PathVariable String day){
        ShiftWeekResponse shift = shiftWeekService.updateShift(shiftWeekUpdate, day);
        return ResponseEntity.ok(shift);
    }

    // [MANAGER]
    @DeleteMapping("/shiftInWeek/{day}")
    public ResponseEntity deleteShiftInWeek(@PathVariable String day){
        ShiftWeekResponse shift = shiftWeekService.deleteShift(day);
        return ResponseEntity.ok(shift);
    }

    // [MANAGER]
    /*@PutMapping("/shiftInWeek/restart/{day}")
    public ResponseEntity restartShiftInWeek(@PathVariable String day){
        ShiftWeekResponse shift = shiftWeekService.restartShift(day);
        return ResponseEntity.ok(shift);
    }*/  // API DỰ PHÒNG TRONG TRƯỜNG HỢP NHẬP TẦM BẬY

    // [MANAGER]
    @GetMapping("/shiftInWeek")
    public ResponseEntity getAllShiftInWeek(){
        List<ShiftWeekResponse> shiftInWeekList = shiftWeekService.getAllShift();
        return ResponseEntity.ok(shiftInWeekList);
    }

    /*@GetMapping("/shiftInWeek/day")
    public ResponseEntity getshiftInWeekByday(@PathVariable String day){
        ShiftInWeek shift = shiftWeekService.getShift(day);
        return ResponseEntity.ok(shift);
    }*/

    // [MANAGER]
    @PutMapping("/shiftInWeek")
    public ResponseEntity updateAllShiftInWeeks(@Valid @RequestBody ShiftWeekUpdate shiftWeekUpdate){
        List<ShiftWeekResponse> shift = shiftWeekService.updateAllShiftInWeeks(shiftWeekUpdate);
        return ResponseEntity.ok(shift);
    }

}
