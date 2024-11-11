package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class StylistWorkDayNullListResponse {
    private int totalPage;
    private List<String> content;
    private int pageNumber;
    private long totalElement;
}
