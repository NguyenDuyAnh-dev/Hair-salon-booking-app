package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

@Data
public class RequestTransaction {
    private String transactionType;

    private double money;

    private String description;
}
