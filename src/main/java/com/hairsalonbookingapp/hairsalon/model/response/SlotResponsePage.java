package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

import java.util.List;

@Data
public class SlotResponsePage {
    List<SlotResponse> content;
    int pageNumber;
    long totalElements;
    int totalPages;
}
