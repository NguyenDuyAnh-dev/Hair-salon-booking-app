package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class KPIMonthResponseDTO {
    private Long id;
    private int kpi;
    private int targetKPI;
    private String month;
    private String employeeId;
    private String employeeName;
}
