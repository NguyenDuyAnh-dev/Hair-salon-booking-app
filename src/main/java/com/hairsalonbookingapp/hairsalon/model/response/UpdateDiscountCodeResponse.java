package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class UpdateDiscountCodeResponse {
    @Id
    @Column(unique = true, nullable = false)
    private String discountCodeId;

    @ManyToOne
    @JoinColumn(name = "discountProgramId", nullable = false) // day la foreign key
    private DiscountProgram discountProgram;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = false) // day la foreign key
    private AccountForCustomer customer;

    private  String appointmentId;

    private boolean isDeleted = false;
}
