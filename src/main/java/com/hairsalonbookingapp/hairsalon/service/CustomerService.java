package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.response.CustomerAccountInfo;
import com.hairsalonbookingapp.hairsalon.model.response.CustomerResponsePage;
import com.hairsalonbookingapp.hairsalon.repository.AccountForCustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    AccountForCustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    // LẤY TOÀN BỘ CUSTOMER
    public CustomerResponsePage getAllCustomerAccounts(int page, int size){
        Page<AccountForCustomer> accountForCustomerPage = customerRepository.findAccountForCustomersByIsDeletedFalse(PageRequest.of(page, size));
        List<CustomerAccountInfo> customerAccountInfoList = new ArrayList<>();
        for(AccountForCustomer accountForCustomer : accountForCustomerPage.getContent()){
            CustomerAccountInfo customerAccountInfo = modelMapper.map(accountForCustomer, CustomerAccountInfo.class);
            customerAccountInfoList.add(customerAccountInfo);
        }
        CustomerResponsePage customerResponsePage = new CustomerResponsePage();
        customerResponsePage.setContent(customerAccountInfoList);
        customerResponsePage.setTotalPages(accountForCustomerPage.getTotalPages());
        customerResponsePage.setTotalElements(accountForCustomerPage.getTotalElements());
        customerResponsePage.setPageNumber(accountForCustomerPage.getNumber());
        return customerResponsePage;
    }

    public CustomerResponsePage getAllBanedCustomerAccounts(int page, int size){
        Page<AccountForCustomer> accountForCustomerPage = customerRepository.findAccountForCustomersByIsDeletedTrue(PageRequest.of(page, size));
        List<CustomerAccountInfo> customerAccountInfoList = new ArrayList<>();
        for(AccountForCustomer accountForCustomer : accountForCustomerPage.getContent()){
            CustomerAccountInfo customerAccountInfo = modelMapper.map(accountForCustomer, CustomerAccountInfo.class);
            customerAccountInfoList.add(customerAccountInfo);
        }
        CustomerResponsePage customerResponsePage = new CustomerResponsePage();
        customerResponsePage.setContent(customerAccountInfoList);
        customerResponsePage.setTotalPages(accountForCustomerPage.getTotalPages());
        customerResponsePage.setTotalElements(accountForCustomerPage.getTotalElements());
        customerResponsePage.setPageNumber(accountForCustomerPage.getNumber());
        return customerResponsePage;
    }

    //HÀM RESTART TÀI KHOẢN CUSTOMER
    public CustomerAccountInfo restartCustomer(String phoneNumber){
        AccountForCustomer accountForCustomer = customerRepository.findByPhoneNumber(phoneNumber);
        if(accountForCustomer == null){
            throw new EntityNotFoundException("Customer not found!");
        }

        accountForCustomer.setDeleted(false);
        AccountForCustomer restartedAccount = customerRepository.save(accountForCustomer);
        CustomerAccountInfo customerAccountInfo = modelMapper.map(restartedAccount, CustomerAccountInfo.class);
        return customerAccountInfo;
    }

}
