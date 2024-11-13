package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
//    Appointment findAppointmentByAppointmentIdAndStatusAndIsDeletedFalse(long id, String status);
//    List<Appointment> findAppointmentsByStatusAndIsDeletedFalse(String status);
//    Appointment findAppointmentByAppointmentIdAndIsDeletedFalse(long id);
//    Appointment findAppointmentBySlot_SlotIdAndIsCompletedFalse(long slotID);
//    Appointment findAppointmentByAppointmentIdAndAccountForCustomerAndStatusAndIsDeletedFalse(long id, AccountForCustomer accountForCustomer, String status);
    Appointment findAppointmentBySlot_SlotIdAndIsDeletedFalse(long id);
    Appointment findAppointmentBySlot_SlotIdAndAccountForCustomerAndIsDeletedFalse(long id, AccountForCustomer accountForCustomer);
    Appointment findAppointmentByAppointmentIdAndAccountForCustomerAndIsDeletedFalse(long id, AccountForCustomer accountForCustomer);
    List<Appointment> findAppointmentsByDateAndAccountForCustomer_PhoneNumberAndIsDeletedFalse(String date, String phone);
    Page<Appointment> findAppointmentsByDateAndStartHourAndIsCompletedFalseAndIsDeletedFalse(String date, String hour, Pageable pageable);
    Appointment findAppointmentByAppointmentId(long id);
    Page<Appointment> findAppointmentsByDateAndIsCompletedFalseAndIsDeletedFalse(String date, Pageable pageable);
}
