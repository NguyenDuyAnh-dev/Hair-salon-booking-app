package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompleteAppointmentRequest {
    String stylistId;
    String startSlot;
    String date;
//    private long appointmentId;
    @NotNull
    private String paymentType; // "VNPay" hoặc "Cash"
}
