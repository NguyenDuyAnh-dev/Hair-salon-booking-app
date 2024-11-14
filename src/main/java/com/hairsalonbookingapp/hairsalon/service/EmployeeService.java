package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.ShiftEmployee;
import com.hairsalonbookingapp.hairsalon.entity.ShiftInWeek;
import com.hairsalonbookingapp.hairsalon.entity.Slot;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.SlotRequest;
import com.hairsalonbookingapp.hairsalon.model.response.*;
import com.hairsalonbookingapp.hairsalon.model.request.FindEmployeeRequest;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.ShiftEmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.ShiftWeekRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TimeService timeService;

    @Autowired
    ShiftWeekRepository shiftWeekRepository;

    @Autowired
    ShiftEmployeeRepository shiftEmployeeRepository;

    @Autowired
    SlotService slotService;

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

    //HÀM TRẢ VỀ DANH SÁCH CÁC STYLIST ĐÃ SET NGÀY LÀM VIỆC
    public List<EmployeeInfo> getStylistsThatWorkDaysNotNull(){
        String role = "Stylist";
        String status = "Workday";
        List<EmployeeInfo> foundStylists = new ArrayList<>();
        List<AccountForEmployee> allStylists = employeeRepository
                .findAccountForEmployeesByRoleAndStatusAndIsDeletedFalse(role, status);
        if(allStylists.isEmpty()){
            throw new EntityNotFoundException("Stylist not found!");
        }
        for(AccountForEmployee accountForEmployee : allStylists){
            if(accountForEmployee.getDays() != null){
                foundStylists.add(modelMapper.map(accountForEmployee, EmployeeInfo.class));
            }
        }
        return foundStylists;
    }

    //HÀM TẠO SHIFT EMPLOYEE: TẠO SHIFT EMPLOYEE CỦA 1 STYLIST TỪ NGÀY HIỆN TẠI ĐẾN CUỐI TUẦN
// STARTDATE LÀ NGÀY BẮT ĐẦU: VÍ DỤ STARTDATE LÀ 13/11/2024 THÌ HỆ THỐNG SẼ TẠO CHO STYLIST CÁC CA NẰM TRONG KHOẢN TỪ 13/11/24 - 17/11/24
//VÍ DỤ START DATE NHẬP LÀ 16/11/2024 THÌ HỆ THỐNG SẼ TẠO CHO STYLIST CÁC CA NẰM TRONG KHOẢN 16/11 – 17/11(CHỦ NHẬT) NẾU CÓ STYLIST ĐĂNG KÝ CA TRONG NGÀY ĐÓ
// VÍ DỤ KHÁC LÀ: VÀO NGÀY THỨ 5 (14/11/2024), 1 STYLIST ĐÃ ĐĂNG KÝ NGÀY LÀM VIỆC LÀ THỨ 2,3 VÀ THỨ 6, TRONG NGÀY NÀY, NẾU CHỨC NĂNG NÀY ĐC SỬ DỤNG THÌ NÓ SẼ CHỈ TẠO CHO STYLIST NÀY CA THỨ 6 THÔI, VÌ THỨ 2,3 NÓ KHÔNG NẰM TRONG KHOẢN THỨ 6 – CHỦ NHẬT
//CHỨC NĂNG NÀY CHỈ TẠO CÁC CA TRONG PHẠM VI 1 TUẦN (LOGIC Y CHĂNG HÀM CREATE ALL, KHÁC Ở CHỖ THAY VÌ NÓ TẠO TUẦN SAU THÌ NÓ CÓ THỂ TẠO CHO BẤT KỲ TUẦN NÀO – THÔNG QUA START DATE, THẾ NÊN NẾU TẠO CA CHO TUẦN SAU THÌ CHỈ CẦN NHẬP START DATE LÀ 18/11/2024 (THỨ 2 TUẦN SAU))
//START DATE NHẬP THEO ĐỊNH DẠNG YYYY-MM-DD

    public List<ShiftEmployeeResponse> generateShiftEmployeeByDate(String stylistId, String startDate){
        AccountForEmployee accountForEmployee = employeeRepository.findAccountForEmployeeByEmployeeId(stylistId);
        if(accountForEmployee == null){
            throw new EntityNotFoundException("Stylist not found!");
        }
        String days = accountForEmployee.getDays(); // LẤY CÁC NGÀY STYLIST CHỌN
        String[] daysOfWeek = days.split(","); // TÁCH CHUỖI CÁC NGÀY THÀNH 1 DANH SÁCH DỰA TRÊN DẤU ,
        List<LocalDate> daysUntilWeekDays = timeService.getDaysUntilWeekend(startDate);
        List<ShiftEmployee> shiftEmployeeList = new ArrayList<>();
        List<ShiftEmployeeResponse> shiftEmployeeResponseList = new ArrayList<>();
        for(String day : daysOfWeek){
            // TẠO MỚI SHIFT EMPLOYEE
            ShiftEmployee shiftEmployee = new ShiftEmployee();
            ShiftInWeek shiftInWeek = shiftWeekRepository
                    .findShiftInWeekByDayOfWeekAndIsAvailableTrue(day);
            shiftEmployee.setShiftInWeek(shiftInWeek);
            shiftEmployee.setAccountForEmployee(accountForEmployee);
            // SET DAY
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
            for(LocalDate date : daysUntilWeekDays){
                if(date.getDayOfWeek() == dayOfWeek){
                    shiftEmployee.setDate(date.toString());
                    break;
                }
            }
            if(shiftEmployee.getDate() != null){
                // SAVE VÀO DB
                ShiftEmployee newShiftEmployee = shiftEmployeeRepository.save(shiftEmployee);
                // TẠO CÁC SLOT
                SlotRequest slotRequest = new SlotRequest();
                slotRequest.setDate(newShiftEmployee.getDate());
                slotRequest.setShiftEmployeeId(newShiftEmployee.getShiftEmployeeId());
                slotRequest.setStartHour(timeService.setStartHour(day));
                slotRequest.setEndHour(timeService.setEndHour(day));
                slotRequest.setDuration(timeService.duration);
                List<Slot> slotList = slotService.generateSlots(slotRequest);
                newShiftEmployee.setSlots(slotList);
                // SAVE LẠI VÀO DB
                ShiftEmployee savedShift = shiftEmployeeRepository.save(newShiftEmployee);

                //ADD TO ACCOUNT FOR EMPLOYEE => MỘT STYLIST CÓ NHIỀU SHIFTS
                shiftEmployeeList.add(savedShift);

                // GENERATE RESPONSE
                ShiftEmployeeResponse shiftEmployeeResponse = new ShiftEmployeeResponse();
                shiftEmployeeResponse.setId(savedShift.getShiftEmployeeId());
                shiftEmployeeResponse.setAvailable(savedShift.isAvailable());
                shiftEmployeeResponse.setEmployeeId(savedShift.getAccountForEmployee().getEmployeeId());
                shiftEmployeeResponse.setName(savedShift.getAccountForEmployee().getName());
                shiftEmployeeResponse.setDayInWeek(savedShift.getShiftInWeek().getDayOfWeek());
                shiftEmployeeResponse.setDate(savedShift.getDate());

                shiftEmployeeResponseList.add(shiftEmployeeResponse);
            }

        }

        // LƯU LẠI ACCOUNT FOR EMPLOYEE
        accountForEmployee.setShiftEmployees(shiftEmployeeList);
        employeeRepository.save(accountForEmployee);

        return shiftEmployeeResponseList;
    }


}
