package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class SoftwareSupportApplicationResponse {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int softwareSupportApplicationId;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = false) // day la foreign key
    private AccountForCustomer customer;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false) // day la foreign key
    private AccountForEmployee employee;

    private String description;

    private Date createdAt;

    private String img;
}
