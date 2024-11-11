package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.DiscountProgram;
import com.hairsalonbookingapp.hairsalon.exception.CreateException;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.exception.UpdatedException;
import com.hairsalonbookingapp.hairsalon.model.request.RequestDiscountprogram;
import com.hairsalonbookingapp.hairsalon.model.request.RequestUpdateDiscountProgram;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramInfoResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.DiscountProgramResponse;
import com.hairsalonbookingapp.hairsalon.model.response.UpdateDiscountProgramResponse;
import com.hairsalonbookingapp.hairsalon.repository.DiscountProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class DiscountProgramService {
    @Autowired
    DiscountProgramRepository discountProgramRepository;

    @Autowired
    ModelMapper modelMapper;

    // create DiscountProgram
    public DiscountProgramResponse createDiscountProgram(RequestDiscountprogram requestDiscountprogram){
        DiscountProgram discountProgram = modelMapper.map(requestDiscountprogram, DiscountProgram.class);
        try{
//            String newId = generateId();
//            discountProgram.setDiscountProgramId(newId);
            if (discountProgram.getPointChange() < 0) {
                throw new CreateException("Point change must be large than 0!");
            }
            discountProgram.setStatus("Not Start");
            DiscountProgram newDiscountProgram = discountProgramRepository.save(discountProgram);
            return modelMapper.map(newDiscountProgram, DiscountProgramResponse.class);
        } catch (Exception e) {
//            if(e.getMessage().contains(discountProgram.getDiscountProgramId())){
//                throw new Duplicate("duplicate discount program id! ");
//            }
            throw new Duplicate(e + "");
        }
//        return null;
    }

//    public String generateId() {
//        // Tìm ID cuối cùng theo vai trò
//        Optional<DiscountProgram> lasDiscountProgram = discountProgramRepository.findTopByOrderByDiscountProgramIdDesc();
//        int newIdNumber = 1; // Mặc định bắt đầu từ 1
//
//        // Nếu có tài khoản cuối cùng, lấy ID
//        if (lasDiscountProgram.isPresent()) {
//            String lastId = lasDiscountProgram.get().getDiscountProgramId();
//            newIdNumber = Integer.parseInt(lastId.replaceAll("\\D+", "")) + 1; // Tăng số lên 1
//        }
//
//
//        String prefix = "DP";
//
//        return String.format("%s%06d", prefix, newIdNumber); // Tạo ID mới với format
//    }

    //Start program -> MANAGER LÀM
    public DiscountProgramResponse startProgram(int id){
        DiscountProgram oldProgram = discountProgramRepository.findDiscountProgramByDiscountProgramIdAndStatus(id, "Not Start");
        if(oldProgram != null){
            oldProgram.setStatus("In Process");
            DiscountProgram newProgram = discountProgramRepository.save(oldProgram);
            return modelMapper.map(newProgram, DiscountProgramResponse.class);
        } else {
            throw new EntityNotFoundException("Program not found!");
        }
    }

    //End program -> MANAGER LÀM
    public DiscountProgramResponse endProgram(int id){
        DiscountProgram oldProgram = discountProgramRepository.findDiscountProgramByDiscountProgramIdAndStatus(id, "In Process");
        if(oldProgram != null){
            oldProgram.setStatus("Ended");
            DiscountProgram newProgram = discountProgramRepository.save(oldProgram);
            return modelMapper.map(newProgram, DiscountProgramResponse.class);
        } else {
            throw new EntityNotFoundException("Program not found!");
        }
    }


    //delete feedback
    public DiscountProgramResponse deleteDiscountProgram(int discountProgramId){
        // tim toi id ma FE cung cap
        DiscountProgram discountProgramNeedDelete = discountProgramRepository.findDiscountProgramByDiscountProgramId(discountProgramId);
        if(discountProgramNeedDelete == null){
            throw new Duplicate("Program not found!"); // dung tai day
        }

        discountProgramNeedDelete.setDeleted(true);
        discountProgramNeedDelete.setStatus("Ended");
        DiscountProgram deletedDiscountProgramm = discountProgramRepository.save(discountProgramNeedDelete);
        return modelMapper.map(deletedDiscountProgramm, DiscountProgramResponse.class);
    }

    // show list of DiscountProgram
    public DiscountProgramListResponse getAllDiscountProgram(int page, int size){
//        List<DiscountProgram> discountPrograms = discountProgramRepository.findDiscountProgramsByIsDeletedFalse();
//        return discountPrograms;
        Page discountProgramPage = discountProgramRepository.findDiscountProgramsByIsDeletedFalseOrderByEndedDateAsc(PageRequest.of(page, size));
        DiscountProgramListResponse discountProgramListResponse = new DiscountProgramListResponse();
        discountProgramListResponse.setTotalPage(discountProgramPage.getTotalPages());
        discountProgramListResponse.setContent(discountProgramPage.getContent());
        discountProgramListResponse.setPageNumber(discountProgramPage.getNumber());
        discountProgramListResponse.setTotalElement(discountProgramPage.getTotalElements());
        return discountProgramListResponse;
    }

    // show list of DiscountProgram
    public DiscountProgramListResponse getAllDiscountProgramInCustomer(int page, int size){
//        List<DiscountProgram> discountPrograms = discountProgramRepository.findDiscountProgramsByIsDeletedFalse();
//        return discountPrograms;
        Page discountProgramPage = discountProgramRepository.findByStatusOrderByEndedDateAsc(PageRequest.of(page, size), "In Process");
        DiscountProgramListResponse discountProgramListResponse = new DiscountProgramListResponse();
        discountProgramListResponse.setTotalPage(discountProgramPage.getTotalPages());
        discountProgramListResponse.setContent(discountProgramPage.getContent());
        discountProgramListResponse.setPageNumber(discountProgramPage.getNumber());
        discountProgramListResponse.setTotalElement(discountProgramPage.getTotalElements());
        return discountProgramListResponse;
    }

//    public DiscountProgram getCurrentDiscountProgram(){
//        DiscountProgram discountProgram = (DiscountProgram) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return discountProgramRepository.findDiscountProgramByDiscountProgramId(discountProgram.getDiscountProgramId());
//    }


    public UpdateDiscountProgramResponse updatedDiscountProgram(RequestUpdateDiscountProgram requestUpdateDiscountProgram, int id) {
        DiscountProgram discountProgram = modelMapper.map(requestUpdateDiscountProgram, DiscountProgram.class);
//        List<DiscountProgram> discountPrograms = discountProgramRepository.findDiscountProgramByName(name);
        DiscountProgram oldDiscountProgram = discountProgramRepository.findDiscountProgramByDiscountProgramId(id);
        LocalDate today = LocalDate.now();
        if (oldDiscountProgram == null) {
            throw new Duplicate("Discount program not found!");// cho dung luon
        } else {
            try{
                if (discountProgram.getName() != null && !discountProgram.getName().isEmpty()) {
                    oldDiscountProgram.setName(discountProgram.getName());
                }

                if (discountProgram.getDescription() != null && !discountProgram.getDescription().isEmpty()) {
                    oldDiscountProgram.setDescription(discountProgram.getDescription());
                }

                if (discountProgram.getStartedDate() != null) {
                    oldDiscountProgram.setStartedDate(discountProgram.getStartedDate());
                }

                if (discountProgram.getEndedDate() != null) {
                    oldDiscountProgram.setEndedDate(discountProgram.getEndedDate());
                }

                // Cập nhật status dựa vào startDate và endDate
                LocalDate startDate = convertToLocalDate(oldDiscountProgram.getStartedDate());
                LocalDate endDate = convertToLocalDate(oldDiscountProgram.getEndedDate());

                if (startDate.isAfter(today)) {
                    oldDiscountProgram.setStatus("Not Start");
                } else if ((startDate.isEqual(today) || startDate.isBefore(today)) && endDate.isAfter(today)) {
                    oldDiscountProgram.setStatus("In Process");
                } else if (endDate.isBefore(today) || endDate.isEqual(today)) {
                    oldDiscountProgram.setStatus("Ended");
                }

                if (discountProgram.getPercentage() != 0) {
                    oldDiscountProgram.setPercentage(discountProgram.getPercentage());
                }

                if (discountProgram.getPointChange() >= 0) {
                    oldDiscountProgram.setPointChange(discountProgram.getPointChange());
                }

                // Lưu cập nhật vào cơ sở dữ liệu
                DiscountProgram updatedDiscountProgram = discountProgramRepository.save(oldDiscountProgram);
                return modelMapper.map(updatedDiscountProgram, UpdateDiscountProgramResponse.class);
            } catch (Exception e) {
                throw new UpdatedException("Discount Program can not update!");
            }
        }
    }

    // Hàm chuyển đổi từ Date sang LocalDate
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    //GET PROFILE DiscountProgram
    public DiscountProgramInfoResponse getInfoDiscountProgram(int id){
        DiscountProgram discountProgram = discountProgramRepository.findDiscountProgramByDiscountProgramId(id);
        return modelMapper.map(discountProgram, DiscountProgramInfoResponse.class);
    }

}
