package com.hairsalonbookingapp.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "DiscountProgram")
public class DiscountProgram {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int discountProgramId;

    private String name;

    private String description;

    private Date startedDate;

    private Date endedDate;

    private String img;

    private long pointChange; // so diem can doi cua chuong trinh

    private String status = "Not Start";

    private double percentage;

    @OneToMany(mappedBy = "discountProgram")
    @JsonIgnore
    private List<DiscountCode> discountCodes;

    private boolean isDeleted = false;
}
