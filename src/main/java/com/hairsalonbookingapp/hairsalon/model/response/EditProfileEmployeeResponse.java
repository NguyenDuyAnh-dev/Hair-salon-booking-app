package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class EditProfileEmployeeResponse {

    private String employeeId;

    private String name;

    private String img;

    private String email;

    private String phoneNumber;

    private String stylistLevel;

    private Integer targetKPI;
    private Integer KPI;
}
