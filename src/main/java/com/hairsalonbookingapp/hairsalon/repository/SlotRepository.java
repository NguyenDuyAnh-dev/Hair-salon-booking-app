package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findSlotsByShiftEmployee_ShiftEmployeeId(long shiftEmployeeId);
    Slot findSlotBySlotId(long id);
    Slot findSlotBySlotIdAndIsAvailableTrue(long id);
    List<Slot> findSlotsByShiftEmployee_ShiftEmployeeIdAndIsAvailableTrue(long shiftEmployeeId);
    List<Slot> findSlotsByShiftEmployee_AccountForEmployee_EmployeeId(String stylistId);
    Slot findSlotByStartSlotAndShiftEmployee_AccountForEmployee_EmployeeIdAndShiftEmployee_ShiftInWeek_DayOfWeek(String startSlot, String stylistID, String dayOfWeek);
    List<Slot> findSlotsByShiftEmployee_AccountForEmployee_EmployeeIdAndShiftEmployee_ShiftEmployeeId(String stylistId, long shiftId);
    Slot findSlotByShiftEmployee_AccountForEmployee_EmployeeIdAndSlotId(String stylistId, long slotId);
    List<Slot> findSlotsByShiftEmployee_AccountForEmployee_EmployeeIdAndDate(String stylistId, String date);
    Slot findSlotBySlotIdAndIsAvailableFalse(long id);
    Slot findSlotByStartSlotAndShiftEmployee_AccountForEmployee_EmployeeIdAndDate(String startSlot, String stylistID, String date);
    List<Slot> findSlotsByDateAndStartSlotAndIsAvailableTrue(String date, String time);
    Slot findSlotByStartSlotAndDateAndShiftEmployee_AccountForEmployee_EmployeeIdAndIsAvailableTrue(String starthour, String date, String employeeId);
    List<Slot> findSlotsByDateAndIsAvailableTrue(String date);
    Page<Slot> findSlotsByShiftEmployee_AccountForEmployeeAndDate(AccountForEmployee accountForEmployee, String date, Pageable pageable);
    Page<Slot> findSlotsByDate(String date, Pageable pageable);
    Page<Slot> findSlotsByDateAndStartSlot(String date, String startSlot, Pageable pageable);
}
