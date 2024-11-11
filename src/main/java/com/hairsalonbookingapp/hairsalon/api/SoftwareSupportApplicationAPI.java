package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.request.RequestSoftwareSupportApplication;
import com.hairsalonbookingapp.hairsalon.model.request.RequestUpdateSoftwareSupportApplication;
import com.hairsalonbookingapp.hairsalon.model.response.SoftwareSupportApplicationListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.SoftwareSupportApplicationResponse;
import com.hairsalonbookingapp.hairsalon.model.response.UpdateSoftwareSupportApplicationResponse;
import com.hairsalonbookingapp.hairsalon.service.SoftwareSupportApplicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/softwareSupportApplication")
@CrossOrigin("*")
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class SoftwareSupportApplicationAPI {
    @Autowired
    SoftwareSupportApplicationService softwareSupportApplicationService;

    @PostMapping
//    @PreAuthorize("hasAuthority('customer')")
    public ResponseEntity createSoftwareSupportApplication(@Valid @RequestBody RequestSoftwareSupportApplication requestSoftwareSupportApplication){
        SoftwareSupportApplicationResponse softwareSupportApplicationResponse = softwareSupportApplicationService.createSoftwareSupportApplication(requestSoftwareSupportApplication);
        return ResponseEntity.ok(softwareSupportApplicationResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteSoftwareSupportApplication(@PathVariable int id){
        SoftwareSupportApplicationResponse softwareSupportApplicationResponse = softwareSupportApplicationService.deleteSoftwareSupportApplication(id);
        return ResponseEntity.ok(softwareSupportApplicationResponse);
    }

    @GetMapping("/customers")
    public ResponseEntity getAllSoftwareSupportApplicationOfCustomer(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SoftwareSupportApplicationListResponse softwareSupportApplications = softwareSupportApplicationService.getAllSoftwareSupportApplicationOfCustomer(page, size);
        return ResponseEntity.ok(softwareSupportApplications);
    }

    @GetMapping("/customer")
    public ResponseEntity getAllSoftwareSupportApplicationOfAnCustomer(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SoftwareSupportApplicationListResponse softwareSupportApplications = softwareSupportApplicationService.getAllSoftwareSupportApplicationOfAnCustomer(page, size);
        return ResponseEntity.ok(softwareSupportApplications);
    }

    @GetMapping("/employees")
    public ResponseEntity getAllSoftwareSupportApplicationOfEmployee(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SoftwareSupportApplicationListResponse softwareSupportApplications = softwareSupportApplicationService.getAllSoftwareSupportApplicationOfEmployee(page, size);
        return ResponseEntity.ok(softwareSupportApplications);
    }

    @GetMapping("/employee")
    public ResponseEntity getAllSoftwareSupportApplicationOfAnEmployee(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        SoftwareSupportApplicationListResponse softwareSupportApplications = softwareSupportApplicationService.getAllSoftwareSupportApplicationOfAnEmployee(page, size);
        return ResponseEntity.ok(softwareSupportApplications);
    }

    @PutMapping("{id}")
    public ResponseEntity updatedSoftwareSupportApplication(@Valid @RequestBody RequestUpdateSoftwareSupportApplication requestUpdateSoftwareSupportApplication, @PathVariable int id){ //@PathVariable de tim thang id tu FE
        UpdateSoftwareSupportApplicationResponse oldSoftwareSupportApplication = softwareSupportApplicationService.updatedSoftwareSupportApplication(requestUpdateSoftwareSupportApplication, id);
        return ResponseEntity.ok(oldSoftwareSupportApplication);
    }

    @GetMapping("{id}")
    public ResponseEntity getSoftwareSupportApplicationInfo(@PathVariable int id){
        SoftwareSupportApplicationResponse softwareSupportApplicationResponse = softwareSupportApplicationService.getInfoSoftwareSupportApplication(id);
        return ResponseEntity.ok(softwareSupportApplicationResponse);
    }
}

