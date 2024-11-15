package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.entity.Slot;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.SlotRequest;
import com.hairsalonbookingapp.hairsalon.model.response.SlotResponse;
import com.hairsalonbookingapp.hairsalon.model.request.ViewAppointmentRequest;
import com.hairsalonbookingapp.hairsalon.model.response.SlotResponsePage;
import com.hairsalonbookingapp.hairsalon.repository.AppointmentRepository;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.ShiftEmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.SlotRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SlotService {

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    ShiftEmployeeRepository shiftEmployeeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ShiftWeekService shiftWeekService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ShiftEmployeeService shiftEmployeeService;

    @Autowired
    TimeService timeService;

    //TẠO SLOT
    public List<Slot> generateSlots(SlotRequest slotRequest){
        //   MỖI CA(SHIFT) CỦA 1 STYLIST NHẤT ĐỊNH SẼ CÓ SỐ SLOT NHẤT ĐỊNH
        List<Slot> list = new ArrayList<>();
        List<LocalTime> localTimeList = timeService.getSLots(slotRequest.getStartHour(), slotRequest.getEndHour(), slotRequest.getDuration());
        for(LocalTime time : localTimeList){
            if(time.equals(localTimeList.get(localTimeList.size() - 1))){
                break;        // DỪNG NẾU TIME = ENDHOUR
            } else {
                Slot slot = new Slot();
                slot.setDate(slotRequest.getDate());
                slot.setStartSlot(time.toString());
                slot.setShiftEmployee(shiftEmployeeRepository.findShiftEmployeeByShiftEmployeeId(slotRequest.getShiftEmployeeId()));
                Slot newSlot = slotRepository.save(slot);  // TRƯỚC KHI KẾT THÚC VÒNG LẶP SẼ LƯU XUỐNG DB, SAU ĐÓ THÊM VÀO LIST
                list.add(newSlot);
            }
        }

        return list;
        /*//GENERATE LIST RESPONSE
        List<SlotResponse> responseList = new ArrayList<>();
        for(Slot slot : list){
            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
            slotResponse.setId(slot.getId());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setDate(slot.);
            slotResponse.setAvailable(slot.isAvailable());

            responseList.add(slotResponse);
        }*/

    }
/* COMMENT TẠM THỜI
    // xem slot trong ngày dựa trên shiftEmployeeId -> STYLIST LÀM
    public List<SlotResponse> getAllSlotsInDay(long shiftEmployeeId){
        AccountForEmployee account = authenticationService.getCurrentAccountForEmployee();
        List<Slot> slots = slotRepository.findSlotsByShiftEmployee_AccountForEmployee_IdAndShiftEmployee_Id(account.getId(), shiftEmployeeId);
        if(slots != null){

            //GENERATE LIST RESPONSE
            List<SlotResponse> responseList = new ArrayList<>();
            for(Slot slot : slots){
                SlotResponse slotResponse = new SlotResponse();
                slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
                slotResponse.setId(slot.getId());
                slotResponse.setStartSlot(slot.getStartSlot());
                slotResponse.setCompleted(slot.isCompleted());
                slotResponse.setAvailable(slot.isAvailable());

                responseList.add(slotResponse);
            }
            return responseList;
        } else {
            throw new EntityNotFoundException("Slots not found!");
        }
    }

    // GET ALL SLOT -> STYLIST LÀM
    public List<SlotResponse> getAllSlots(){
        List<Slot> slots = slotRepository.findSlotsByShiftEmployee_AccountForEmployee_Id(authenticationService.getCurrentAccountForEmployee().getId());
        if(slots != null){

            //GENERATE LIST RESPONSE
            List<SlotResponse> responseList = new ArrayList<>();
            for(Slot slot : slots){
                SlotResponse slotResponse = new SlotResponse();
                slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
                slotResponse.setId(slot.getId());
                slotResponse.setStartSlot(slot.getStartSlot());
                slotResponse.setCompleted(slot.isCompleted());
                slotResponse.setAvailable(slot.isAvailable());

                responseList.add(slotResponse);
            }
            return responseList;
        } else {
            throw new EntityNotFoundException("Slots not found!");
        }
    }

    // xem slot trong ngày dựa trên shiftEmployeeId -> CUSTOMER LÀM -> DÙNG CHO APPOINTMENT SERVICE
    public List<Slot> getSlots(long shiftEmployeeId){
        List<Slot> slots = slotRepository.findSlotsByShiftEmployee_IdAndIsAvailableTrue(shiftEmployeeId);
        if(slots != null){
            return slots;
        } else {
            throw new EntityNotFoundException("Slots not found!");
        }
    }

    //xóa slot
    //CÓ 2 TRƯỜNG HỢP XẢY RA:
    //1. TRƯỚC: STYLIST BẬN, NÓ THÔNG BÁO MANAGER VÀ ĐC CHẤP THUẬN, STYLIST TẠM THỜI GỠ SLOT ĐÓ RA ĐỂ KHÁCH KHÔNG CHỌN
    //2. SAU: KHÁCH ĐẶT LỊCH HẸN TẠI SLOT NÀY, APPOINTMENT SERVICE TỰ FALSE SLOT ĐÓ
    public SlotResponse deleteSLot(long slotId){
        AccountForEmployee account = authenticationService.getCurrentAccountForEmployee();
        Slot slot = slotRepository.findSlotByShiftEmployee_AccountForEmployee_IdAndId(account.getId(), slotId);
        if(slot != null){
            slot.setAvailable(false);
            Slot newSlot = slotRepository.save(slot);

            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setShiftEmployeeId(newSlot.getShiftEmployee().getId());
            slotResponse.setId(newSlot.getId());
            slotResponse.setStartSlot(newSlot.getStartSlot());
            slotResponse.setCompleted(newSlot.isCompleted());
            slotResponse.setAvailable(newSlot.isAvailable());

            return slotResponse;
        } else {
            throw new EntityNotFoundException("Slot not found!");
        }
    }


    //COMPLETE SLOT -> STAFF LÀM SAU KHI XONG 1 SLOT
    public SlotResponse completeSlot(long slotId){
        Slot slot = slotRepository.findSlotById(slotId);
        if(slot != null){
            slot.setCompleted(true);
            AccountForEmployee account = slot.getShiftEmployee().getAccountForEmployee();
            account.setCompletedSlot(account.getCompletedSlot() + 1);     // CẬP NHẬT VÀO SLOT ĐỂ TÍNH KPI
            AccountForEmployee updatedAccount = employeeRepository.save(account);

            slot.setAvailable(true);
            Slot newSlot = slotRepository.save(slot);

            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setShiftEmployeeId(newSlot.getShiftEmployee().getId());
            slotResponse.setId(newSlot.getId());
            slotResponse.setStartSlot(newSlot.getStartSlot());
            slotResponse.setCompleted(newSlot.isCompleted());
            slotResponse.setAvailable(newSlot.isAvailable());

            return slotResponse;
        } else {
            throw new EntityNotFoundException("Slot not found!");
        }
    }

    // CHUYỂN TẤT CẢ COMPLETE VỀ FALSE -> MANAGER LÀM VÀO THỨ 7
    public String resetAll(){
        List<Slot> slots = slotRepository.findAll();
        for(Slot slot : slots){         // RESET ALL SLOTS
            slot.setAvailable(true);
            slot.setCompleted(false);
            Slot updatedSlot = slotRepository.save(slot);

            Appointment checkAppontment = appointmentRepository.findAppointmentBySlot_IdAndIsCompletedFalse(slot.getId());
            if(checkAppontment != null){
                checkAppontment.setCompleted(true);
                Appointment newAppointment = appointmentRepository.save(checkAppontment);
            }
        }

        List<ShiftEmployee> shiftEmployeeList = shiftEmployeeRepository.findAll();
        for(ShiftEmployee shiftEmployee : shiftEmployeeList){
            shiftEmployee.setCompleted(false);
            ShiftEmployee newShiftEmployee = shiftEmployeeRepository.save(shiftEmployee);
        }

        String message = "Reset complete!";
        return message;
    }

    // STAFF XEM SLOT CỦA STYLIST ĐỂ XÁC NHẬN COMPLETE
    *//*public SlotResponse viewSlotInfo(String stylistID, String day, String startSlot){
        Slot slot = slotRepository.findSlotByStartSlotAndShiftEmployee_AccountForEmployee_IdAndShiftEmployee_ShiftInWeek_DayOfWeek(startSlot, stylistID, day);
        if(slot != null){
            SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
            return slotResponse;
        } else {
            throw new EntityNotFoundException("Slot not found!");
        }
    }*//*

    public SlotResponse viewSlotInfo(long appointmentID) {
        Appointment appointment = appointmentRepository.findAppointmentByIdAndIsDeletedFalse(appointmentID);
        if (appointment != null) {
            Slot slot = appointment.getSlot();

            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
            slotResponse.setId(slot.getId());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setCompleted(slot.isCompleted());
            slotResponse.setAvailable(slot.isAvailable());

            return slotResponse;
        } else {
            throw new EntityNotFoundException("Appointment not found!");
        }
    }

    // CUSTOMER XEM CÁC SLOT PHÙ HỢP
    public List<SlotResponse> viewAvailableSlots(long shiftEmployeeId) {     // XEM CÁC SLOT KHẢ DỤNG CỦA CA
        List<Slot> slotList = getSlots(shiftEmployeeId);
        List<SlotResponse> slotResponseList = new ArrayList<>();
        for(Slot slot : slotList){
            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getId());
            slotResponse.setId(slot.getId());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setCompleted(slot.isCompleted());
            slotResponse.setAvailable(slot.isAvailable());

            slotResponseList.add(slotResponse);
        }
        return slotResponseList;
    }*/ // COMMENT TẠM THỜI

    // HÀM LẤY SLOT
    public Slot getAvailableSlot(long slotId) {
        Slot slot = slotRepository.findSlotBySlotIdAndIsAvailableTrue(slotId);
        if(slot != null){
            return slot;
        } else {
            throw new EntityNotFoundException("Slot not found!");
        }
    }


    // STAFF TÌM KIẾM THÔNG TIN SLOT CỦA STYLIST
    public List<SlotResponse> viewSlotsOfStylist(ViewAppointmentRequest viewAppointmentRequest){
        List<Slot> slotList = slotRepository
                .findSlotsByShiftEmployee_AccountForEmployee_EmployeeIdAndDate(
                        viewAppointmentRequest.getStylistId(),
                        viewAppointmentRequest.getDate()
                );
        if(slotList != null){
            List<SlotResponse> slotResponseList = new ArrayList<>();
            for(Slot slot : slotList){
                SlotResponse slotResponse = new SlotResponse();
                slotResponse.setId(slot.getSlotId());
                slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getShiftEmployeeId());
                slotResponse.setDate(slot.getDate());
                slotResponse.setStartSlot(slot.getStartSlot());
                slotResponse.setAvailable(slot.isAvailable());

                slotResponseList.add(slotResponse);
            }
            return slotResponseList;
        } else {
            throw new EntityNotFoundException("Slot not found!");
        }
    }

    // CUSTOMER XEM KHUNG GIỜ KHẢ DỤNG CỦA STYLIST TRONG NGÀY
    public List<String> getStartHoursByCustomer(ViewAppointmentRequest viewAppointmentRequest){
        List<SlotResponse> slotResponseList = viewSlotsOfStylist(viewAppointmentRequest);
        if(slotResponseList == null){
            throw new EntityNotFoundException("Slot not found!");
        }

        List<String> startHours = new ArrayList<>();
        for (SlotResponse slotResponse : slotResponseList){
            if(slotResponse.isAvailable()){
                String startHour = slotResponse.getStartSlot();
                startHours.add(startHour);
            }
        }

        return startHours;
    }

    // CUSTOMER XEM TẤT CẢ KHUNG GIỜ KHẢ DỤNG TRONG NGÀY
