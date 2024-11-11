package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.DiscountCode;
import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import com.hairsalonbookingapp.hairsalon.exception.AccountNotFoundException;
import com.hairsalonbookingapp.hairsalon.exception.CreateException;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.exception.UpdatedException;
import com.hairsalonbookingapp.hairsalon.model.request.CreateCodeRequest;
import com.hairsalonbookingapp.hairsalon.model.request.RequestDiscountCode;
import com.hairsalonbookingapp.hairsalon.model.request.RequestUpdateDiscountCode;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountCodeInfResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountCodeListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountCodeResponse;
import com.hairsalonbookingapp.hairsalon.model.response.UpdateDiscountCodeResponse;
import com.hairsalonbookingapp.hairsalon.repository.AccountForCustomerRepository;
import com.hairsalonbookingapp.hairsalon.repository.DiscountCodeRepository;
import com.hairsalonbookingapp.hairsalon.repository.DiscountProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiscountCodeService {
    @Autowired
    DiscountCodeRepository discountCodeRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    DiscountProgramService discountProgramService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    DiscountProgramRepository discountProgramRepository;

    @Autowired
    AccountForCustomerRepository accountForCustomerRepository;

    // create feedback
    public List<DiscountCodeResponse> createDiscountCode(CreateCodeRequest createCodeRequest) {
        DiscountProgram discountProgram = discountProgramRepository.findDiscountProgramByDiscountProgramId(createCodeRequest.getProgramId());
        if(discountProgram == null){
            System.out.println("No current Discount program found.");
            throw new Duplicate("No current Discount program found.");
        }

        AccountForCustomer accountForCustomer = authenticationService.getCurrentAccountForCustomer();
        if(accountForCustomer == null){
            throw new AccountNotFoundException("Account not found!");
        }

        long pointToTrade = discountProgram.getPointChange() * createCodeRequest.getNumberOfTrade();

        if(pointToTrade > accountForCustomer.getPoint()){
            throw new CreateException("You do not have enough points to change!");
        }

        List<DiscountCode> discountCodeList = new ArrayList<>();
        for(int i = 0; i < createCodeRequest.getNumberOfTrade(); i++){
            DiscountCode discountCode = new DiscountCode(); // Tạo mới đối tượng
            discountCode.setDiscountCodeId(generateRandomCode()); // GENERATE CODE
            discountCode.setCustomer(accountForCustomer);
            discountCode.setDiscountProgram(discountProgram);
            DiscountCode savedCode = discountCodeRepository.save(discountCode);
            discountCodeList.add(savedCode);
        }

        // Trừ điểm của khách hàng
        accountForCustomer.setPoint(accountForCustomer.getPoint() - pointToTrade);
        accountForCustomerRepository.save(accountForCustomer);

        // GENERATE RESPONSE
        List<DiscountCodeResponse> discountCodeResponseList = new ArrayList<>();
        for(DiscountCode code : discountCodeList){
            DiscountCodeResponse discountCodeResponse = new DiscountCodeResponse();
            discountCodeResponse.setDiscountCodeId(code.getDiscountCodeId());
            discountCodeResponse.setCustomerName(code.getCustomer().getName());
            discountCodeResponse.setPercentage(code.getDiscountProgram().getPercentage());
            discountCodeResponse.setProgramName(code.getDiscountProgram().getName());

            discountCodeResponseList.add(discountCodeResponse);
        }

        return discountCodeResponseList;
    }

    //HÀM GENERATE DISCOUNT CODE NGẪU NHIÊN GỒM 5 KÝ TỰ BAO GỒM CÁC SỐ , KÝ TỰ THƯỜNG, HOA VÀ ĐẶC BIỆT
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "!@#$%^&*()-_+=<>?";

    private final int CODE_LENGTH = 5;

    public String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }


    //delete feedback
    public DiscountCodeResponse deleteDiscountCode(String discountCodeId){
        // tim toi id ma FE cung cap
        DiscountCode discountCodeNeedDelete = discountCodeRepository.findDiscountCodeByDiscountCodeId(discountCodeId);
        if(discountCodeNeedDelete == null){
            throw new Duplicate("Feedback not found!"); // dung tai day
        }

        discountCodeNeedDelete.setDeleted(true);
        DiscountCode deletedDiscountCode = discountCodeRepository.save(discountCodeNeedDelete);
        return modelMapper.map(deletedDiscountCode, DiscountCodeResponse.class);
    }

    // show list of feedback
    public DiscountCodeListResponse getAllDiscountCode(int page, int size){
//        List<DiscountCode> discountCodes = discountCodeRepository.findDiscountCodesByIsDeletedFalse();
//        return discountCodes;
        Page discountCodePage = discountCodeRepository.findDiscountCodesByIsDeletedFalseOrderByDiscountProgramEndedDateAsc(PageRequest.of(page, size));
        DiscountCodeListResponse discountCodeListResponse = new DiscountCodeListResponse();
        discountCodeListResponse.setTotalPage(discountCodePage.getTotalPages());
        discountCodeListResponse.setContent(discountCodePage.getContent());
        discountCodeListResponse.setPageNumber(discountCodePage.getNumber());
        discountCodeListResponse.setTotalElement(discountCodePage.getTotalElements());
        return discountCodeListResponse;
    }

    public UpdateDiscountCodeResponse updatedDiscountCode(RequestUpdateDiscountCode requestUpdateDiscountCode, String id) {
        DiscountCode discountCode = modelMapper.map(requestUpdateDiscountCode, DiscountCode.class);
//        List<DiscountProgram> discountPrograms = discountProgramRepository.findDiscountProgramByName(name);
        DiscountCode oldDiscountCode = discountCodeRepository.findDiscountCodeByDiscountCodeId(id);
        if (oldDiscountCode == null) {
            throw new Duplicate("Discount program not found!");// cho dung luon
        } else {
            try{
//                if (oldDiscountCode.getCode() != null && !oldDiscountCode.getCode().isEmpty()) {
//                    oldDiscountCode.setCode(oldDiscountCode.getCode());
//                }

                // Lưu cập nhật vào cơ sở dữ liệu
                DiscountCode updatedDiscountCode = discountCodeRepository.save(oldDiscountCode);
                return modelMapper.map(updatedDiscountCode, UpdateDiscountCodeResponse.class);
            } catch (Exception e) {
                throw new UpdatedException("Discount Program can not update!");
            }
        }
    }

    //GET PROFILE DiscountCode
    public DiscountCodeInfResponse getInfoDiscountCode(String id){
        DiscountCode discountCode = discountCodeRepository.findDiscountCodeByDiscountCodeId(id);
        return modelMapper.map(discountCode, DiscountCodeInfResponse.class);
    }
}
