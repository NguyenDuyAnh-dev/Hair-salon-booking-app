package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.ShiftInWeek;
import com.hairsalonbookingapp.hairsalon.exception.DuplicateEntity;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.ShiftWeekRequest;
import com.hairsalonbookingapp.hairsalon.model.response.ShiftWeekResponse;
import com.hairsalonbookingapp.hairsalon.model.request.ShiftWeekUpdate;
import com.hairsalonbookingapp.hairsalon.repository.ShiftWeekRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftWeekService {

    @Autowired
    ShiftWeekRepository shiftWeekRepository;

    @Autowired
    ModelMapper modelMapper;

    //tạo mới shift -> MANAGER LÀM
    public ShiftWeekResponse createWeekShift(ShiftWeekRequest shiftWeekRequest){
        try{
            ShiftInWeek checkExistedShift = shiftWeekRepository.findShiftInWeekByDayOfWeekAndIsAvailableTrue(shiftWeekRequest.getDayOfWeek());
            if(checkExistedShift != null){
                throw new DuplicateEntity("Duplicate day!");
            }
            ShiftInWeek newShift = modelMapper.map(shiftWeekRequest, ShiftInWeek.class);
            ShiftInWeek savedShift = shiftWeekRepository.save(newShift);
            return modelMapper.map(savedShift, ShiftWeekResponse.class);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate day!");
        }
    }

    //update shift -> MANAGER LÀM
    public ShiftWeekResponse updateShift(ShiftWeekUpdate shiftWeekUpdate, String dayOfWeek){
        ShiftInWeek shift = shiftWeekRepository.findShiftInWeekByDayOfWeekAndIsAvailableTrue(dayOfWeek);
        if(shift != null){
            shift.setStartHour(shiftWeekUpdate.getStartHour());
            shift.setEndHour(shiftWeekUpdate.getEndHour());

            ShiftInWeek newShift = shiftWeekRepository.save(shift);
            return modelMapper.map(newShift, ShiftWeekResponse.class);
        } else {
            throw new EntityNotFoundException("Shift not found!");
        }
    }

    //delete shift -> MANAGER LÀM
    public ShiftWeekResponse deleteShift(String dayOfWeek){
        ShiftInWeek shift = shiftWeekRepository.findShiftInWeekByDayOfWeekAndIsAvailableTrue(dayOfWeek);
        if(shift != null){
            shift.setAvailable(false);
            ShiftInWeek newShift = shiftWeekRepository.save(shift);
            return modelMapper.map(newShift, ShiftWeekResponse.class);
        } else {
            throw new EntityNotFoundException("Shift not found!");
        }
    }

    //get all shift -> MANAGER LÀM
    public List<ShiftWeekResponse> getAllShift(){
        List<ShiftInWeek> list = shiftWeekRepository.findShiftInWeeksByIsAvailableTrue();
        if(list != null){
            List<ShiftWeekResponse> shiftWeekResponseList = new ArrayList<>();
            for(ShiftInWeek shiftInWeek : list){
                ShiftWeekResponse shiftWeekResponse = modelMapper.map(shiftInWeek, ShiftWeekResponse.class);
                shiftWeekResponseList.add(shiftWeekResponse);
            }
            return shiftWeekResponseList;
        } else {
            throw new EntityNotFoundException("Shift not found!");
        }
    }

    /*//get shift by day
    public ShiftInWeek getShift(String dayOfWeek){
        ShiftInWeek shift = shiftWeekRepository.findShiftInWeekByDayOfWeekAndStatusTrue(dayOfWeek);
        return shift;
    }*/

    //RESTART SHIFT -> MANAGER LÀM
    public ShiftWeekResponse restartShift(String dayOfWeek){
        ShiftInWeek shift = shiftWeekRepository.findShiftInWeekByDayOfWeekAndIsAvailableFalse(dayOfWeek);
        if(shift != null){
            shift.setAvailable(true);
            ShiftInWeek newShift = shiftWeekRepository.save(shift);
            return modelMapper.map(newShift, ShiftWeekResponse.class);
        } else {
            throw new EntityNotFoundException("Shift not found!");
        }
    }

    // HÀM LẤY NGÀY ĐẦU TIÊN TRONG SỐ TẤT CẢ CÁC NGAỲ KHẢ DỤNG
//    public ShiftInWeek getFirstAvailableShiftWeek(){
//        List<ShiftInWeek> shiftInWeeks = shiftWeekRepository
//                .findShiftInWeeksByIsAvailableTrue();
//        if(shiftInWeeks.isEmpty()){
//            throw new EntityNotFoundException("No day available!");
//        }
//        return shiftInWeeks.get(0);
//    }
    // HÀM LẤY SHIFT WEEK THEO NGÀY NHẬP
    public ShiftInWeek getAvailableShiftWeekByDay(String day){
        ShiftInWeek shiftInWeek = shiftWeekRepository
                .findShiftInWeekByDayOfWeekAndIsAvailableTrue(day);
        if(shiftInWeek == null){
            throw new EntityNotFoundException("Shift not found!");
        }
        return shiftInWeek;
    }


    // HÀM CẬP NHẬT TOÀN BỘ THỜI GIAN TRONG TUẦN: TẤT CẢ CÁC NGAỲ TRONG TUẦN PHẢI CHUNG KHUNG THỜI GIAN -> MANAGER LÀM
    public List<ShiftWeekResponse> updateAllShiftInWeeks(ShiftWeekUpdate shiftWeekUpdate){
        List<ShiftInWeek> shiftInWeekList = shiftWeekRepository.findShiftInWeeksByIsAvailableTrue();
        if(shiftInWeekList.isEmpty()){
            throw new EntityNotFoundException("No shift found!");
        }
        List<ShiftWeekResponse> shiftWeekResponseList = new ArrayList<>();
        for(ShiftInWeek shiftInWeek : shiftInWeekList){
            ShiftWeekResponse shiftWeekResponse = updateShift(shiftWeekUpdate, shiftInWeek.getDayOfWeek());
            shiftWeekResponseList.add(shiftWeekResponse);
        }
        return shiftWeekResponseList;
    }

}
