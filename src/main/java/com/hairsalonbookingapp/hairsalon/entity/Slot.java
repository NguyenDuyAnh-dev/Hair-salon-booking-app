package com.hairsalonbookingapp.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long slotId;  // ID CÓ THỂ GENERATE

    private String startSlot;  // SLOT BẮT ĐẦU LÚC MẤY GIỜ

    /*@NotBlank(message = "Date must not be blank!")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$\n", message = "Invalid date!")
    private String date;*/
    private String date; // NGÀY THEO ĐỊNH DẠNG Y-M-D

    private boolean isAvailable = true;  // SLOT CÒN TRỐNG KHÔNG

    @ManyToOne
    @JoinColumn(name = "shiftEmployeeId")
    private ShiftEmployee shiftEmployee;   //   CHO BIẾT SLOT THUỘC CA NÀO, CỦA AI

    @OneToOne(mappedBy = "slot")
    @JsonIgnore
    private Appointment appointments;
}
