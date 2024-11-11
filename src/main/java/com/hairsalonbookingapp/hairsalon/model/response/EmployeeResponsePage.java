package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeResponsePage {
    List<EmployeeInfo> content;
    int pageNumber;
    long totalElements;
    int totalPages;
}
