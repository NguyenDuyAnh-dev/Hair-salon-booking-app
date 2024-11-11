package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RequestEditProfileEmployee {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String name;

    private String img;

    @Email(message = "Email invalid!")
    private String email;

    private String oldPassword;

//    @Size(min = 6, message = "Password must at least 6 characters")
    private String newPassword;

//    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "phone number is invalid! ")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b|^$", message = "phone number is invalid! ")
    private String phoneNumber;

//    private String password;
}
