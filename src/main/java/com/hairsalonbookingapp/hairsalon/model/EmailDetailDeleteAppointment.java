package com.hairsalonbookingapp.hairsalon.model;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.HairSalonService;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EmailDetailDeleteAppointment {
    private AccountForCustomer receiver;
    private String subject;
    private String link;
    private long appointmentId;
    private List<HairSalonService> serviceName;
    private String nameStylist;
    private String day;
    private String startHour;
}
