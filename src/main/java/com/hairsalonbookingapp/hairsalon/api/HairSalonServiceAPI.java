package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.request.HairSalonServiceRequest;
import com.hairsalonbookingapp.hairsalon.model.response.HairSalonServiceResponse;
import com.hairsalonbookingapp.hairsalon.model.request.HairSalonServiceUpdate;
import com.hairsalonbookingapp.hairsalon.model.response.HairSalonServiceResponsePage;
import com.hairsalonbookingapp.hairsalon.service.HairSalonBookingAppService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
public class HairSalonServiceAPI {

    @Autowired
    HairSalonBookingAppService hairSalonBookingAppService;

    @PostMapping("/service")
    public ResponseEntity createNewHairSalonService(@Valid @RequestBody HairSalonServiceRequest hairSalonServiceRequest){
        HairSalonServiceResponse hairSalonService = hairSalonBookingAppService.createNewService(hairSalonServiceRequest);
        return ResponseEntity.ok(hairSalonService);
    }

    @PutMapping("/service/{id}")
    public ResponseEntity updateHairSalonService(@Valid @RequestBody HairSalonServiceUpdate hairSalonServiceUpdate, @PathVariable long id){
        HairSalonServiceResponse hairSalonService = hairSalonBookingAppService.updateService(hairSalonServiceUpdate,id);
        return ResponseEntity.ok(hairSalonService);
    }

    @DeleteMapping("/service/{id}")
    public ResponseEntity deleteHairSalonService(@PathVariable long id){
        HairSalonServiceResponse hairSalonService = hairSalonBookingAppService.deleteService(id);
        return ResponseEntity.ok(hairSalonService);
    }

    @PutMapping("/service/restart/{id}")
    public ResponseEntity restartHairSalonService(@PathVariable long id){
        HairSalonServiceResponse hairSalonService = hairSalonBookingAppService.startService(id);
        return ResponseEntity.ok(hairSalonService);
    }

    @GetMapping("/service")
    public ResponseEntity getAllService(@RequestParam int page, @RequestParam(defaultValue = "2") int size){
        HairSalonServiceResponsePage hairSalonServices = hairSalonBookingAppService.getAllService(page, size);
        return ResponseEntity.ok(hairSalonServices);
    }

    @GetMapping("/availableService")
    public ResponseEntity getAllAvailableService(){
        List<HairSalonServiceResponse> hairSalonServices = hairSalonBookingAppService.getAllAvailableService();
        return ResponseEntity.ok(hairSalonServices);
    }
}
