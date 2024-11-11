package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.entity.AccountForCustomer;
import com.hairsalonbookingapp.hairsalon.entity.AccountForEmployee;
import com.hairsalonbookingapp.hairsalon.exception.Duplicate;
import com.hairsalonbookingapp.hairsalon.model.request.RegisterRequestForCustomer;
import com.hairsalonbookingapp.hairsalon.model.request.RegisterRequestForEmloyee;
import com.hairsalonbookingapp.hairsalon.model.response.AccountForCustomerResponse;
import com.hairsalonbookingapp.hairsalon.model.response.AccountForEmployeeResponse;
import com.hairsalonbookingapp.hairsalon.repository.AccountForCustomerRepository;
import com.hairsalonbookingapp.hairsalon.repository.EmployeeRepository;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Optional;

import static org.testng.Assert.*;

public class AuthenticationServiceTest {

    @Mock
    private AccountForCustomerRepository accountForCustomerRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper; // Thêm mock cho ModelMapper

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountForCustomerRepository.deleteAll();
    }


    @Test
    public void testRegisterForCustomerSuccess() {
        // Tạo đối tượng AccountForCustomer với dữ liệu mẫu
        AccountForCustomer accountForCustomer = new AccountForCustomer();
        accountForCustomer.setPhoneNumber("0799128953");
        accountForCustomer.setEmail("string@gmail.com");
        accountForCustomer.setName("anh");
        accountForCustomer.setPassword("123456");

        // Tạo đối tượng RegisterRequestForCustomer từ dữ liệu mẫu
        RegisterRequestForCustomer registerRequestForCustomer = new RegisterRequestForCustomer();
        registerRequestForCustomer.setPhoneNumber(accountForCustomer.getPhoneNumber());
        registerRequestForCustomer.setEmail(accountForCustomer.getEmail());
        registerRequestForCustomer.setName(accountForCustomer.getName());
        registerRequestForCustomer.setPassword(accountForCustomer.getPassword());

        // Mô phỏng hành vi của modelMapper khi chuyển đổi DTO sang Entity
        Mockito.when(modelMapper.map(ArgumentMatchers.eq(registerRequestForCustomer), ArgumentMatchers.eq(AccountForCustomer.class))).thenReturn(accountForCustomer);

        // Mô phỏng hành vi của passwordEncoder
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");

        // Mô phỏng hành vi của repository khi lưu đối tượng
        Mockito.when(accountForCustomerRepository.save(ArgumentMatchers.any(AccountForCustomer.class))).thenReturn(accountForCustomer);

        // Tạo đối tượng AccountForCustomerResponse giả định
        AccountForCustomerResponse response = new AccountForCustomerResponse();
        response.setPhoneNumber(accountForCustomer.getPhoneNumber());
        response.setEmail(accountForCustomer.getEmail());
        response.setName(accountForCustomer.getName());


        // Mô phỏng hành vi của modelMapper khi chuyển đổi Entity sang Response DTO
        Mockito.when(modelMapper.map(ArgumentMatchers.eq(accountForCustomer), ArgumentMatchers.eq(AccountForCustomerResponse.class))).thenReturn(response);
        AccountForCustomerResponse account = authenticationService.register(registerRequestForCustomer);
        ResponseEntity resp = ResponseEntity.ok(account);

        // Assert
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getEmail(), "string@gmail.com");
        Assert.assertEquals(response.getPhoneNumber(), "0799128953");
        Assert.assertEquals(response.getName(), "anh");
        Assert.assertEquals(resp.getStatusCode(), HttpStatus.OK);

    }

    @Test(expectedExceptions = Duplicate.class, expectedExceptionsMessageRegExp = ".*Phone number exist!.*")
    public void testRegisterWithExistingPhoneNumber() {
        // Tạo đối tượng RegisterRequestForCustomer với số điện thoại đã tồn tại
        RegisterRequestForCustomer registerRequest = new RegisterRequestForCustomer();
        registerRequest.setEmail("new@example.com");
        registerRequest.setPhoneNumber("0799128953"); // Số điện thoại đã tồn tại
        registerRequest.setName("anh");
        registerRequest.setPassword("123456");

        // Mô phỏng hành vi của modelMapper
        AccountForCustomer account = new AccountForCustomer();
        account.setEmail(registerRequest.getEmail());
        account.setPhoneNumber(registerRequest.getPhoneNumber());
        account.setName(registerRequest.getName());
        account.setPassword(registerRequest.getPassword());
        Mockito.when(modelMapper.map(ArgumentMatchers.any(RegisterRequestForCustomer.class), ArgumentMatchers.any(Class.class))).thenReturn(account);

        // Mô phỏng hành vi của repository
        Mockito.when(accountForCustomerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        Mockito.when(accountForCustomerRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())).thenReturn(true);

        // Gọi phương thức register và xác nhận ngoại lệ Duplicate
        authenticationService.register(registerRequest);
    }

    @Test(expectedExceptions = Duplicate.class, expectedExceptionsMessageRegExp = ".*Email exist!.*")
    public void testRegisterWithExistingEmail() {
        // Tạo đối tượng RegisterRequestForCustomer với email đã tồn tại
        RegisterRequestForCustomer registerRequest = new RegisterRequestForCustomer();
        registerRequest.setEmail("existing@example.com");
        registerRequest.setPhoneNumber("0799128953");
        registerRequest.setName("anh");
        registerRequest.setPassword("123456");

        // Mô phỏng hành vi của modelMapper
        AccountForCustomer account = new AccountForCustomer();
        account.setEmail(registerRequest.getEmail());
        account.setPhoneNumber(registerRequest.getPhoneNumber());
        account.setName(registerRequest.getName());
        account.setPassword(registerRequest.getPassword());
        Mockito.when(modelMapper.map(ArgumentMatchers.any(RegisterRequestForCustomer.class), ArgumentMatchers.any(Class.class))).thenReturn(account);

        // Mô phỏng hành vi của repository
        Mockito.when(accountForCustomerRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        Mockito.when(accountForCustomerRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())).thenReturn(false);

        // Gọi phương thức register và xác nhận ngoại lệ Duplicate
        authenticationService.register(registerRequest);
    }

    @Test(expectedExceptions = Duplicate.class, expectedExceptionsMessageRegExp = "Email invalid!")
    public void testRegisterWithInvalidEmailFormat() {
        // Tạo đối tượng RegisterRequestForCustomer với email không hợp lệ
        RegisterRequestForCustomer registerRequest = new RegisterRequestForCustomer();
        registerRequest.setEmail("invalid-email"); // Email không hợp lệ
        registerRequest.setPhoneNumber("0799128953");
        registerRequest.setName("anh");
        registerRequest.setPassword("123456");

        // Gọi phương thức register và xác nhận ngoại lệ Duplicate
        authenticationService.register(registerRequest);
    }

    @Test(expectedExceptions = Duplicate.class, expectedExceptionsMessageRegExp = "Phone number is invalid!")
    public void testRegisterWithInvalidPhoneNumberFormat() {
        // Tạo đối tượng RegisterRequestForCustomer với số điện thoại không hợp lệ
        RegisterRequestForCustomer registerRequest = new RegisterRequestForCustomer();
        registerRequest.setEmail("valid@example.com");
        registerRequest.setPhoneNumber("invalid-phone"); // Số điện thoại không hợp lệ
        registerRequest.setName("anh");
        registerRequest.setPassword("123456");

        // Gọi phương thức register và xác nhận ngoại lệ Duplicate
        authenticationService.register(registerRequest);
    }

    @Test(expectedExceptions = Duplicate.class, expectedExceptionsMessageRegExp = "Email invalid! Phone number is invalid!")
    public void testRegisterWithInvalidEmailAndPhoneNumberFormat() {
        // Tạo đối tượng RegisterRequestForCustomer với email và số điện thoại không hợp lệ
        RegisterRequestForCustomer registerRequest = new RegisterRequestForCustomer();
        registerRequest.setEmail("invalid-email"); // Email không hợp lệ
        registerRequest.setPhoneNumber("invalid-phone"); // Số điện thoại không hợp lệ
        registerRequest.setName("anh");
        registerRequest.setPassword("123456");

        // Gọi phương thức register và xác nhận ngoại lệ Duplicate
        authenticationService.register(registerRequest);
    }


    @Test
    public void testRegisterEmployeeSuccess() {
        // Tạo đối tượng AccountForCustomer với dữ liệu mẫu
        AccountForEmployee accountForEmployee = new AccountForEmployee();
        accountForEmployee.setPhoneNumber("0799128954");
        accountForEmployee.setEmail("string@gmail.com");
        accountForEmployee.setName("anh");
        accountForEmployee.setPassword("123456");
        accountForEmployee.setRole("Stylist");
        accountForEmployee.setUsername("anh123");

        // Tạo đối tượng RegisterRequestForCustomer từ dữ liệu mẫu
        RegisterRequestForEmloyee registerRequestForEmloyee = new RegisterRequestForEmloyee();
        registerRequestForEmloyee.setPhoneNumber(accountForEmployee.getPhoneNumber());
        registerRequestForEmloyee.setEmail(accountForEmployee.getEmail());
        registerRequestForEmloyee.setName(accountForEmployee.getName());
        registerRequestForEmloyee.setPassword(accountForEmployee.getPassword());
        registerRequestForEmloyee.setRole(accountForEmployee.getRole());
        registerRequestForEmloyee.setUsername(accountForEmployee.getUsername());

        // Mô phỏng hành vi của modelMapper khi chuyển đổi DTO sang Entity
        Mockito.when(modelMapper.map(ArgumentMatchers.eq(registerRequestForEmloyee), ArgumentMatchers.eq(AccountForEmployee.class))).thenReturn(accountForEmployee);

        // Mô phỏng hành vi của passwordEncoder
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("encodedPassword");


        // Mô phỏng hành vi của repository khi lưu đối tượng
        Mockito.when(employeeRepository.save(ArgumentMatchers.any(AccountForEmployee.class))).thenReturn(accountForEmployee);


        // Tạo đối tượng AccountForCustomerResponse giả định
        AccountForEmployeeResponse response = new AccountForEmployeeResponse();
        response.setPhoneNumber(accountForEmployee.getPhoneNumber());
        response.setEmail(accountForEmployee.getEmail());
        response.setName(accountForEmployee.getName());
        response.setRole(accountForEmployee.getRole());
        response.setUsername(accountForEmployee.getUsername());
        // Giả sử bạn có thêm các trường khác trong response

        // Mô phỏng hành vi của modelMapper khi chuyển đổi Entity sang Response DTO
        Mockito.when(modelMapper.map(ArgumentMatchers.eq(accountForEmployee), ArgumentMatchers.eq(AccountForEmployeeResponse.class))).thenReturn(response);

        // Gọi phương thức register trong AuthenticationService
        AccountForEmployeeResponse registeredAccountForEmployee = authenticationService.register(registerRequestForEmloyee);
    }

    @Test
    public void testLoginForCustomer() {
    }

    @Test
    public void testLoginForEmployee() {
    }
}