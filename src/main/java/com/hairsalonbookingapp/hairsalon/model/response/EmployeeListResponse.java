package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeListResponse {
    private int totalPage;
    private List<EmployeeInfo> content;
    private int pageNumber;
    private long totalElement;
}
