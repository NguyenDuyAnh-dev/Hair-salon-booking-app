package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.Feedback;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.model.response.FeedbackListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.FeedbackResponse;
import com.hairsalonbookingapp.hairsalon.model.request.RequestFeedback;
import com.hairsalonbookingapp.hairsalon.repository.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ModelMapper modelMapper;
    // create feedback
    public FeedbackResponse createFeedback(RequestFeedback requestFeedback){
        Feedback feedback = modelMapper.map(requestFeedback, Feedback.class);
        try{
            AccountForCustomer accountForCustomer = authenticationService.getCurrentAccountForCustomer();
            if(accountForCustomer == null){
                throw new Duplicate("No current customer found.");
            }
            feedback.setCustomer(accountForCustomer);
            feedback.setCreatedAt(new Date());
            Feedback newFeedback = feedbackRepository.save(feedback);
            return modelMapper.map(newFeedback, FeedbackResponse.class);
        } catch (Exception e) {
            if(e.getMessage().contains(feedback.getStar() + "")){
                throw new Duplicate("duplicate start! ");
            }
            else if (e.getMessage().contains(feedback.getCustomer() + "")) {
                System.out.println("Error creating SoftwareSupportApplication: " + e.getMessage());
                throw new Duplicate("duplicate Customer! ");
            }
        }
        return null;
    }


    //delete feedback
    public FeedbackResponse deleteFeedback(int feedbackId){
        // tim toi id ma FE cung cap
        Feedback feedbackNeedDelete = feedbackRepository.findFeedbackByFeedbackId(feedbackId);
        if(feedbackNeedDelete == null){
            throw new Duplicate("Feedback not found!"); // dung tai day
        }

        feedbackNeedDelete.setDeleted(true);
        Feedback deletedFeedback = feedbackRepository.save(feedbackNeedDelete);
        return modelMapper.map(deletedFeedback, FeedbackResponse.class);
    }

    // show list of feedback
    public FeedbackListResponse getAllFeedback(int page, int size){
//        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByIsDeletedFalse();
//        return feedbacks;
//        return feedbackRepository.findFeedbacksByIsDeletedFalse(PageRequest.of(page, size));
        Page feedbackPage = feedbackRepository.findFeedbacksByIsDeletedFalseOrderByCreatedAtDesc(PageRequest.of(page, size));
        FeedbackListResponse feedbackListResponse = new FeedbackListResponse();
        feedbackListResponse.setTotalPage(feedbackPage.getTotalPages());
        feedbackListResponse.setContent(feedbackPage.getContent());
        feedbackListResponse.setPageNumber(feedbackPage.getNumber());
        feedbackListResponse.setTotalElement(feedbackPage.getTotalElements());
        return feedbackListResponse;
    }


    //GET PROFILE Feedback
    public FeedbackResponse getInfoFeedback(int id){
        Feedback feedback = feedbackRepository.findFeedbackByFeedbackId(id);
        return modelMapper.map(feedback, FeedbackResponse.class);
    }

}
