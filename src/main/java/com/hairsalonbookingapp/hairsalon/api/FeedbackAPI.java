package com.hairsalonbookingapp.hairsalon.api;

import com.hairsalonbookingapp.hairsalon.model.response.FeedbackListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.FeedbackResponse;
import com.hairsalonbookingapp.hairsalon.model.request.RequestFeedback;
import com.hairsalonbookingapp.hairsalon.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api") // tao controller moi nho copy qua
public class FeedbackAPI {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping
//    @PreAuthorize("hasAuthority('customer')")
    public ResponseEntity createFeedback(@Valid @RequestBody RequestFeedback requestFeedback){
        FeedbackResponse feedbackResponse = feedbackService.createFeedback(requestFeedback);
        return ResponseEntity.ok(feedbackResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFeedback(@PathVariable int id){
        FeedbackResponse feedbackResponse = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(feedbackResponse);
    }

    @GetMapping
    public ResponseEntity getAllFeedback(@RequestParam int page, @RequestParam(defaultValue = "10") int size){
//        List<Feedback> feedbacks = feedbackService.getAllFeedback(page, size);
//        return ResponseEntity.ok(feedbacks);
        FeedbackListResponse feedbackListResponse = feedbackService.getAllFeedback(page, size);
        return ResponseEntity.ok(feedbackListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity getFeedbackInfo(@PathVariable int id){
        FeedbackResponse feedbackResponse = feedbackService.getInfoFeedback(id);
        return ResponseEntity.ok(feedbackResponse);
    }

}
