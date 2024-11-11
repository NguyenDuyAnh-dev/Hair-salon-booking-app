package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.HairSalonService;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentResponse {
    private long id;
    private double cost;
    private String day;
    private String startHour;
    private String customer;  // USERNAME
    private List<String> service;
    private String stylist;


    /*private long slotId;
    private String CustomerId;
    private long ServiceId;
    private String discountCodeId;*/
}
