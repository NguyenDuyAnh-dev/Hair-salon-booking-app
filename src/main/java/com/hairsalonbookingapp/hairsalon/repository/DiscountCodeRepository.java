package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.DiscountCode;
import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DiscountCodeRepository extends JpaRepository<DiscountCode, String> {
    Optional<DiscountCode> findTopByOrderByDiscountCodeIdDesc();
    DiscountCode findDiscountCodeByDiscountCodeId(String id);
    List<DiscountCode> findDiscountCodesByIsDeletedFalse();
    Page<DiscountCode> findDiscountCodesByIsDeletedFalseOrderByDiscountProgramEndedDateAsc(Pageable pageable);
}
