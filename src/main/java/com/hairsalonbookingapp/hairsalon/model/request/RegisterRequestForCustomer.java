package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestForCustomer {
    @Email(message = "Email invalid!")
    @NotBlank(message = "email must not blank!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Name must not blank!")
    @Size(min = 3, message = "Name must be more than 3 characters")
    private String name;

    @Id
    @Column(unique = true)
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "phone number is invalid!")
    @NotBlank(message = "phone number must not blank!")
    private String phoneNumber;

    @NotBlank(message = "Password can not blank!")
    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

//    private String role = "customer";
}
