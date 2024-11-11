package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class UpdateSoftwareSupportApplicationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int softwareSupportApplicationId;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = true, referencedColumnName = "phoneNumber") // day la foreign key
    private AccountForCustomer customer;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = true, referencedColumnName = "employeeId") // day la foreign key
    private AccountForEmployee employee;

    private String description;

    private String img;

    private boolean isDeleted = false;
}
