package com.example.StudentAPI.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailFormatException extends RuntimeException{
    MailFormatException() {
        super("Invalid mail format");
    }
}
