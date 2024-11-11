package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import lombok.Data;

@Data
public class DiscountCodeResponse {
    private String discountCodeId;

    private String programName;

    private String customerName;

    private double percentage;

    private  String appointmentId;
}
