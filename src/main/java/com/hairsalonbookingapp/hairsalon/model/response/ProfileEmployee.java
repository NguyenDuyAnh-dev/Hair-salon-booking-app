package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileEmployee {
    String id;
    String username;
    String name;
    String img;
    String email;
    String phoneNumber;
    double basicSalary;
    Date createdAt;
    String role;
    String stylistLevel; // [Stylist]
    private Long stylistSelectionFee;
    int KPI; // KPI của stylist // [Stylist]
    int targetKPI;
    String days;
}
