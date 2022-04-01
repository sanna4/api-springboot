package com.example.StudentAPI.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailFormatException extends RuntimeException{
    MailFormatException(String mail) {
        super(mail + " doesn't follow our mail policy\n" +
                "Username must be at least 5 characters\n" +
                "Domain must be 'student.com'");
    }
}
