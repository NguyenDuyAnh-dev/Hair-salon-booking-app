package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.entity.KPIMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KPIMonthRepository extends JpaRepository<KPIMonth, Long> {
    Optional<KPIMonth> findByEmployeeAndMonth(AccountForEmployee employee, String month);
    Page<KPIMonth> findByMonth(String month, Pageable pageable);
}
