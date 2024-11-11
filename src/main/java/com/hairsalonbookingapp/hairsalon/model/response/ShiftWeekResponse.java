package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class ShiftWeekResponse {
    private String dayOfWeek;
    private String startHour;
    private String endHour;
    private boolean isAvailable;
}
