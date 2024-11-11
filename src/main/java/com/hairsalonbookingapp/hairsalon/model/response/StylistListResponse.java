package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class StylistListResponse {
    private int totalPage;
    private List<StylistInfo> content;
    private int pageNumber;
    private long totalElement;
}
