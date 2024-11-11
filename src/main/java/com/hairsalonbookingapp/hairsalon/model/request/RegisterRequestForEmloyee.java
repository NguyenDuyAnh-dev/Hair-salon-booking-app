package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

@Data
public class RegisterRequestForEmloyee {
    @Id
    @Column(unique = true, nullable = false)
    private String employeeId;

    @NotBlank(message = "Name can not blank!", groups = CreatedBy.class) //ko cho de trong, neu de trong se hien messsage "Name can not blank!"
    private String name;

    @NotBlank(message = "Username can not blank!", groups = CreatedBy.class) //groups = CreatedBy.class chi co nhom create moi bat loi
    @Size(min = 6, message = "Username must be more than 6 characters")
    @Pattern(regexp = "^[\\S]*$", message = "Username must not contain spaces")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=\\-.,<>?/]+$", message = "Invalid characters") // cho phép và ký tự đặc biệt
    @Column(unique = true)
    private String username;

    @Email(message = "Email invalid!")
    @NotBlank(message = "email must not blank!", groups = CreatedBy.class)
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "phone number is invalid!")
    @NotBlank(message = "phone number must not blank!", groups = CreatedBy.class)
    @Column(unique = true)
    private String phoneNumber;

    @NotBlank(message = "Password can not blank!", groups = CreatedBy.class)
    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

    @NotBlank(message = "role must not blank!", groups = CreatedBy.class)
    @Pattern(regexp = "Manager|Stylist|Staff|Admin", message = "role invalid")
    private String role;
}
