package com.hairsalonbookingapp.hairsalon.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hairsalonbookingapp.hairsalon.entity.DiscountCode;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateDiscountProgramResponse {
    private int discountProgramId;

    private String name;

    private String description;

    private Date startedDate;

    private Date endedDate;

    private long amount;

    private String img;

    private String status;

    private double percentage;

    @OneToMany(mappedBy = "discountProgram")
    @JsonIgnore
    private List<DiscountCode> discountCodes;

    private boolean isDeleted = false;
}
