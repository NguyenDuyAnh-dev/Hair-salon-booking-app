package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

@Data
public class RequestSalaryMonthForAnEmployee {
    RequestSalaryMonth requestSalaryMonth;
    String employeeId;
}
