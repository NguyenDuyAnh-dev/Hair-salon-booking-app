package com.hairsalonbookingapp.hairsalon.model.response;

import lombok.Data;

@Data
public class KPITotal {
    String stylistId;
    int targetKPI;
    int KPI; // TỔNG LỊCH HẸN STYLIST NHẬN TRONG THÁNG
}
