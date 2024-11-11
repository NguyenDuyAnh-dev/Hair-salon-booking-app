package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class RequestAppointment {
    @Id
    @Column(unique = true, nullable = false)
    private String appointmentId;

    private Date date;

    private int slot;

    private String status;

    private String serviceStylistSupport;

}
