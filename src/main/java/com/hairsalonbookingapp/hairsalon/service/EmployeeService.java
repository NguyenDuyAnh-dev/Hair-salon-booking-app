package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.response.*;
import com.hairsalonbookingapp.hairsalon.model.request.FindEmployeeRequest;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ModelMapper modelMapper;

//    public List<EmployeeInfo> getEmployeeByRole(FindEmployeeRequest findEmployeeRequest){
//        String status = "Workday";
//        List<AccountForEmployee> accountForEmployeeList = new ArrayList<>();
//        if(findEmployeeRequest.getRole().equals("Stylist")){
//            if(findEmployeeRequest.getStylistLevel().equals("Normal")){
//                accountForEmployeeList = employeeRepository.findAccountForEmployeesByRoleAndStylistLevelAndStatusAndIsDeletedFalse("Stylist", "Normal", status);
//            } else if(findEmployeeRequest.getStylistLevel().equals("Expert")){
//                accountForEmployeeList = employeeRepository.findAccountForEmployeesByRoleAndStylistLevelAndStatusAndIsDeletedFalse("Stylist", "Expert", status);
//            } else {
//                throw new EntityNotFoundException("Stylist not found!");
//            }
//        } else {
//            accountForEmployeeList = employeeRepository.findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(findEmployeeRequest.getRole(), status);
//        }
//
//        if(accountForEmployeeList != null){
//            List<EmployeeInfo> employeeInfoList = new ArrayList<>();
//            for(AccountForEmployee accountForEmployee : accountForEmployeeList){
//                EmployeeInfo employeeInfo = modelMapper.map(accountForEmployee, EmployeeInfo.class);
//                employeeInfoList.add(employeeInfo);
//            }
//
//            return employeeInfoList;
//        } else {
//            throw new EntityNotFoundException("Employee not found!");
//        }
//    }

    public EmployeeResponsePage getEmployeeByRole(FindEmployeeRequest findEmployeeRequest, int page, int size){

        String status = "Workday";
        String role = findEmployeeRequest.getRole();
        String stylistLevel = null;
        if(role.equals("Stylist")){
            stylistLevel = findEmployeeRequest.getStylistLevel();
            if(stylistLevel.isEmpty()){
                throw new EntityNotFoundException("Stylist level must not be blank!");
            }
        }

        Page<AccountForEmployee> accountForEmployeePage = employeeRepository
                .findAccountForEmployeesByRoleAndStylistLevelAndStatusAndIsDeletedFalse(
                        role,
                        stylistLevel,
                        status,
                        PageRequest.of(page, size));
        if(accountForEmployeePage.getContent().isEmpty()){
            throw new EntityNotFoundException("Employee not found!");
        }
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for(AccountForEmployee accountForEmployee : accountForEmployeePage.getContent()){
            EmployeeInfo employeeInfo = modelMapper.map(accountForEmployee, EmployeeInfo.class);
            employeeInfoList.add(employeeInfo);
        }
        EmployeeResponsePage employeeResponsePage = new EmployeeResponsePage();
        employeeResponsePage.setContent(employeeInfoList);
        employeeResponsePage.setPageNumber(accountForEmployeePage.getNumber());
        employeeResponsePage.setTotalPages(accountForEmployeePage.getTotalPages());
        employeeResponsePage.setTotalElements(accountForEmployeePage.getTotalElements());

        return employeeResponsePage;

    }



    //GET ALL STYLIST
    public StylistListResponse getAllAvailableStylist(int page, int size) {
        String role = "Stylist";
        String status = "Workday";

        // Tạo yêu cầu phân trang
        PageRequest pageRequest = PageRequest.of(page, size);

        // Lấy danh sách stylist đã phân trang từ database
        Page<AccountForEmployee> stylistPage = employeeRepository
                .findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(role, status, pageRequest);

        // Chuyển đổi danh sách từ AccountForEmployee sang StylistInfo
        List<StylistInfo> stylistInfoList = new ArrayList<>();
        for (AccountForEmployee account : stylistPage.getContent()) {
            StylistInfo stylistInfo = modelMapper.map(account, StylistInfo.class);
            stylistInfoList.add(stylistInfo);
        }

        // Tạo đối tượng StylistListResponse để chứa thông tin trả về
        StylistListResponse response = new StylistListResponse();
        response.setTotalPage(stylistPage.getTotalPages());
        response.setContent(stylistInfoList);
        response.setPageNumber(stylistPage.getNumber());
        response.setTotalElement(stylistPage.getTotalElements());

        return response;
    }

