package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.repository.AccountForCustomerRepository;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountForCustomerRepository accountForCustomerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407P";

    private SecretKey getSigninKey(){
        byte[] ketBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(ketBytes);
    }

    //tạo ra token cho customer
    public String generateTokenCustomer(AccountForCustomer accountForCustomer){
        String token = Jwts.builder()
                .subject(accountForCustomer.getPhoneNumber()+ "")
                .issuedAt(new Date(System.currentTimeMillis())) // 10:30
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    //tạo ra token cho employee
    public String generateTokenEmployee(AccountForEmployee accountForEmployee){
        String token = Jwts.builder()
                .subject(accountForEmployee.getUsername()+ "")
                .issuedAt(new Date(System.currentTimeMillis())) // 10:30
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

//    //verify token cho customer
//    public AccountForCustomer getAccountCustomerByToken(String token){
//        Claims claims = Jwts.parser()
//                .verifyWith(getSigninKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//
//        String phoneNumber = claims.getSubject();
//        if(authenticationService.isPhoneNumber(phoneNumber)){
//            return accountForCustomerRepository.findByPhoneNumber(phoneNumber);
//        } else {
//            return null;
//        }
//    }
//
//    //verify token cho employee
//    public AccountForEmployee getAccountEmployeeByToken(String token){
//        Claims claims = Jwts.parser()
//                .verifyWith(getSigninKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//
//        String name = claims.getSubject();
//        if(authenticationService.isPhoneNumber(name)){
//            return null;
//        } else {
//            return employeeRepository.findAccountForEmployeeByUsername(name); // nen thay thanh uẻname
//        }
//    }

    //verify token cho customer
    public AccountForCustomer getAccountCustomerByToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String phoneNumber = claims.getSubject();
        if(authenticationService.isPhoneNumber(phoneNumber)){
            return accountForCustomerRepository.findByPhoneNumber(phoneNumber);
        } else {
            return null;
        }
    }

    //verify token cho employee
    public AccountForEmployee getAccountEmployeeByToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String name = claims.getSubject();
        if(authenticationService.isPhoneNumber(name)){
            return null;
        } else {
            return employeeRepository.findAccountForEmployeeByUsername(name);
        }
    }
}
