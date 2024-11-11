package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Optional<Feedback> findTopByOrderByFeedbackIdDesc();
    Feedback findFeedbackByFeedbackId(int feedbackId);
    List<Feedback> findFeedbacksByIsDeletedFalse();

    Page<Feedback> findFeedbacksByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}
