package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.SoftwareSupportApplication;
import lombok.Data;

import java.util.List;

@Data
public class SoftwareSupportApplicationListResponse {
    private List<SoftwareSupportApplication> content;
    private int pageNumber;
    private long totalElement;
    private int totalPage;
}
