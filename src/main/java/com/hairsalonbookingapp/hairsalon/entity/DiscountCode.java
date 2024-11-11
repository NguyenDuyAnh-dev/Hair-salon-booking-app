package com.hairsalonbookingapp.hairsalon.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "DiscountCode")
public class DiscountCode {
    @Id
    @Column(unique = true, nullable = false)
    private String discountCodeId;

    @ManyToOne
    @JoinColumn(name = "discountProgramId", nullable = false) // day la foreign key
    private DiscountProgram discountProgram;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = true) // day la foreign key
    private AccountForCustomer customer;

    @OneToOne(mappedBy = "discountCode")
    private Appointment appointment;

    private boolean isDeleted = false;
}
