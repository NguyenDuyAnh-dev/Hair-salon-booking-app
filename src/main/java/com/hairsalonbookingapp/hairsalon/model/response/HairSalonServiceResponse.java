package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class HairSalonServiceResponse {
    private long id;
    private String name;
    private double cost;
    private int timeOfService;
    private String image;
    private boolean isAvailable;
    private String description; //MÔ TẢ DỊCH VỤ
}
