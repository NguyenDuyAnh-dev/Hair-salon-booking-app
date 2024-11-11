package com.hairsalonbookingapp.hairsalon.repository;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<AccountForEmployee, String> {

    Optional<String> findLastIdByRole(@Param("role") String role);
    Optional<AccountForEmployee> findTopByRoleOrderByEmployeeIdDesc(@Param("role") String role);

    AccountForEmployee findByEmail(String email);

    // Lấy account có id lớn nhất để tạo id mới
    Optional<AccountForEmployee> findTopByOrderByEmployeeIdDesc();
    AccountForEmployee findAccountForEmployeeByUsername(String username);
    AccountForEmployee findAccountForEmployeeByEmployeeId(String id);
    AccountForEmployee findAccountForEmployeeByName(String name);
    List<AccountForEmployee> findAccountForEmployeesByIsDeletedFalse();

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);

    List<AccountForEmployee> findAccountForEmployeesByRoleAndStylistLevelAndStatusAndIsDeletedFalse(String role, String stylistLevel, String status);
    List<AccountForEmployee> findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(String role, String status);
    AccountForEmployee findAccountForEmployeeByEmployeeIdAndStatusAndIsDeletedFalse(String id, String status);

    @Query("SELECT COUNT(e.employeeId) FROM AccountForEmployee as e WHERE e.role <> 'MANAGER'")
    long countAllExceptManager();

    @Query(value = "SELECT e.name, e.img, e.KPI FROM account_for_employee e WHERE e.role = 'Stylist' ORDER BY e.KPI DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5StylistsByKPI();

    Page<AccountForEmployee> findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(String role, String status, Pageable pageable);

    Page<AccountForEmployee> findAccountForEmployeesByIsDeletedFalse(Pageable pageable);

    Page<AccountForEmployee> findAccountForEmployeesByIsDeletedTrue(Pageable pageable);

    Page<AccountForEmployee> findAccountForEmployeesByRoleAndStylistLevelAndStatusAndIsDeletedFalse(String role, String stylistLevel, String status, Pageable pageable);
}
