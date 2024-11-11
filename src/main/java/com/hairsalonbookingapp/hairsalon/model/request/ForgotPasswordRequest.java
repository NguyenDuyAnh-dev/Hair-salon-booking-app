package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @Email(message = "Invalid email!")
    private String email;
}
