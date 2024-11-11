package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.entity.SalaryMonth;
import com.hairsalonbookingapp.hairsalon.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findTopByOrderByTransactionIdDesc();
    Transaction findTransactionByTransactionId(int transactionId);
    List<Transaction> findTransactionsByIsDeletedFalse();
    Page<Transaction> findTransactionsByIsDeletedFalseOrderByDateDesc(Pageable pageable);

    // Truy vấn doanh thu theo tháng của salon trong năm hiện tại
    @Query("SELECT YEAR(t.date) AS year, MONTH(t.date) AS month, SUM(t.money) AS totalRevenue " +
            "FROM Transaction t " +
            "WHERE t.status = 'Success' " +
            "GROUP BY YEAR(t.date), MONTH(t.date) " +
            "ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> calculateMonthlyRevenue();
}
