package com.hairsalonbookingapp.hairsalon.model.request;

import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
public class RequestUpdateDiscountCode {
    @Id
    @Column(unique = true, nullable = false)
    private String discountCodeId;

    @ManyToOne
    @JoinColumn(name = "discountProgramId", nullable = false) // day la foreign key
    private DiscountProgram discountProgram;

    private  String appointmentId;

}
