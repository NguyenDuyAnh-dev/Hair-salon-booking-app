package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.entity.SoftwareSupportApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SoftwareSupportApplicationRepository extends JpaRepository<SoftwareSupportApplication, Integer> {
    Optional<SoftwareSupportApplication> findTopByOrderBySoftwareSupportApplicationIdDesc();
    SoftwareSupportApplication findSoftwareSupportApplicationBySoftwareSupportApplicationId(int id);

    List<SoftwareSupportApplication> findSoftwareSupportApplicationsByIsDeletedFalse();

    // Lấy tất cả các ứng dụng hỗ trợ phần mềm của Customer với isDeleted = false
//    List<SoftwareSupportApplication> findByCustomerIsNotNullAndIsDeletedFalse();
    Page<SoftwareSupportApplication> findSoftwareSupportApplicationsByCustomerIsNotNullAndIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<SoftwareSupportApplication> findByCustomerAndIsDeletedFalseOrderByCreatedAtDesc(AccountForCustomer customer, Pageable pageable);

    // Lấy tất cả các ứng dụng hỗ trợ phần mềm của Employee với isDeleted = false
    List<SoftwareSupportApplication> findByEmployeeIsNotNullAndIsDeletedFalse();
    Page<SoftwareSupportApplication> findSoftwareSupportApplicationsByEmployeeIsNotNullAndIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<SoftwareSupportApplication> findByEmployeeAndIsDeletedFalseOrderByCreatedAtDesc(AccountForEmployee employee, Pageable pageable);

    Page<SoftwareSupportApplication> findSoftwareSupportApplicationsByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
