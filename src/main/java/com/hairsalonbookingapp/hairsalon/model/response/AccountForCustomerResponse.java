package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class AccountForCustomerResponse {
    private String email;
    private String name;

    private String phoneNumber;
    private String token;
    private long score;
}
