package com.hairsalonbookingapp.hairsalon.model.response;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

@Data
public class EditSalaryEmployeeResponse {
    @Id
    @Column(unique = true, nullable = false)
    private String employeeId;

    @NotBlank(message = "Name can not blank!", groups = CreatedBy.class) //ko cho de trong, neu de trong se hien messsage "Name can not blank!"
    private String name;

    @NotBlank(message = "Username can not blank!", groups = CreatedBy.class) //groups = CreatedBy.class chi co nhom create moi bat loi
    @Size(min = 6, message = "Username must be more than 6 characters")
    @Pattern(regexp = "^[\\S]*$", message = "Username must not contain spaces")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=\\-.,<>?/]+$", message = "Invalid characters") //cho phép  ký tự đặc biệt
    private String username;

    private String img;

    @Email(message = "Email invalid!")
    @NotBlank(message = "email must not blank!", groups = CreatedBy.class)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "phone number is invalid!")
    @NotBlank(message = "phone number must not blank!", groups = CreatedBy.class)
    private String phoneNumber;
    @Min(value = 0, message = "Basic Salary must at least 0")
    private Double basicSalary;

    @Min(value = 0, message = "Commession Overrated From KPI must at least 0")
    private Double CommessionOverratedFromKPI;

    @Min(value = 0, message = "Fine Underated From KPI must at least 0")
    private Double FineUnderatedFromKPI;
}
