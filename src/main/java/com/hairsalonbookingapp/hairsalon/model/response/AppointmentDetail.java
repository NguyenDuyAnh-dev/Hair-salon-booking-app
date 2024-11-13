package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentDetail {
    private long id;
    private double totalCost;
    private String date;
    private String startHour;
    private String customer;  // USERNAME
    private String stylist;
    private List<String> service;
    private boolean isCompleted;
    private double stylistFee;
    private String discountCode;
    private String status;
}
