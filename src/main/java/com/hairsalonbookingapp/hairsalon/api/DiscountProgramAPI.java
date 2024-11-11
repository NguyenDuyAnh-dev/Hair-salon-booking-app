package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.request.RequestDiscountprogram;
import com.hairsalonbookingapp.hairsalon.model.request.RequestUpdateDiscountProgram;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramInfoResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramResponse;
import com.hairsalonbookingapp.hairsalon.model.response.UpdateDiscountProgramResponse;
import com.hairsalonbookingapp.hairsalon.service.DiscountProgramService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discountProgram")
@CrossOrigin("*")
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class DiscountProgramAPI {
    @Autowired
    DiscountProgramService discountProgramService;
    @PostMapping
    public ResponseEntity createDiscountProgram(@Valid @RequestBody RequestDiscountprogram requestDiscountprogram){
        DiscountProgramResponse discountProgramResponse = discountProgramService.createDiscountProgram(requestDiscountprogram);
        return ResponseEntity.ok(discountProgramResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteDiscountProgram(@PathVariable int id){
        DiscountProgramResponse discountProgramResponse = discountProgramService.deleteDiscountProgram(id);
        return ResponseEntity.ok(discountProgramResponse);
    }

    @GetMapping("/manager")
    public ResponseEntity getAllDiscountProgram(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        DiscountProgramListResponse discountPrograms = discountProgramService.getAllDiscountProgram(page, size);
        return ResponseEntity.ok(discountPrograms);
    }

    @GetMapping
    public ResponseEntity getAllDiscountProgramInCustomer(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        DiscountProgramListResponse discountPrograms = discountProgramService.getAllDiscountProgramInCustomer(page, size);
        return ResponseEntity.ok(discountPrograms);
    }

    // update profile cua customer
    @PutMapping("{id}")
    public ResponseEntity updatedDiscountProgram(@Valid @RequestBody RequestUpdateDiscountProgram requestUpdateDiscountProgram, @PathVariable int id){ //@PathVariable de tim thang id tu FE
        UpdateDiscountProgramResponse oldDiscountProgram = discountProgramService.updatedDiscountProgram(requestUpdateDiscountProgram, id);
        return ResponseEntity.ok(oldDiscountProgram);
    }

    @GetMapping("{id}")
    public ResponseEntity getDiscountProgramInfo(@PathVariable int id){
        DiscountProgramInfoResponse discountProgram = discountProgramService.getInfoDiscountProgram(id);
        return ResponseEntity.ok(discountProgram);
    }

    @PutMapping("/discountProgram/start/{id}")
    public ResponseEntity startDiscountProgram(@PathVariable int id){
        DiscountProgramResponse discountProgram = discountProgramService.startProgram(id);
        return ResponseEntity.ok(discountProgram);
    }

    @DeleteMapping("/discountProgram/end/{id}")
    public ResponseEntity endDiscountProgram(@PathVariable int id){
        DiscountProgramResponse discountProgram = discountProgramService.endProgram(id);
        return ResponseEntity.ok(discountProgram);
    }
}
