package com.example.StudentAPI.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StudentErrorHandler {

    @ResponseBody
    @ExceptionHandler(MailTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMailTakenException(MailTakenException mailTakenException) {
        return mailTakenException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(StudentNotFoundException studentNotFoundException) {
        return studentNotFoundException.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MailFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMailFormatException(MailFormatException mailFormatException) {
        return mailFormatException.getMessage();
    }
}