//    public List<String> getStartHoursAvailable(String date, String day){
//        List<String> availableHours = new ArrayList<>(); // MẢNG CHỨA CÁC KHUNG GIỜ KHẢ DỤNG
//        List<Slot> list = new ArrayList<>();
//        List<LocalTime> localTimeList = timeService.getSLots(timeService.setStartHour(day), timeService.setEndHour(day), timeService.duration);
//        for(LocalTime time : localTimeList){
//            if(time.equals(localTimeList.get(localTimeList.size() - 1))){
//                break;        // DỪNG NẾU TIME = ENDHOUR
//            } else {
//                List<Slot> slotList = slotRepository
//                        .findSlotsByDateAndStartSlotAndIsAvailableTrue(date, time.toString());
//                if(!slotList.isEmpty()){
//                    availableHours.add(time.toString());
//                }
//            }
//        }
//        return availableHours;
//
//    }
    public List<String> getStartHoursAvailable(String date){
        List<Slot> slotList = slotRepository.findSlotsByDateAndIsAvailableTrue(date);
        if(slotList.isEmpty()){
            throw new EntityNotFoundException("Slot not found!");
        }
        List<String> availableHours = new ArrayList<>(); // MẢNG CHỨA CÁC KHUNG GIỜ KHẢ DỤNG
        for(int i = 1; i <= 24; i++){
            for(Slot slot : slotList){
                String hour = slot.getStartSlot();
                if(i == Integer.parseInt(hour.substring(0,2))){
                    availableHours.add(hour);
                    break;
                }
            }
        }
        return availableHours;

    }


    //HÀM LẤY SLOT TRONG NGÀY CỦA STYLIST -> STYLIST LÀM
    public SlotResponsePage getSlotsInDayForStylist(String date, int page, int size){
        AccountForEmployee accountForEmployee = authenticationService.getCurrentAccountForEmployee();
        Page<Slot> slotPage = slotRepository
                .findSlotsByShiftEmployee_AccountForEmployeeAndDate(accountForEmployee, date, PageRequest.of(page, size));
        if(slotPage.getContent().isEmpty()){
            throw new EntityNotFoundException("Slot not found!");
        }
        List<SlotResponse> slotResponseList = new ArrayList<>();
        for(Slot slot : slotPage.getContent()){
            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setId(slot.getSlotId());
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getShiftEmployeeId());
            slotResponse.setDate(slot.getDate());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setAvailable(slot.isAvailable());

            slotResponseList.add(slotResponse);
        }

        SlotResponsePage slotResponsePage = new SlotResponsePage();
        slotResponsePage.setContent(slotResponseList);
        slotResponsePage.setPageNumber(slotPage.getNumber());
        slotResponsePage.setTotalElements(slotPage.getTotalElements());
        slotResponsePage.setTotalPages(slotPage.getTotalPages());

        return slotResponsePage;
    }

    //HÀM LẤY TOÀN BỘ SLOT CỦA MỌI STYLIST TRONG NGÀY -> STAFF LÀM
    public SlotResponsePage getSlotsInDayForAllStylist(String date, int page, int size){
        Page<Slot> slotPage = slotRepository
                .findSlotsByDate(date, PageRequest.of(page, size));
        if(slotPage.getContent().isEmpty()){
            throw new EntityNotFoundException("Slot not found!");
        }
        List<SlotResponse> slotResponseList = new ArrayList<>();
        for(Slot slot : slotPage.getContent()){
            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setId(slot.getSlotId());
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getShiftEmployeeId());
            slotResponse.setDate(slot.getDate());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setAvailable(slot.isAvailable());
            slotResponse.setStylist(slot.getShiftEmployee().getAccountForEmployee().getName());
            slotResponse.setLevel(slot.getShiftEmployee().getAccountForEmployee().getStylistLevel());

            slotResponseList.add(slotResponse);
        }

        SlotResponsePage slotResponsePage = new SlotResponsePage();
        slotResponsePage.setContent(slotResponseList);
        slotResponsePage.setPageNumber(slotPage.getNumber());
        slotResponsePage.setTotalElements(slotPage.getTotalElements());
        slotResponsePage.setTotalPages(slotPage.getTotalPages());

        return slotResponsePage;
    }

    //HÀM LẤY SLOT CỦA MỌI STYLIST THEO NGÀY VÀ GIỜ -> STAFF LÀM
    public SlotResponsePage getSlotsByDateAndHourForAllStylist(String date, String startHour, int page, int size){
        Page<Slot> slotPage = slotRepository
                .findSlotsByDateAndStartSlot(date, startHour, PageRequest.of(page, size));
        if(slotPage.getContent().isEmpty()){
            throw new EntityNotFoundException("Slot not found!");
        }
        List<SlotResponse> slotResponseList = new ArrayList<>();
        for(Slot slot : slotPage.getContent()){
            SlotResponse slotResponse = new SlotResponse();
            slotResponse.setId(slot.getSlotId());
            slotResponse.setShiftEmployeeId(slot.getShiftEmployee().getShiftEmployeeId());
            slotResponse.setDate(slot.getDate());
            slotResponse.setStartSlot(slot.getStartSlot());
            slotResponse.setAvailable(slot.isAvailable());

            slotResponseList.add(slotResponse);
        }

        SlotResponsePage slotResponsePage = new SlotResponsePage();
        slotResponsePage.setContent(slotResponseList);
        slotResponsePage.setPageNumber(slotPage.getNumber());
        slotResponsePage.setTotalElements(slotPage.getTotalElements());
        slotResponsePage.setTotalPages(slotPage.getTotalPages());

        return slotResponsePage;
    }

}
