package com.hairsalonbookingapp.hairsalon.model.response;

import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.entity.KPIMonth;
import lombok.Data;

import java.util.List;

@Data
public class KPIMonthListResponse {
    private List<KPIMonthResponseDTO> content;
    private int pageNumber;
    private long totalElement;
    private int totalPage;
}
