package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class SlotResponse {
    private long id;
    private String startSlot;
    private boolean isAvailable;
    private long shiftEmployeeId;
    private String date;
    private String stylist;
    private String level;
}
