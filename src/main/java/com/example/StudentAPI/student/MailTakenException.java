package com.example.StudentAPI.student;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailTakenException extends RuntimeException{
    public MailTakenException(String mail) {
        super(mail + " mail has already been used by another student...");
    }
}
