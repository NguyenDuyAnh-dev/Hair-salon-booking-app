package com.hairsalonbookingapp.hairsalon.model;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import lombok.Data;

@Data
public class EmailDetailForEmployee {
    private AccountForEmployee receiver;
    private String subject;
    private String link;
}
