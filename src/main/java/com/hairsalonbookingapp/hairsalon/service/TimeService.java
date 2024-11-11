package com.hairsalonbookingapp.hairsalon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeService {

    // GIẢ ĐỊNH NGÀY HIỆN TẠI
    public LocalDate today = getToday();
    // THỜI GIAN BẮT ĐÂÙ VÀ KẾT THÚC CA LÀM VIỆC
//    public final int startHour = 7;
//    public final int endHour = 10;
    public final long duration = 60;

    @Autowired
    ShiftWeekService shiftWeekService;

    public int setStartHour(String day){
        String startHour = shiftWeekService.getAvailableShiftWeekByDay(day).getStartHour();
        return Integer.parseInt(startHour.substring(0,2));
    }

    public int setEndHour(String day){
        String endHour = shiftWeekService.getAvailableShiftWeekByDay(day).getEndHour();
        return Integer.parseInt(endHour.substring(0,2));
    }

    // DANH SÁCH CÁC NGÀY TRONG NĂM
    public List<LocalDate> getAllDaysInYear(int year) {
        List<LocalDate> daysInYear = new ArrayList<>();

        // Ngày bắt đầu là 1/1 của năm nhập vào
        LocalDate startDate = LocalDate.of(year, 1, 1);

        // Lặp qua từng ngày trong năm
        for (int i = 0; i < startDate.lengthOfYear(); i++) {
            daysInYear.add(startDate.plusDays(i));
        }

        return daysInYear;
    }


    // LẤY CÁC NGÀY TỪ NGÀY A ĐẾN NGÀY B
    public List<LocalDate> getDaysBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> daysBetween = new ArrayList<>();

        // Kiểm tra nếu startDate trước endDate
        if (startDate.isAfter(endDate)) {
            System.out.println("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc.");
            return daysBetween; // Trả về danh sách rỗng nếu không hợp lệ
        }

        // Lặp qua các ngày trong khoảng từ startDate đến endDate
        while (!startDate.isAfter(endDate)) {
            daysBetween.add(startDate);
            startDate = startDate.plusDays(1);  // Cộng thêm 1 ngày
        }

        return daysBetween;
    }


    // HÀM LẤY THỜI GIAN HIỆN TẠI (NGÀY, THÁNG, NĂM)
    public LocalDate getToday(){
        return LocalDate.now();
    }

    // HÀM LẤY NGÀY THÁNG CỦA TUẦN TIẾP THEO TÍNH TỪ NGÀY HIỆN TẠI
    public List<LocalDate> getNextWeekDays(LocalDate date) {
        List<LocalDate> daysOfWeek = new ArrayList<>();

        // Tìm thứ 2 của tuần tiếp theo
        LocalDate nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        // Thêm từng ngày từ thứ 2 đến chủ nhật vào danh sách
        for (int i = 0; i < 7; i++) {
            daysOfWeek.add(nextMonday.plusDays(i));
        }

        return daysOfWeek;

    }

    /*// HÀM LỌC NGÀY
    public static List<LocalDate> filterDays(List<LocalDate> dates, ) {
        List<LocalDate> result = new ArrayList<>();

        for (LocalDate date : dates) {
            // Nếu là thứ 2 hoặc thứ 6, thêm vào danh sách kết quả
            if (date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                result.add(date);
            }
        }

        return result;
    }*/

    //chia các slot -> HỖ TRỢ HÀM DƯỚI
    public List<LocalTime> getTimeIntervals(LocalTime startTime, LocalTime endTime, Duration interval) {
        List<LocalTime> timeIntervals = new ArrayList<>();

        // Bắt đầu từ startTime và thêm vào danh sách các khoảng thời gian đều nhau
        LocalTime currentTime = startTime;
        while (!currentTime.isAfter(endTime)) {
            timeIntervals.add(currentTime);
            currentTime = currentTime.plus(interval); // Tăng thời gian lên theo khoảng thời gian interval
        }

        return timeIntervals;
    }

    // chia slot dựa vào giờ bắt đầu và kết thúc
    public List<LocalTime> getSLots(int startHour, int endHour, long duration){
        LocalTime startTime = LocalTime.of(startHour, 0);
        LocalTime endTime = LocalTime.of(endHour, 0);

        Duration interval = Duration.ofMinutes(duration);

        List<LocalTime> intervals = getTimeIntervals(startTime, endTime, interval);
        return intervals;
    }

    // HÀM TRẢ VỀ NGÀY HÔM NAY VÀ NGÀY MAI
    public List<LocalDate> getTodayAndNextDay(){
        List<LocalDate> list = new ArrayList<>();
        list.add(today);
        list.add(today.plusDays(1));
        return list;
    }

    // HÀM NHẬN NGÀY VÀ TRẢ VỀ THỨ
    public String getDay(String date){
        String year = date.substring(0,4);
        int yearInt = Integer.parseInt(year);
        String month = date.substring(5,7);
        int monthInt = Integer.parseInt(month);
        String day = date.substring(8);
        int dayInt = Integer.parseInt(day);
        String dayOfWeek = LocalDate.of(yearInt, monthInt, dayInt).getDayOfWeek().name();
        return dayOfWeek;
    }


}
