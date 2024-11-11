package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import lombok.Data;

import java.util.List;

@Data
public class EmailDetailCreateAppointment {
    private AccountForCustomer receiver;
    private String subject;
    private String link;
    private long appointmentId;
    private List<String> serviceName;
    private String nameStylist;
    private String day;
    private String startHour;
}
