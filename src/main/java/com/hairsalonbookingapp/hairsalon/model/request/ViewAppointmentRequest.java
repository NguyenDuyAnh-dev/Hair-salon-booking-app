package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewAppointmentRequest {
    String stylistId;
    String date;
}
