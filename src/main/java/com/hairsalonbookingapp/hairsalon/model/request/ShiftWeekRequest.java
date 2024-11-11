package com.hairsalonbookingapp.hairsalon.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class ShiftWeekRequest {
    @NotBlank(message = "Day must not be blank!")
    @Pattern(regexp = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY")
    private String dayOfWeek;

    private Date DateApply;

    @NotBlank(message = "Start hour must not be blank!")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time!")
    private String startHour;

    @NotBlank(message = "End hour must not be blank!")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time!")
    private String endHour;

}
