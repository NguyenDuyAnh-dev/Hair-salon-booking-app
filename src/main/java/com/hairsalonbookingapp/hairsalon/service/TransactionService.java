package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.*;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.CompleteAppointmentRequest;
import com.hairsalonbookingapp.hairsalon.model.request.RequestTransaction;
import com.hairsalonbookingapp.hairsalon.model.response.TransactionListResponse;
import com.hairsalonbookingapp.hairsalon.model.response.TransactionResponse;
import com.hairsalonbookingapp.hairsalon.repository.AppointmentRepository;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import com.hairsalonbookingapp.hairsalon.repository.PaymentRepository;
import com.hairsalonbookingapp.hairsalon.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    PaymentRepository paymentRepository;





    @Autowired
    ModelMapper modelMapper;
    // create Transaction
    public TransactionResponse createTransactionInCast(long appointmentId) {
        // Lấy thông tin appointment và nhân viên
        Appointment appointment = appointmentService.completeAppointmentById(appointmentId);
        AccountForEmployee accountForEmployee = authenticationService.getCurrentAccountForEmployee();

        if (appointment == null || accountForEmployee == null) {
            throw new IllegalStateException("Thông tin cuộc hẹn hoặc nhân viên không hợp lệ.");
        }

        try {
            // Tạo Payment
            Payment payment = new Payment();
            payment.setAppointment(appointment);
            payment.setCreateAt(new Date());
            payment.setTypePayment("Cash");

            // Tạo Transaction
            Transaction transaction = new Transaction();
            transaction.setMoney(appointment.getCost());
            transaction.setDate(new Date());
            transaction.setEmployee(accountForEmployee);
            transaction.setCustomer(appointment.getAccountForCustomer());
            transaction.setTransactionType("Cash");
            transaction.setPayment(payment);
            transaction.setStatus("Success");
            transaction.setDescription("Thanh toán trực tiếp tại quầy");

            // Thêm Transaction vào Payment
            payment.getTransactions().add(transaction);

            // Lưu Payment (giao dịch sẽ được lưu cùng do CascadeType.ALL)
            paymentRepository.save(payment);
            return null;

        } catch (Exception e) {
            e.printStackTrace(); // Ghi log chi tiết để kiểm tra lỗi
            throw new RuntimeException("Lỗi khi lưu giao dịch.");
        }
    }


    //delete Transaction
    public TransactionResponse deleteTransaction(int transactionId){
        // tim toi id ma FE cung cap
        Transaction transactionNeedDelete = transactionRepository.findTransactionByTransactionId(transactionId);
        if(transactionNeedDelete == null){
            throw new Duplicate("Feedback not found!"); // dung tai day
        }

        transactionNeedDelete.setDeleted(true);
        Transaction deletedTransaction = transactionRepository.save(transactionNeedDelete);
        return modelMapper.map(deletedTransaction, TransactionResponse.class);
    }

    // show list of Transaction
    public TransactionListResponse getAllTransaction(int page, int size){
//        List<Transaction> transactions = transactionRepository.findTransactionsByIsDeletedFalse();
//        return transactions;
        Page transactionPage = transactionRepository.findTransactionsByIsDeletedFalseOrderByDateDesc(PageRequest.of(page, size));
        TransactionListResponse transactionListResponse = new TransactionListResponse();
        transactionListResponse.setTotalPage(transactionPage.getTotalPages());
        transactionListResponse.setContent(transactionPage.getContent());
        transactionListResponse.setPageNumber(transactionPage.getNumber());
        transactionListResponse.setTotalElement(transactionPage.getTotalElements());
        return transactionListResponse;
    }

    //GET PROFILE SalaryMonth
    public TransactionResponse getInfoTransaction(int id){
        Transaction transaction = transactionRepository.findTransactionByTransactionId(id);
        return modelMapper.map(transaction, TransactionResponse.class);
    }
}
