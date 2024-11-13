package com.hairsalonbookingapp.hairsalon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class KPIMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int kpi;
    private int targetKPI;
    private String month; // Lưu tháng, ví dụ "2024-11"

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false) // day la foreign key
    private AccountForEmployee employee;  // Mối quan hệ với Employee

}