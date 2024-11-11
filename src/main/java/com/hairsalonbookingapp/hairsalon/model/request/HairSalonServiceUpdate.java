package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class HairSalonServiceUpdate {
    //@Pattern(regexp = "^[a-zA-Z0-9 ]*$\n|", message = "Invalid name!")
    private String name;

    @Min(value = 0, message = "Invalid cost!")
    private double cost;

    @Min(value = 0, message = "Invalid time!")
    private int timeOfService;

    private String description; //MÔ TẢ DỊCH VỤ

    private String image;
}
