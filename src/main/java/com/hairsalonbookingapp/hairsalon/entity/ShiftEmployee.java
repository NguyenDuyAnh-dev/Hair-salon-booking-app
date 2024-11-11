package com.hairsalonbookingapp.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class ShiftEmployee { // DO STYLIST LÀM
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shiftEmployeeId;  // ID CÓ THỂ TỰ GENERATE

    private boolean isAvailable = true;  // CHECK XEM CA NÀY CÒN KHẢ DỤNG VỚI STYLIST HAY KO

    private String date; // NGÀY THEO ĐỊNH DẠNG Y-M-D

    @ManyToOne
    @JoinColumn(name = "dayInWeek", nullable = true)
    private ShiftInWeek shiftInWeek;    // STYLIST CÓ THỂ CÓ NHIỀU CA(THỨ 2,3,4...) TRONG TUẦN

    @ManyToOne
    @JoinColumn(name = "employeeId")
    private AccountForEmployee accountForEmployee;   // CHO BIẾT CA ĐÓ AI LÀM

    @OneToMany(mappedBy = "shiftEmployee")
    @JsonIgnore
    private List<Slot> slots;
}
