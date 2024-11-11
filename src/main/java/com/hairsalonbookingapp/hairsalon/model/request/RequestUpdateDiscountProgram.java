package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class RequestUpdateDiscountProgram {
    private int discountProgramId;

    private String name;

    private String description;

    private long pointChange; // so diem can doi cua chuong trinh

    private Date startedDate;

    private Date endedDate;

    private double percentage;
}
