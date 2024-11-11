package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class StylistInfo {
    String id;
    String name;
    String img;
    String phoneNumber;
    String stylistLevel;
    String status;
    private Double stylistSelectionFee;
    private int targetKPI;
    int KPI; // KPI của stylist // [Stylist]
}
