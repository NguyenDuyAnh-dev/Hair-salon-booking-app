package com.hairsalonbookingapp.hairsalon.model;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import lombok.Data;

@Data
public class EmailDetailForEmployeeSalary {
    private AccountForEmployee receiver;
    private String subject;
    private String link;
    private double sumSalary;
}
