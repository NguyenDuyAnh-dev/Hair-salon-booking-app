package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentResponseInfo {
    long id;
    double cost;
    String date;
    String startHour;
    String customer;  // USERNAME
    List<String> service;
    String stylist;
    boolean isDeleted;
    boolean isCompleted;
}
