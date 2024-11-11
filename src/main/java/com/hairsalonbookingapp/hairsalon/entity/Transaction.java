package com.hairsalonbookingapp.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    private String transactionType;

    private double money;

    private String description;

    private Date date;

    private String status;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false) // day la foreign key
    @JsonIgnore // Bỏ qua thông tin nhân viên nếu không cần thiết
    private AccountForEmployee employee;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", nullable = true) // day la foreign key
    @JsonIgnore // Bỏ qua thông tin nhân viên nếu không cần thiết
    private AccountForCustomer customer;

    @ManyToOne
    @JoinColumn(name = "paymentId")
    @JsonBackReference // Giải quyết vấn đề vòng lặp tuần hoàn
    private Payment payment;


    private boolean isDeleted = false;
}