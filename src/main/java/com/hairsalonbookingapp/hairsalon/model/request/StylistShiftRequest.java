package com.hairsalonbookingapp.hairsalon.model.request;

import lombok.Data;

import java.util.List;

@Data
public class StylistShiftRequest {
    List<String> workDays; // MONDAY, TUESDAY,... CHUỖI CHỨA CÁC NGÀY LÀM VIỆC
    String StylistID;
}
