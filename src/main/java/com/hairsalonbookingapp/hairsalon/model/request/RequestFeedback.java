package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RequestFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @Min(value = 1, message = "start must at least 1")
    @Max(value = 5, message = "start must smaller than 5")
    private int star;

    private String comment;

}
