package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

@Data
public class RequestUpdateProfileEmployeeByManager {
//    @Id
//    @Column(unique = true, nullable = false)
//    private String employeeId;

//    @OneToMany(mappedBy = "employee")
//    @JsonIgnore
//    private List<SalaryMonth> salaryMonths;

    private Long stylistSelectionFee;

    private String stylistLevel;

    private Integer targetKPI;
}
