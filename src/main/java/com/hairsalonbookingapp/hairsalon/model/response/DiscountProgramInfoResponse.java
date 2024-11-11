package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class DiscountProgramInfoResponse {
    private int discountProgramId;

    private String name;

    private String description;

    private long pointChange; // so diem can doi cua chuong trinh

    private Date startedDate;

    private Date endedDate;

    private String status;

    private double percentage;

    private boolean isDeleted = false;
}
