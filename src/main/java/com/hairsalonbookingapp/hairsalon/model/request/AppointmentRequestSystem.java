package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

import java.util.List;

@Data
public class AppointmentRequestSystem {
    String date;
    String startHour;
    List<Long> serviceIdList;
    String discountCode;
}
