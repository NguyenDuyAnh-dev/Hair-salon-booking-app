package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

@Data
public class RequestSoftwareSupportApplication {
//    @Id
//    @Column(unique = true)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int softwareSupportApplicationId;

    private String description;

    private String img;
}
