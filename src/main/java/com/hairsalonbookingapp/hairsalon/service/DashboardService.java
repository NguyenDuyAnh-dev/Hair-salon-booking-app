package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.exception.AccountNotFoundException;
import com.hairsalonbookingapp.hairsalon.repository.AccountForCustomerRepository;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.ServiceRepository;
import com.hairsalonbookingapp.hairsalon.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    @Autowired
    AccountForCustomerRepository accountForCustomerRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ServiceRepository serviceRepository;

    public Map<String, Object> getDashbroadStats(){
        Map<String, Object> stats = new HashMap<>();
        // tong customer trong he thong
        long totalCustomer = accountForCustomerRepository.count();
        stats.put("totalCustomer", totalCustomer);

        // tong customer ko bi ban trong he thong
        long totalCustomerNotBan = accountForCustomerRepository.countByIsDeletedFalse();
        stats.put("totalCustomerNotBan", totalCustomerNotBan);

        // tong employee trong salon
        long totalEmployee = employeeRepository.countAllExceptManager();
        stats.put("totalEmployee", totalEmployee);

        //tim top 5 service dc book nhieu nhat
        List<Object[]> topService = serviceRepository.findTop5MostSelectedServices();
        List<Map<String, Object>> topServiceList = new ArrayList<>();
        for(Object[] serviceData : topService){
            Map<String, Object> serviceInfo = new HashMap<>();
            serviceInfo.put("serviceName", serviceData[0]);
            serviceInfo.put("serviceDescription", serviceData[1]);
            serviceInfo.put("serviceImage", serviceData[2]);
            serviceInfo.put("serviceCount", serviceData[3]);

            topServiceList.add(serviceInfo);
        }
        stats.put("topServiceList", topServiceList);

        //tim top 5 stylist dc book nhieu nhat
        List<Object[]> topStylist = employeeRepository.findTop5StylistsByKPI();
        List<Map<String, Object>> topStylistList = new ArrayList<>();
        for(Object[] stylistData : topStylist){
            Map<String, Object> stylistInfo = new HashMap<>();
            stylistInfo.put("stylistName", stylistData[0]);
            stylistInfo.put("stylistImage", stylistData[1]);
            stylistInfo.put("stylistKpi", stylistData[2]);

            topStylistList.add(stylistInfo);
        }
        stats.put("topStylistList", topStylistList);

        return stats;
    }

    public Map<String, Object> getMonthlyRevenue() {
        // Truy vấn doanh thu hàng tháng từ repository.
        List<Object[]> monthlyRevenueData = transactionRepository.calculateMonthlyRevenue();

        // Chuẩn bị danh sách dữ liệu doanh thu.
        List<Map<String, Object>> monthlyRevenueList = new ArrayList<>();
        for (Object[] result : monthlyRevenueData) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("year", result[0]);
            monthData.put("month", result[1]);
            monthData.put("totalRevenue", result[2]);
            monthlyRevenueList.add(monthData);
        }

        // Đưa dữ liệu vào Map để trả về.
        Map<String, Object> response = new HashMap<>();
        response.put("monthlyRevenue", monthlyRevenueList);

        return response;
    }

//    public Map<String, Object> getMonthlyRenuve(){
//        Map<String, Object> renuveDate = new HashMap<>();
//        AccountForCustomer account = authenticationService.getCurrentAccountForCustomer();
//        if(account == null){
//            throw new AccountNotFoundException(" Can find account ");
//        }
//        List<Object[]> monthlyRenuve = transactionRepository.caculateMonthlyRenuve(account.getId());
//        List<Map<String, Object>> monthlyRenuveList = new ArrayList<>();
//        for(Object[] result : monthlyRenuve){
//            Map<String, Object> monthData = new HashMap<>();
//            monthData.put("year", result[0]);
//            monthData.put("month", result[1]);
//            monthData.put("totalRenuve", result[2]);
//            monthlyRenuveList.add(monthData);
//        }
//        renuveDate.put("monthlyRenuve", monthlyRenuveList);
//        return renuveDate;
//    }

}
