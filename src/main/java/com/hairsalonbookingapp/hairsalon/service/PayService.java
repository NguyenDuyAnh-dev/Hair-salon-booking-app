package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.*;
import com.hairsalonbookingapp.hairsalon.exception.EntityNotFoundException;
import com.hairsalonbookingapp.hairsalon.model.request.CompleteAppointmentRequest;
import com.hairsalonbookingapp.hairsalon.model.request.RequestAppointment;
import com.hairsalonbookingapp.hairsalon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PayService {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountForCustomerRepository accountForCustomerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    PaymentRepository paymentRepository;

    public String createUrl(long orderRequest) throws  Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);

        // code cua minh
//        Appointment appointment = appointmentService.completeAppointmentById(orderRequest);
        Appointment appointment = appointmentRepository.findAppointmentByAppointmentId(orderRequest);
        double money = appointment.getCost() * 100;
        String amount = String.valueOf((int) money);

        String tmnCode = "OAXJYXKZ";
        String secretKey = "4MKK3NOKE1SOCD9YNKN9BOKKV3BQJBFU";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:5173/staff_page/paymentSuccessful?orderID=" + appointment.getAppointmentId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();

        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", appointment.getAppointmentId() + "");
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + appointment.getAppointmentId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount",amount);

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }

    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public Transaction createTransactionSuccess(long appointmentId) {
        // Tìm appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found!"));

            // Tạo payment
            Payment payment = new Payment();
            payment.setAppointment(appointment);
            payment.setCreateAt(new Date());
            payment.setTypePayment("Banking");

            List<Transaction> transactions = new ArrayList<>();

            AccountForEmployee employee = authenticationService.getCurrentAccountForEmployee();
            if (employee == null) {
                throw new IllegalStateException("Không tìm thấy nhân viên thực hiện giao dịch.");
            }

            AccountForCustomer accountForCustomer = appointment.getAccountForCustomer();
            // Tạo giao dịch cho thanh toán của khách hàng
            Transaction transaction = new Transaction();

            // Tạo giao dịch cho admin
            Transaction transaction1 = new Transaction();
//        AccountForEmployee employee = appointment.getSlot().getShiftEmployee().getAccountForEmployee();
            transaction1.setDate(new Date());
            transaction1.setEmployee(employee);
            transaction1.setMoney(appointment.getCost());
            transaction1.setCustomer(accountForCustomer);
            transaction1.setTransactionType("Banking");
            transaction1.setPayment(payment);
            transaction1.setStatus("Success");
            transaction1.setDescription("Chuyển từ khách hàng tới salon");
            transactions.add(transaction1);

            // Thiết lập giao dịch trong payment
            payment.setTransactions(transactions);

            // Lưu payment trước
            paymentRepository.save(payment);

            // Không cần lưu giao dịch riêng biệt nếu đã sử dụng CascadeType.ALL
            // transactionRepository.saveAll(transactions); // Không cần thiết nếu CascadeType.ALL đã được sử dụng
        return transaction1;
        }


    }


