package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RequestEditSsalaryEmployee {

    @Min(value = 0, message = "Basic Salary must at least 0")
    private Double basicSalary;

    @Min(value = 0, message = "Commession Overrated From KPI must at least 0")
    private Double CommessionOverratedFromKPI;

    @Min(value = 0, message = "Fine Underated From KPI must at least 0")
    private Double FineUnderatedFromKPI;
}
