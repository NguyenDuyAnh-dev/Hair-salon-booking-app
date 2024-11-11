package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileCustomer {
    String email;
    String customerName;
    long point;
    String phoneNumber;
    Date creatAt;
    String password;
}
