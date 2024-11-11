package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class RequestUpdateSoftwareSupportApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int softwareSupportApplicationId;

    private String description;

    private String img;
}
