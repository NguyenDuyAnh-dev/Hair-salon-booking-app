package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.*;
import com.hairsalonbookingapp.hairsalon.exception.CreateException;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.exception.NoContentException;
import com.hairsalonbookingapp.hairsalon.model.EmailDetail;
import com.hairsalonbookingapp.hairsalon.model.EmailDetailForEmployee;
import com.hairsalonbookingapp.hairsalon.model.EmailDetailForEmployeeSalary;
import com.hairsalonbookingapp.hairsalon.model.request.RequestSalaryMonth;
import com.hairsalonbookingapp.hairsalon.model.request.RequestSalaryMonthForAnEmployee;
import com.hairsalonbookingapp.hairsalon.model.response.SalaryMonthListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.SalaryMonthResponse;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.SalaryMonthRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryMonthService {
    @Autowired
    SalaryMonthRepository salaryMonthRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    ModelMapper modelMapper;
    // create feedback
    public SalaryMonthResponse createSalaryMonthForAnEmployee(RequestSalaryMonthForAnEmployee requestSalaryMonthForAnEmployee){
        SalaryMonth salaryMonth = modelMapper.map(requestSalaryMonthForAnEmployee.getRequestSalaryMonth(), SalaryMonth.class);
        try{
//            String newId = generateId();
//            feedback.setFeedbackId(newId);
            // Tìm nhân viên theo ID
            AccountForEmployee employee = employeeRepository.findAccountForEmployeeByEmployeeId(requestSalaryMonthForAnEmployee.getEmployeeId());
            if (employee == null) {
                System.out.println("employee empty");
                throw new Duplicate("Employee not found");
            }
            salaryMonth.setEmployee(employee);

            Month currentMonth = LocalDate.now().getMonth();
            salaryMonth.setMonth(currentMonth);

            double commessionOverratedFromKPI = 1;
            if (employee.getCommessionOverratedFromKPI() != null) {
                if(employee.getCommessionOverratedFromKPI() > 0){
                    commessionOverratedFromKPI = employee.getCommessionOverratedFromKPI();
                    salaryMonth.setCommessionOveratedFromKPI(commessionOverratedFromKPI);
                }else{
                    throw new CreateException("Commession Overrated From KPI must be greater than 0");
                }
            }

            double fineUnderatedFromKPI = 1;
            if (employee.getFineUnderatedFromKPI() != null) {
                if(employee.getFineUnderatedFromKPI() > 0){
                    fineUnderatedFromKPI = employee.getFineUnderatedFromKPI();
                    salaryMonth.setFineUnderatedFromKPI(fineUnderatedFromKPI);
                }else{
                    throw new CreateException("Fine Underated From KPI must be greater than 0");
                }
            }

            double kpiRatio = (double)employee.getKPI() / employee.getTargetKPI(); // Tỷ lệ KPI đạt được

            if (kpiRatio < 1.0) {  // KPI dưới mục tiêu
                double fine = (1.0 - kpiRatio) * fineUnderatedFromKPI; // Phạt dựa trên phần thiếu
                salaryMonth.setSumSalary(employee.getBasicSalary() - (employee.getBasicSalary() * fine));

            } else if (kpiRatio > 1.0) { // KPI vượt mục tiêu
                double bonus = (kpiRatio - 1.0) * commessionOverratedFromKPI; // Thưởng dựa trên phần vượt
                salaryMonth.setSumSalary(employee.getBasicSalary() + (employee.getBasicSalary() * bonus));

            } else { // KPI bằng mục tiêu
                salaryMonth.setSumSalary(employee.getBasicSalary());
            }

            EmailDetailForEmployeeSalary emailDetail = new EmailDetailForEmployeeSalary();
            emailDetail.setReceiver(employee);
            emailDetail.setSubject("Salary Announcement" + currentMonth + "!");
            emailDetail.setLink("http://localhost:5173/loginEmployee");
            emailDetail.setSumSalary(salaryMonth.getSumSalary());
            emailService.sendEmailToEmployeeSalary(emailDetail);

            SalaryMonth newSalaryMonth = salaryMonthRepository.save(salaryMonth);
            return modelMapper.map(newSalaryMonth, SalaryMonthResponse.class);
        } catch (Exception e) {
//            if(e.getMessage().contains(salaryMonth.get)){
//                throw new Duplicate("duplicate employee! ");
//            }
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<SalaryMonthResponse> createSalaryForAllEmployees() {
        List<SalaryMonthResponse> salaryMonthResponses = new ArrayList<>();
        try {
            // Lấy danh sách tất cả nhân viên
            List<AccountForEmployee> employees = employeeRepository.findAccountForEmployeesByIsDeletedFalse();
            if (employees.isEmpty()) {
                throw new Duplicate("No employees found");
            }

            Month currentMonth = LocalDate.now().getMonth();
            boolean salaryCreated = false; // Biến để kiểm tra có tạo được lương không

            // Lặp qua từng nhân viên để tạo SalaryMonth
            for (AccountForEmployee employee : employees) {
                // Kiểm tra xem nhân viên đã có lương cho tháng hiện tại hay chưa
                Optional<SalaryMonth> existingSalaryMonth = salaryMonthRepository.findByEmployeeAndMonth(employee, currentMonth);
                if (existingSalaryMonth.isPresent()) {
                    // Nếu đã có lương cho tháng hiện tại, bỏ qua hoặc cập nhật tùy theo yêu cầu
                    System.out.println("Salary already exists for employee: " + employee.getEmployeeId());
                    continue; // Bỏ qua nếu không muốn cập nhật
                }

                if(employee.getBasicSalary() == null){
                    continue;
                }

                SalaryMonth salaryMonth = new SalaryMonth();
                salaryMonth.setEmployee(employee);
                salaryMonth.setMonth(currentMonth);

                double commessionOverratedFromKPI = 1;
                if (employee.getCommessionOverratedFromKPI() != null) {
                    if(employee.getCommessionOverratedFromKPI() > 0){
                        commessionOverratedFromKPI = employee.getCommessionOverratedFromKPI();
                        salaryMonth.setCommessionOveratedFromKPI(commessionOverratedFromKPI);
                    }else{
                        throw new CreateException("Commession Overrated From KPI must be greater than 0");
                    }
                }

                double fineUnderatedFromKPI = 1;
                if (employee.getFineUnderatedFromKPI() != null) {
                    if(employee.getFineUnderatedFromKPI() > 0){
                        fineUnderatedFromKPI = employee.getFineUnderatedFromKPI();
                        salaryMonth.setFineUnderatedFromKPI(fineUnderatedFromKPI);
                    }else{
                        throw new CreateException("Fine Underated From KPI must be greater than 0");
                    }
                }

                double kpiRatio = (double)employee.getKPI() / employee.getTargetKPI(); // Tỷ lệ KPI đạt được

                if (kpiRatio < 1.0) {  // KPI dưới mục tiêu
                    double fine = (1.0 - kpiRatio) * fineUnderatedFromKPI; // Phạt dựa trên phần thiếu
                    salaryMonth.setSumSalary(employee.getBasicSalary() - (employee.getBasicSalary() * fine));

                } else if (kpiRatio > 1.0) { // KPI vượt mục tiêu
                    double bonus = (kpiRatio - 1.0) * commessionOverratedFromKPI; // Thưởng dựa trên phần vượt
                    salaryMonth.setSumSalary(employee.getBasicSalary() + (employee.getBasicSalary() * bonus));

                } else { // KPI bằng mục tiêu
                    salaryMonth.setSumSalary(employee.getBasicSalary());
                }

                // Lưu SalaryMonth vào cơ sở dữ liệu
                SalaryMonth newSalaryMonth = salaryMonthRepository.save(salaryMonth);
                SalaryMonthResponse salaryMonthResponse = modelMapper.map(newSalaryMonth, SalaryMonthResponse.class);
                salaryMonthResponses.add(salaryMonthResponse);

                EmailDetailForEmployeeSalary emailDetail = new EmailDetailForEmployeeSalary();
                emailDetail.setReceiver(employee);
                emailDetail.setSubject("Salary Announcement" + currentMonth + "!");
                emailDetail.setLink("http://localhost:5173/loginEmployee");
                emailDetail.setSumSalary(salaryMonth.getSumSalary());
                emailService.sendEmailToEmployeeSalary(emailDetail);

                salaryCreated = true; // Đánh dấu là đã tạo lương thành công
            }
            if (!salaryCreated) {
                throw new NoContentException("No salaries were created for any employees. Or all employee was get salary month"); // Ném exception
            }

            return salaryMonthResponses;

        } catch (NoContentException e) {
            throw new NoContentException("No salaries were created for any employees. Or all employee was get salary month"); // Hoặc sử dụng một exception tùy chỉnh để trả về mã 204
        }
//        return null;
    }

//    public String generateId() {
//        // Tìm ID cuối cùng theo vai trò
//        Optional<Feedback> lastFeedback = feedbackRepository.findTopByOrderByFeedbackIdDesc();
//        int newIdNumber = 1; // Mặc định bắt đầu từ 1
//
//        // Nếu có tài khoản cuối cùng, lấy ID
//        if (lastFeedback.isPresent()) {
//            String lastId = lastFeedback.get().getFeedbackId();
//            newIdNumber = Integer.parseInt(lastId.replaceAll("\\D+", "")) + 1; // Tăng số lên 1
//        }
//
//
//        String prefix = "FB";
//
//        return String.format("%s%06d", prefix, newIdNumber); // Tạo ID mới với format
//    }


    //delete feedback
    public SalaryMonthResponse deleteSalaryMonth(int salaryMonthId){
        // tim toi id ma FE cung cap
        SalaryMonth salaryMonthNeedDelete = salaryMonthRepository.findSalaryMonthBySalaryMonthId(salaryMonthId);
        if(salaryMonthNeedDelete == null){
            throw new Duplicate("SalaryMonth not found!"); // dung tai day
        }

        salaryMonthNeedDelete.setDeleted(true);
        SalaryMonth deletedSalaryMonth = salaryMonthRepository.save(salaryMonthNeedDelete);
        return modelMapper.map(deletedSalaryMonth, SalaryMonthResponse.class);
    }

    // show list of SalaryMonth
    public SalaryMonthListResponse getAllSalaryMonth(int page, int size){
//        List<SalaryMonth> salaryMonths = salaryMonthRepository.findSalaryMonthsByIsDeletedFalse();
//        return salaryMonths;
        Page salaryMonthPage = salaryMonthRepository.findSalaryMonthsByIsDeletedFalseOrderByCreatedAtDesc(PageRequest.of(page, size));
        SalaryMonthListResponse salaryMonthListResponse = new SalaryMonthListResponse();
        salaryMonthListResponse.setTotalPage(salaryMonthPage.getTotalPages());
        salaryMonthListResponse.setContent(salaryMonthPage.getContent());
        salaryMonthListResponse.setPageNumber(salaryMonthPage.getNumber());
        salaryMonthListResponse.setTotalElement(salaryMonthPage.getTotalElements());
        return salaryMonthListResponse;
    }

    public SalaryMonthListResponse getAllSalaryMonthOfAnEmployee(int page, int size){
//        List<SalaryMonth> salaryMonths = salaryMonthRepository.findSalaryMonthsByIsDeletedFalse();
//        return salaryMonths;
        AccountForEmployee employee = authenticationService.getCurrentAccountForEmployee();
        if(employee == null){
            throw new EntityNotFoundException("Không tìm thấy nhân viên");
        }
        Page<SalaryMonth> salaryMonthPage = salaryMonthRepository
                .findByEmployee_EmployeeIdAndIsDeletedFalseOrderByCreatedAtDesc(
                        employee.getEmployeeId(), PageRequest.of(page, size)
                );
        SalaryMonthListResponse salaryMonthListResponse = new SalaryMonthListResponse();
        salaryMonthListResponse.setTotalPage(salaryMonthPage.getTotalPages());
        salaryMonthListResponse.setContent(salaryMonthPage.getContent());
        salaryMonthListResponse.setPageNumber(salaryMonthPage.getNumber());
        salaryMonthListResponse.setTotalElement(salaryMonthPage.getTotalElements());
        return salaryMonthListResponse;
    }

    // show list of SalaryMonth chi acc do thay
    public List<SalaryMonth> getAllSalaryMonthOfAnEmployee(String employeeId){
        List<SalaryMonth> salaryMonths = salaryMonthRepository.findSalaryMonthsByEmployee_EmployeeIdAndIsDeletedFalse(employeeId);
        return salaryMonths;
    }

    //GET PROFILE SalaryMonth
    public SalaryMonthResponse getInfoSalaryMonth(int id){
        SalaryMonth salaryMonth = salaryMonthRepository.findSalaryMonthBySalaryMonthId(id);
        return modelMapper.map(salaryMonth, SalaryMonthResponse.class);
    }
}
