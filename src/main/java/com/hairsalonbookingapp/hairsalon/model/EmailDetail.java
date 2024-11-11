package com.hairsalonbookingapp.hairsalon.model;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import lombok.Data;

@Data
public class EmailDetail {
    private AccountForCustomer receiver;
    private String subject;
    private String link;
}
