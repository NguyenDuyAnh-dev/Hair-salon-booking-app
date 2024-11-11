package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import lombok.Data;

import java.util.Date;

@Data
public class FeedbackResponse {

    private int feedbackId;

    private int star;

    private String comment;

    private AccountForCustomer customer;

    private Date createdAt;

}
