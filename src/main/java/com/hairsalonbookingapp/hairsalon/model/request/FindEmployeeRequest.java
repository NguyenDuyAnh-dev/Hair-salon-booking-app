package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FindEmployeeRequest {
    @NotBlank(message = "Role must not be blank!")
    @Pattern(regexp = "Manager|Stylist|Staff|Admin", message = "Role is invalid!")
    String role;

    @Pattern(regexp = "Normal|Expert|", message = "StylistLevel is invalid!")
    String stylistLevel;
}
