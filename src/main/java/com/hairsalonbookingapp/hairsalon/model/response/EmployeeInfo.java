package com.hairsalonbookingapp.hairsalon.model.response;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeInfo {
    String id;
    String username;
    String name;
    String img;
    String email;
    String phoneNumber;
    private String Status;
    double basicSalary;
    Date createdAt;
    String role;
    String stylistLevel; // [Stylist]
    private long stylistSelectionFee;
    private int targetKPI;
    int KPI; // KPI của stylist // [Stylist]
    String status;
    boolean isDeleted;

    private Double CommessionOverratedFromKPI;


    private Double FineUnderatedFromKPI;
    String days;
}
