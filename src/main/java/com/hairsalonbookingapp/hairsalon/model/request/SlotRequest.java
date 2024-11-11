package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlotRequest {
    long shiftEmployeeId;
    int startHour;
    int endHour;
    long duration;
    String date;
}
