package com.hairsalonbookingapp.hairsalon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @Min(value = 1, message = "start must at least 1")
    @Max(value = 5, message = "start must smaller than 5")
    private int star;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = false) // day la foreign key
    private AccountForCustomer customer;

    private Date createdAt;

    private boolean isDeleted = false;
}
