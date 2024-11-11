package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.*;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.exception.UpdatedException;
import com.hairsalonbookingapp.hairsalon.model.request.RequestSoftwareSupportApplication;
import com.hairsalonbookingapp.hairsalon.model.request.RequestUpdateSoftwareSupportApplication;
import com.hairsalonbookingapp.hairsalon.model.response.SoftwareSupportApplicationListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.SoftwareSupportApplicationResponse;
import com.hairsalonbookingapp.hairsalon.model.response.UpdateSoftwareSupportApplicationResponse;
import com.hairsalonbookingapp.hairsalon.repository.SoftwareSupportApplicationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SoftwareSupportApplicationService {
    @Autowired
    SoftwareSupportApplicationRepository softwareSupportApplicationRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ModelMapper modelMapper;
    // create SoftwareSupportApplication
    public SoftwareSupportApplicationResponse createSoftwareSupportApplication(RequestSoftwareSupportApplication requestSoftwareSupportApplication){
//        SoftwareSupportApplication softwareSupportApplication = modelMapper.map(requestSoftwareSupportApplication, SoftwareSupportApplication.class);
        SoftwareSupportApplication softwareSupportApplication = new SoftwareSupportApplication();
        softwareSupportApplication.setDescription(requestSoftwareSupportApplication.getDescription());
        softwareSupportApplication.setImg(requestSoftwareSupportApplication.getImg());
        try{
//            String newId = generateId();
//            feedback.setFeedbackId(newId);
            // Lấy thông tin tài khoản khách hàng hiện tại
            AccountForCustomer accountForCustomer = authenticationService.getCurrentAccountForCustomer();

            // Lấy thông tin tài khoản nhân viên hiện tại
            AccountForEmployee accountForEmployee = authenticationService.getCurrentAccountForEmployee();
            if (accountForCustomer != null) {
                // Nếu là khách hàng, thiết lập customer
                softwareSupportApplication.setCustomer(accountForCustomer);
            } else {
                // Nếu không phải khách hàng, kiểm tra tài khoản nhân viên
                if (accountForEmployee != null) {
                    softwareSupportApplication.setEmployee(accountForEmployee);
                } else {
                    // Nếu cả hai loại tài khoản đều không tồn tại, ném ra ngoại lệ
                    throw new IllegalStateException("At least one of Customer or Employee must be set.");
                }
            }



            // Kiểm tra nếu cả customer và employee đều không được thiết lập
            if (softwareSupportApplication.getCustomer() == null && softwareSupportApplication.getEmployee() == null) {
                throw new Duplicate("At least one of Customer or Employee must be set.");
            }

            softwareSupportApplication.setCreatedAt(new Date());

            SoftwareSupportApplication newSoftwareSupportApplication = softwareSupportApplicationRepository.save(softwareSupportApplication);
            return modelMapper.map(newSoftwareSupportApplication, SoftwareSupportApplicationResponse.class);
        } catch (Exception e) {
            System.out.println("Error creating SoftwareSupportApplication: " + e.getMessage());
            throw new Duplicate("Software Support Application duplicate id");
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


    //delete SoftwareSupportApplication
    public SoftwareSupportApplicationResponse deleteSoftwareSupportApplication(int id){
        // tim toi id ma FE cung cap
        SoftwareSupportApplication softwareSupportApplicationNeedDelete = softwareSupportApplicationRepository.findSoftwareSupportApplicationBySoftwareSupportApplicationId(id);
        if( softwareSupportApplicationNeedDelete== null){
            throw new Duplicate("Software Support Application not found!"); // dung tai day
        }

        softwareSupportApplicationNeedDelete.setDeleted(true);
        SoftwareSupportApplication deletedSoftwareSupportApplication = softwareSupportApplicationRepository.save(softwareSupportApplicationNeedDelete);
        return modelMapper.map(deletedSoftwareSupportApplication, SoftwareSupportApplicationResponse.class);
    }

    public UpdateSoftwareSupportApplicationResponse updatedSoftwareSupportApplication(RequestUpdateSoftwareSupportApplication requestUpdateSoftwareSupportApplication, int id) {
        SoftwareSupportApplication softwareSupportApplication = modelMapper.map(requestUpdateSoftwareSupportApplication, SoftwareSupportApplication.class);
//        List<DiscountProgram> discountPrograms = discountProgramRepository.findDiscountProgramByName(name);
        SoftwareSupportApplication oldSoftwareSupportApplication = softwareSupportApplicationRepository.findSoftwareSupportApplicationBySoftwareSupportApplicationId(id);
        if (oldSoftwareSupportApplication == null) {
            throw new Duplicate("Software Support Application not found!");// cho dung luon
        } else {
            try{
                // Lấy thông tin tài khoản khách hàng hiện tại
                AccountForCustomer accountForCustomer = authenticationService.getCurrentAccountForCustomer();

                // Lấy thông tin tài khoản nhân viên hiện tại
                AccountForEmployee accountForEmployee = authenticationService.getCurrentAccountForEmployee();

                if (accountForCustomer != null) {
                    softwareSupportApplication.setCustomer(accountForCustomer);
                } else if (accountForEmployee != null) { // Chỉ nên kiểm tra một cái thôi
                    softwareSupportApplication.setEmployee(accountForEmployee);
                } else {
                    throw new Duplicate("At least one of Customer or Employee must be set.");
                }

                if (softwareSupportApplication.getDescription() != null && !softwareSupportApplication.getDescription().isEmpty()) {
                    oldSoftwareSupportApplication.setDescription(softwareSupportApplication.getDescription());
                }

                if (softwareSupportApplication.getImg() != null && !softwareSupportApplication.getImg().isEmpty()) {
                    oldSoftwareSupportApplication.setImg(softwareSupportApplication.getImg());
                }

                // Lưu cập nhật vào cơ sở dữ liệu
                SoftwareSupportApplication updatedSoftwareSupportApplication = softwareSupportApplicationRepository.save(oldSoftwareSupportApplication);
                return modelMapper.map(updatedSoftwareSupportApplication, UpdateSoftwareSupportApplicationResponse.class);
            } catch (Exception e) {
                throw new UpdatedException("Software Support Application can not update!");
            }
        }
    }

    // show list of SoftwareSupportApplication cua customer
    public SoftwareSupportApplicationListResponse getAllSoftwareSupportApplicationOfCustomer(int page, int size){
//        List<SoftwareSupportApplication> softwareSupportApplications = softwareSupportApplicationRepository.findByCustomerIsNotNullAndIsDeletedFalse();
//        return softwareSupportApplications;
        Page softwareSupportApplicationPage = softwareSupportApplicationRepository.findSoftwareSupportApplicationsByCustomerIsNotNullAndIsDeletedFalseOrderByCreatedAtDesc(PageRequest.of(page, size));
        SoftwareSupportApplicationListResponse softwareSupportApplicationListResponse = new SoftwareSupportApplicationListResponse();
        softwareSupportApplicationListResponse.setTotalPage(softwareSupportApplicationPage.getTotalPages());
        softwareSupportApplicationListResponse.setContent(softwareSupportApplicationPage.getContent());
        softwareSupportApplicationListResponse.setPageNumber(softwareSupportApplicationPage.getNumber());
        softwareSupportApplicationListResponse.setTotalElement(softwareSupportApplicationPage.getTotalElements());
        return softwareSupportApplicationListResponse;
    }

    // show list of SoftwareSupportApplication cua rieng 1 customer
    public SoftwareSupportApplicationListResponse getAllSoftwareSupportApplicationOfAnCustomer(int page, int size){
//        List<SoftwareSupportApplication> softwareSupportApplications = softwareSupportApplicationRepository.findByCustomerIsNotNullAndIsDeletedFalse();
//        return softwareSupportApplications;

        AccountForCustomer accountForCustomer = authenticationService.getCurrentAccountForCustomer();

        Page softwareSupportApplicationPage = softwareSupportApplicationRepository.findByCustomerAndIsDeletedFalseOrderByCreatedAtDesc(accountForCustomer, PageRequest.of(page, size));
        SoftwareSupportApplicationListResponse softwareSupportApplicationListResponse = new SoftwareSupportApplicationListResponse();
        softwareSupportApplicationListResponse.setTotalPage(softwareSupportApplicationPage.getTotalPages());
        softwareSupportApplicationListResponse.setContent(softwareSupportApplicationPage.getContent());
        softwareSupportApplicationListResponse.setPageNumber(softwareSupportApplicationPage.getNumber());
        softwareSupportApplicationListResponse.setTotalElement(softwareSupportApplicationPage.getTotalElements());
        return softwareSupportApplicationListResponse;
    }

    //GET PROFILE SoftwareSupportApplication
    public SoftwareSupportApplicationResponse getInfoSoftwareSupportApplication(int id){
        SoftwareSupportApplication softwareSupportApplication = softwareSupportApplicationRepository.findSoftwareSupportApplicationBySoftwareSupportApplicationId(id);
        return modelMapper.map(softwareSupportApplication, SoftwareSupportApplicationResponse.class);
    }

    // show list of SoftwareSupportApplication cua employee
    public SoftwareSupportApplicationListResponse getAllSoftwareSupportApplicationOfEmployee(int page, int size){
//        List<SoftwareSupportApplication> softwareSupportApplications = softwareSupportApplicationRepository.findByEmployeeIsNotNullAndIsDeletedFalse();
//        return softwareSupportApplications;
        Page softwareSupportApplicationPage = softwareSupportApplicationRepository.findSoftwareSupportApplicationsByEmployeeIsNotNullAndIsDeletedFalseOrderByCreatedAtDesc(PageRequest.of(page, size));
        SoftwareSupportApplicationListResponse softwareSupportApplicationListResponse = new SoftwareSupportApplicationListResponse();
        softwareSupportApplicationListResponse.setTotalPage(softwareSupportApplicationPage.getTotalPages());
        softwareSupportApplicationListResponse.setContent(softwareSupportApplicationPage.getContent());
        softwareSupportApplicationListResponse.setPageNumber(softwareSupportApplicationPage.getNumber());
        softwareSupportApplicationListResponse.setTotalElement(softwareSupportApplicationPage.getTotalElements());
        return softwareSupportApplicationListResponse;
    }

    // show list of SoftwareSupportApplication cua rieng 1 customer
    public SoftwareSupportApplicationListResponse getAllSoftwareSupportApplicationOfAnEmployee(int page, int size){
//        List<SoftwareSupportApplication> softwareSupportApplications = softwareSupportApplicationRepository.findByCustomerIsNotNullAndIsDeletedFalse();
//        return softwareSupportApplications;

        AccountForEmployee accountForEmployee = authenticationService.getCurrentAccountForEmployee();

        Page softwareSupportApplicationPage = softwareSupportApplicationRepository.findByEmployeeAndIsDeletedFalseOrderByCreatedAtDesc(accountForEmployee, PageRequest.of(page, size));
        SoftwareSupportApplicationListResponse softwareSupportApplicationListResponse = new SoftwareSupportApplicationListResponse();
        softwareSupportApplicationListResponse.setTotalPage(softwareSupportApplicationPage.getTotalPages());
        softwareSupportApplicationListResponse.setContent(softwareSupportApplicationPage.getContent());
        softwareSupportApplicationListResponse.setPageNumber(softwareSupportApplicationPage.getNumber());
        softwareSupportApplicationListResponse.setTotalElement(softwareSupportApplicationPage.getTotalElements());
        return softwareSupportApplicationListResponse;
    }
}
