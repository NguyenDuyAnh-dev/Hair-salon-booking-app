package com.hairsalonbookingapp.hairsalon.service;

import com.hairsalonbookingapp.hairsalon.model.EmailDetail;
import com.hairsalonbookingapp.hairsalon.model.EmailDetailDeleteAppointment;
import com.hairsalonbookingapp.hairsalon.model.EmailDetailForEmployee;
import com.hairsalonbookingapp.hairsalon.model.EmailDetailForEmployeeSalary;
import com.hairsalonbookingapp.hairsalon.model.response.EmailDetailCreateAppointment;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmail(EmailDetail emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("button", "Go to our Hairsalon website");
            context.setVariable("link", emailDetail.getLink());
            String template = templateEngine.process("welcome-hairsalon", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }

    public void sendEmailResetPassword(EmailDetail emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("button", "Go to our Hairsalon website");
            context.setVariable("link", emailDetail.getLink());
            String template = templateEngine.process("reset-password", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }

    public void sendEmailCreateAppointment(EmailDetailCreateAppointment emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("appointmentID", emailDetail.getAppointmentId());
            context.setVariable("serviceName", emailDetail.getServiceName());
            context.setVariable("stylistName", emailDetail.getNameStylist());
            context.setVariable("date", emailDetail.getDay());
            context.setVariable("time", emailDetail.getStartHour());
            String template = templateEngine.process("appointment-create", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }

    public void sendEmailChangedAppointment(EmailDetailDeleteAppointment emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("appointmentID", emailDetail.getAppointmentId());
            context.setVariable("serviceName", emailDetail.getServiceName());
            context.setVariable("stylistName", emailDetail.getNameStylist());
            context.setVariable("date", emailDetail.getDay());
            context.setVariable("time", emailDetail.getStartHour());
            String template = templateEngine.process("welcome-hairsalon", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }

    public void sendEmailToEmployee(EmailDetailForEmployee emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("button", "Go to our Hairsalon website");
            context.setVariable("link", emailDetail.getLink());
            String template = templateEngine.process("reset-password", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }

    public void sendEmailToEmployeeSalary(EmailDetailForEmployeeSalary emailDetail) {
        try{
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver().getName());
            context.setVariable("button", "Go to our Hairsalon website");
            context.setVariable("link", emailDetail.getLink());
            context.setVariable("sumsalary", emailDetail.getSumSalary());
            String template = templateEngine.process("Salary", context);
            // Creating a simple mail message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            // Setting up necessary details
            mimeMessageHelper.setFrom("hairsalon459@gmail.com");
            mimeMessageHelper.setTo(emailDetail.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            javaMailSender.send(mimeMessage);
        }catch (Exception e) {
            System.out.println("error sent email");
        }

    }



}
