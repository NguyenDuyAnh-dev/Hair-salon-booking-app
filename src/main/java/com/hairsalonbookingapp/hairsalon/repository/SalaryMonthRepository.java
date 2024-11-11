package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.entity.SalaryMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;
import java.util.Optional;

public interface SalaryMonthRepository extends JpaRepository<SalaryMonth, Integer> {
    Optional<SalaryMonth> findTopByOrderBySalaryMonthIdDesc();
    SalaryMonth findSalaryMonthBySalaryMonthId(int salaryMonthId);
    List<SalaryMonth> findSalaryMonthsByIsDeletedFalse();
    List<SalaryMonth> findSalaryMonthsByEmployee_EmployeeIdAndIsDeletedFalse(String employeeId);

    Optional<SalaryMonth> findByEmployeeAndMonth(AccountForEmployee employee, Month month);
    Page<SalaryMonth> findSalaryMonthsByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    Page<SalaryMonth> findByEmployee_EmployeeIdAndIsDeletedFalseOrderByCreatedAtDesc(
            String employeeId, Pageable pageable);
}
