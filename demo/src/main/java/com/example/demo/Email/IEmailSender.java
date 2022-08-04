package com.example.demo.Email;

import javax.mail.MessagingException;

public interface IEmailSender {
    void send(String to, String subject,String body) throws MessagingException;
}
