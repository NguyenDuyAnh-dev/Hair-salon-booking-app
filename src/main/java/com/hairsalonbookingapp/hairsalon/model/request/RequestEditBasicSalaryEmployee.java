package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RequestEditBasicSalaryEmployee {

//    @Id
//    @Column(unique = true, nullable = false)
//    private String employeeId;

    @Min(value = 0, message = "Basic Salary must at least 0")
    private Double basicSalary;
}
