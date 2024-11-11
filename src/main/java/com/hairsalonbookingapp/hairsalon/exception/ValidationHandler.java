package com.hairsalonbookingapp.hairsalon.exception;

import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice
public class ValidationHandler {
    //dinh nghia cho moi khi chay gap exception nao do
    @ExceptionHandler(MethodArgumentNotValidException.class) //MethodArgumentNotValidException la loi khi ng dung nhap sai
    @ResponseStatus(HttpStatus.BAD_REQUEST) // dua ra ma loi cho FE biet BAD REQUEST la dau va nguoi dung nhap sai FE coi lai
    public ResponseEntity handleValidation(MethodArgumentNotValidException exception){
        String message = "";
        //cu 1 thuoc tinh loi => su li
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            //FieldError la name, studentCode, score
            message += fieldError.getField() + ":" + fieldError.getDefaultMessage();
        }
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateRequestException.class) //MethodArgumentNotValidException la loi khi ng dung nhap sai
    @ResponseStatus(HttpStatus.BAD_REQUEST) // dua ra ma loi cho FE biet BAD REQUEST la dau va nguoi dung nhap sai FE coi lai
    public ResponseEntity handleValidation(AccountNotFoundException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    public ResponseEntity handleValidation(Duplicate exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity handleValidation(UpdatedException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity handleValidation(CreateException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity handleValidation(AccountBlockedException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
    public ResponseEntity handleValidation(NoContentException exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(Exception.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)// cái này báo cho front end biết mã lỗi status: INPUT đầu vào sai, front end check lại
    public ResponseEntity handleGenericException(Exception exception){
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
