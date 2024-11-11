package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class AccountForEmployeeResponse {
    private String employeeId;
    private String username;
    private String name;
    private String img;
    private String email;
    private String phoneNumber;
    private String role;
    private String token;
    private String stylistLevel;
    private long stylistSelectionFee;
    private int targetKPI;
    private int KPI;
    private Date createdAt;
    private String Status;
    private boolean isDeleted;
    private String days;
}
