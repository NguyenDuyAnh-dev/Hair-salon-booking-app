package com.hairsalonbookingapp.hairsalon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "AccountForEmployee")
public class AccountForEmployee implements UserDetails {
    @Id
    @Column(unique = true, nullable = false)
    private String employeeId;

    @NotBlank(message = "Name can not blank!", groups = CreatedBy.class) //ko cho de trong, neu de trong se hien messsage "Name can not blank!"
    private String name;

    @NotBlank(message = "Username can not blank!", groups = CreatedBy.class) //groups = CreatedBy.class chi co nhom create moi bat loi
    @Size(min = 6, message = "Username must be more than 6 characters")
    @Pattern(regexp = "^[\\S]*$", message = "Username must not contain spaces")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=\\-.,<>?/]+$", message = "Invalid characters") //cho phép  ký tự đặc biệt
    @Column(unique = true)
    private String username;

    private String img;

    private String days;

    @Email(message = "Email invalid!")
    @NotBlank(message = "email must not blank!", groups = CreatedBy.class)
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "phone number is invalid!")
    @NotBlank(message = "phone number must not blank!", groups = CreatedBy.class)
    private String phoneNumber;

//    @OneToMany(mappedBy = "employee")
//    @JsonIgnore
//    private List<SalaryMonth> salaryMonths;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<SoftwareSupportApplication> softwareSupportApplications;


    private String stylistLevel;

    @OneToMany(mappedBy = "employee") // Thiết lập mối quan hệ một-nhiều
    @JsonIgnore
    private List<SalaryMonth> salaryMonths;

    private Integer KPI = 0;

    private Integer targetKPI = 0;

    private Double stylistSelectionFee;

    @Min(value = 0, message = "Basic Salary must at least 0")
    private Double basicSalary;

    @Min(value = 0, message = "Commession Overrated From KPI must at least 0")
    private Double CommessionOverratedFromKPI;

    @Min(value = 0, message = "Fine Underated From KPI must at least 0")
    private Double FineUnderatedFromKPI;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<KPIMonth> KPIMonths;

    private Date createdAt;

    @OneToMany(mappedBy = "accountForEmployee")
    @JsonIgnore
    private List<ShiftEmployee> shiftEmployees;

    @NotBlank(message = "Password can not blank!", groups = CreatedBy.class)
    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

    @NotBlank(message = "role must not blank!", groups = CreatedBy.class)
    @Pattern(regexp = "Manager|Stylist|Staff|Admin", message = "role invalid")
    private String role;

    private String status;

    private boolean isDeleted = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(this.role));
//        return authorities;
        return null;
    }

    @Override
    public String getUsername() {
        return this.username; // cho ng dung dang nhap vao bang cai gi co the cho phone neu muon ng dung dang nhap bang phone
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
