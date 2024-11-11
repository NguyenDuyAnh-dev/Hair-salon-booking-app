package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

@Data
public class DeleteAllAppointmentsRequest {
    String stylistId;
    String date;
}
