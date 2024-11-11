package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.HairSalonService;
import com.hairsalonbookingapp.hairsalon.exception.DuplicateEntity;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.HairSalonServiceRequest;
import com.hairsalonbookingapp.hairsalon.model.response.HairSalonServiceResponse;
import com.hairsalonbookingapp.hairsalon.model.request.HairSalonServiceUpdate;
import com.hairsalonbookingapp.hairsalon.model.response.HairSalonServiceResponsePage;
import com.hairsalonbookingapp.hairsalon.repository.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HairSalonBookingAppService {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ModelMapper modelMapper;

    //tạo mới service -> MANAGER LÀM
    public HairSalonServiceResponse createNewService(HairSalonServiceRequest hairSalonServiceRequest){
        try{
            HairSalonService newService = modelMapper.map(hairSalonServiceRequest, HairSalonService.class);
            HairSalonService savedService = serviceRepository.save(newService);
            return modelMapper.map(savedService, HairSalonServiceResponse.class);
        } catch (Exception e){
            throw new DuplicateEntity("Duplicate name!");
        }
    }

    //update service -> MANAGER LÀM
    public HairSalonServiceResponse updateService(HairSalonServiceUpdate hairSalonServiceUpdate, long id){
        HairSalonService oldService = serviceRepository.findHairSalonServiceByIdAndIsAvailableTrue(id);
        if(oldService != null){
            try{
                if(!hairSalonServiceUpdate.getName().isEmpty()){       // NẾU ĐỂ TRỐNG TÊN
                    oldService.setName(hairSalonServiceUpdate.getName());
                }

                if(hairSalonServiceUpdate.getCost() > 0){               // NẾU NHẬP SỐ LỚN HƠN 0 THÌ LẤY SỐ ĐÓ, KO THÌ CỨ ĐÊ 0
                    oldService.setCost(hairSalonServiceUpdate.getCost());
                }

                if(hairSalonServiceUpdate.getTimeOfService() > 0 && hairSalonServiceUpdate.getTimeOfService() < 60){
                    oldService.setTimeOfService(hairSalonServiceUpdate.getTimeOfService());
                }

                if(!hairSalonServiceUpdate.getImage().isEmpty()){
                    oldService.setImage(hairSalonServiceUpdate.getImage());
                }

                if(!hairSalonServiceUpdate.getDescription().isEmpty()){
                    oldService.setDescription(hairSalonServiceUpdate.getDescription());
                }

                HairSalonService newService = serviceRepository.save(oldService);
                return modelMapper.map(newService, HairSalonServiceResponse.class);
            } catch(Exception e) {
                throw new DuplicateEntity("Duplicate name!");
            }
        } else {
            throw new EntityNotFoundException("Service not found!");
        }
    }

    //delete service  -> MANAGER LÀM
    public HairSalonServiceResponse deleteService(long id){
        HairSalonService oldService = serviceRepository.findHairSalonServiceByIdAndIsAvailableTrue(id);
        if(oldService != null){
            oldService.setAvailable(false);
            HairSalonService savedService = serviceRepository.save(oldService);
            return modelMapper.map(savedService, HairSalonServiceResponse.class);
        } else {
            throw new EntityNotFoundException("Service not found!");
        }
    }

    //restart service -> MANAGER LÀM
    public HairSalonServiceResponse startService(long id){
        HairSalonService oldService = serviceRepository.findHairSalonServiceById(id);
        if(oldService != null){
            oldService.setAvailable(true);
            HairSalonService savedService = serviceRepository.save(oldService);
            return modelMapper.map(savedService, HairSalonServiceResponse.class);
        } else {
            throw new EntityNotFoundException("Service not found!");
        }
    }

    //get all service  -> MANAGER LÀM
    public HairSalonServiceResponsePage getAllService(int page, int size){
        Page<HairSalonService> servicePage = serviceRepository
                .findHairSalonServicesByIsAvailableTrue(PageRequest.of(page, size));       // LẤY TẤT CẢ SERVICE KHẢ DỤNG
        HairSalonServiceResponsePage hairSalonServiceResponsePage = new HairSalonServiceResponsePage();
        List<HairSalonServiceResponse> hairSalonServiceResponseList = new ArrayList<>();

        for(HairSalonService hairSalonService : servicePage.getContent()){
            HairSalonServiceResponse hairSalonServiceResponse = modelMapper.map(hairSalonService, HairSalonServiceResponse.class);
            hairSalonServiceResponseList.add(hairSalonServiceResponse);
        }

        hairSalonServiceResponsePage.setContent(hairSalonServiceResponseList);
        hairSalonServiceResponsePage.setPageNumber(servicePage.getNumber());
        hairSalonServiceResponsePage.setTotalElements(servicePage.getTotalElements());
        hairSalonServiceResponsePage.setTotalPages(servicePage.getTotalPages());
        return hairSalonServiceResponsePage;
    }

    // VIEW AVAILABLE SERVICE -> CUSTOMER LÀM
    public List<HairSalonServiceResponse> getAllAvailableService(){
        List<HairSalonService> list = serviceRepository.findHairSalonServicesByIsAvailableTrue();       // LẤY TẤT CẢ SERVICE KHẢ DỤNG
        if(list != null){
            List<HairSalonServiceResponse> responseList = new ArrayList<>();
            for(HairSalonService hairSalonService : list){
                HairSalonServiceResponse hairSalonServiceResponse = modelMapper.map(hairSalonService, HairSalonServiceResponse.class);
                responseList.add(hairSalonServiceResponse);
            }
            return responseList;
        } else {
            throw new EntityNotFoundException("Service not found!");
        }
    }


    // HÀM LẤY SERVICE
    public HairSalonService getService(long serviceId) {
        HairSalonService service = serviceRepository.findHairSalonServiceByIdAndIsAvailableTrue(serviceId);
        if(service != null){
            return service;
        } else {
            throw new EntityNotFoundException("Service not found!");
        }
    }


}
