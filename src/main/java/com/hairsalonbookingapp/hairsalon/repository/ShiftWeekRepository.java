package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.ShiftInWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftWeekRepository extends JpaRepository<ShiftInWeek, String> {
    ShiftInWeek findShiftInWeekByDayOfWeekAndIsAvailableTrue(String dayOfWeek);
    List<ShiftInWeek> findShiftInWeeksByIsAvailableTrue();
    //List<String> findDayOfWeeksByStatusTrue();
    ShiftInWeek findShiftInWeekByDayOfWeekAndIsAvailableFalse(String dayOfWeek);
}
