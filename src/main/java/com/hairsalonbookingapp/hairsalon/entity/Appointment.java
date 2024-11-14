package com.hairsalonbookingapp.hairsalon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long appointmentId;

    private double cost;

    private boolean isCompleted = false;

    private boolean isDeleted = false;

//    private Date date;
    private String date;
    private String startHour;
    private String stylist;
    boolean isSystemChose = false;

    @OneToOne
    @JoinColumn(name = "slotId")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "CustomerId")
    private AccountForCustomer accountForCustomer;

    @ManyToMany
    @JoinTable(
            name = "service_appointment", // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "appointment_id"), // Khóa ngoại từ bảng Appointment
            inverseJoinColumns = @JoinColumn(name = "service_id") // Khóa ngoại từ bảng Service
    )
    private List<HairSalonService> hairSalonServices;

    @OneToOne
    @JoinColumn(name = "discountCodeId")     // MÃ GIẢM GIÁ
    private DiscountCode discountCode;


    @OneToOne(mappedBy = "appointment")
    private Payment payment;

    private String status = "Chưa phục vụ";// Đang phục vụ, Đã thanh toán
}
