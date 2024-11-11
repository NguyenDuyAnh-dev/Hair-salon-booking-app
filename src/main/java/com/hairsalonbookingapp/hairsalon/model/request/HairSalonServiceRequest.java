package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HairSalonServiceRequest {
    @NotBlank(message = "Service name must not be blank!")
    private String name;

    @Min(value = 0, message = "Invalid cost!")
    private double cost;

    @Min(value = 0, message = "Invalid time!")
    @Max(value = 60, message = "Invalid time!")
    private int timeOfService;

    private String description; //MÔ TẢ DỊCH VỤ

    private String image;
}