//    public List<StylistInfo> getAllAvailableStylist(){
//        String role = "Stylist";
//        String status = "Workday";
//        List<StylistInfo> stylistInfos = new ArrayList<>();
//        List<AccountForEmployee> list = employeeRepository.findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(role, status);
//        if(list != null){
//            for(AccountForEmployee account : list){
//                StylistInfo stylistInfo = modelMapper.map(account, StylistInfo.class);
//                stylistInfos.add(stylistInfo);
//            }
//            return stylistInfos;
//        } else {
//            throw new EntityNotFoundException("Stylist not found!");
//        }
//    }

    // HÀM LẤY STYLIST
    public AccountForEmployee getStylist(String stylistId) {
        String status = "Workday";
        AccountForEmployee account = employeeRepository
                .findAccountForEmployeeByEmployeeIdAndStatusAndIsDeletedFalse(stylistId, status);
        if(account != null){
            return account;
        } else {
            throw new EntityNotFoundException("Stylist not found!");
        }
    }

    // HÀM LẤY TOÀN BỘ EMPLOYEE KHÔNG QUAN TRỌNG ROLE LÀ GÌ
    public EmployeeResponsePage getAllEmployees(int page, int size){
        Page<AccountForEmployee> accountForEmployeePage = employeeRepository.findAccountForEmployeesByIsDeletedFalse(PageRequest.of(page, size));
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for(AccountForEmployee accountForEmployee : accountForEmployeePage.getContent()){
            EmployeeInfo employeeInfo = modelMapper.map(accountForEmployee, EmployeeInfo.class);
            employeeInfoList.add(employeeInfo);
        }
        EmployeeResponsePage employeeResponsePage = new EmployeeResponsePage();
        employeeResponsePage.setContent(employeeInfoList);
        employeeResponsePage.setTotalPages(accountForEmployeePage.getTotalPages());
        employeeResponsePage.setTotalElements(accountForEmployeePage.getTotalElements());
        employeeResponsePage.setPageNumber(accountForEmployeePage.getNumber());
        return employeeResponsePage;
    }

//    public List<EmployeeInfo> getAllEmployees(){
//        List<AccountForEmployee> accountForEmployeeList = employeeRepository.findByIsDeletedFalse();
//        if(accountForEmployeeList.isEmpty()){
//            throw new EntityNotFoundException("Employee not found!");
//        }
//        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
//        for(AccountForEmployee accountForEmployee : accountForEmployeeList){
//            EmployeeInfo employeeInfo = modelMapper.map(accountForEmployee, EmployeeInfo.class);
//            employeeInfoList.add(employeeInfo);
//        }
//        return employeeInfoList;
//    }

    //HÀM CHECK DANH SÁCH CÁC STYLIST COI CÓ AI CHƯA SET NGÀY LÀM VIỆC KHÔNG
    public StylistWorkDayNullListResponse getStylistsThatWorkDaysNull(int page, int size) {
        String role = "Stylist";
        String status = "Workday";

        // Tạo yêu cầu phân trang
        PageRequest pageRequest = PageRequest.of(page, size);

        // Lấy stylist theo phân trang từ cơ sở dữ liệu
        Page<AccountForEmployee> stylistPage = employeeRepository
                .findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(role, status, pageRequest);

        // Kiểm tra nếu không có stylist nào được tìm thấy
        if (stylistPage.isEmpty()) {
            throw new EntityNotFoundException("Stylist not found!");
        }

        // Tạo danh sách stylist có days = null
        List<String> foundStylists = new ArrayList<>();
        for (AccountForEmployee account : stylistPage.getContent()) {
            if (account.getDays() == null) {
                String stylistInfo = "Id: " + account.getEmployeeId() + ", Name: " + account.getName();
                foundStylists.add(stylistInfo);
            }
        }

        // Tạo đối tượng StylistWorkDayNullListResponse để trả về thông tin phân trang và nội dung
        StylistWorkDayNullListResponse response = new StylistWorkDayNullListResponse();
        response.setTotalPage(stylistPage.getTotalPages());
        response.setContent(foundStylists);
        response.setPageNumber(stylistPage.getNumber());
        response.setTotalElement(stylistPage.getTotalElements());

        return response;
    }


    public List<String> getStylistsThatWorkDaysNull(){
        String role = "Stylist";
        String status = "Workday";
        List<String> foundStylists = new ArrayList<>(); // DANH SÁCH CÁC STYLIST BẮT ĐƯỢC
        List<AccountForEmployee> allStylists = employeeRepository
                .findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(role, status);
        if(allStylists.isEmpty()){
            throw new EntityNotFoundException("Stylist not found!");
        }
        for(AccountForEmployee accountForEmployee : allStylists){
            if(accountForEmployee.getDays() == null){
                String foundStylist = "Id: " + accountForEmployee.getEmployeeId() + ", Name: " + accountForEmployee.getName();
                foundStylists.add(foundStylist);
            }
        }
        return foundStylists;
    }

    public EmployeeResponsePage getAllBanedEmployees(int page, int size){
        Page<AccountForEmployee> accountForEmployeePage = employeeRepository.findAccountForEmployeesByIsDeletedTrue(PageRequest.of(page, size));
        List<EmployeeInfo> employeeInfoList = new ArrayList<>();
        for(AccountForEmployee accountForEmployee : accountForEmployeePage.getContent()){
            EmployeeInfo employeeInfo = modelMapper.map(accountForEmployee, EmployeeInfo.class);
            employeeInfoList.add(employeeInfo);
        }
        EmployeeResponsePage employeeResponsePage = new EmployeeResponsePage();
        employeeResponsePage.setContent(employeeInfoList);
        employeeResponsePage.setTotalPages(accountForEmployeePage.getTotalPages());
        employeeResponsePage.setTotalElements(accountForEmployeePage.getTotalElements());
        employeeResponsePage.setPageNumber(accountForEmployeePage.getNumber());
        return employeeResponsePage;
    }

    //HÀM RESTART ACCOUNT EMPLOYEE
    public EmployeeInfo restartEmployee(String id){
        AccountForEmployee accountForEmployee = employeeRepository.findAccountForEmployeeByEmployeeId(id);
        if(accountForEmployee == null){
            throw new EntityNotFoundException("Employee not found!");
        }

        accountForEmployee.setDeleted(false);
        AccountForEmployee restartedAccount = employeeRepository.save(accountForEmployee);
        EmployeeInfo employeeInfo = modelMapper.map(restartedAccount, EmployeeInfo.class);
        return employeeInfo;
    }
}
