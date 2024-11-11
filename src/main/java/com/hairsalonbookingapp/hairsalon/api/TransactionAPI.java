package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.request.CompleteAppointmentRequest;
import com.hairsalonbookingapp.hairsalon.model.request.RequestTransaction;
import com.hairsalonbookingapp.hairsalon.model.response.TransactionListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.TransactionResponse;
import com.hairsalonbookingapp.hairsalon.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin("*")
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class TransactionAPI {
    @Autowired
    TransactionService transactionService;

//    @PostMapping
////    @PreAuthorize("hasAuthority('customer')")
//    public ResponseEntity createTransaction(@Valid @RequestBody CompleteAppointmentRequest completeAppointmentRequest){
//        TransactionResponse transactionResponse = transactionService.createTransactionInCast(completeAppointmentRequest);
//        return ResponseEntity.ok(transactionResponse);
//    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteTransaction(@PathVariable int id){
        TransactionResponse transactionResponse = transactionService.deleteTransaction(id);
        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping
    public ResponseEntity getAllTransaction(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
        TransactionListResponse transactions = transactionService.getAllTransaction(page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("{id}")
    public ResponseEntity getTransactionInfo(@PathVariable int id){
        TransactionResponse transactionResponse = transactionService.getInfoTransaction(id);
        return ResponseEntity.ok(transactionResponse);
    }
}
