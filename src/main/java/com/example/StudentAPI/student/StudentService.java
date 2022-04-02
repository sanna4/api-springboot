package com.example.StudentAPI.student;

import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;

import javax.swing.text.html.Option;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    public List<Student> getStudents() {
        return studentRepo.findAll();
    }

    public Student getStudent(long id) {
        return studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student addStudent(Student student) {
        if (this.isMailFormatValid(student.getMail())) {
            Optional<Student> valid = studentRepo.findStudentByMail(student.getMail());
            if (!valid.isPresent()) {
                return studentRepo.save(student);
            } else {
                throw new MailTakenException(student.getMail());
            }
        } else {
            throw new MailFormatException(student.getMail());
        }
    }

    public void deleteStudent(long id) {
        studentRepo.delete(studentRepo.findById(id)
                        .orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public void updateStudent(long id, Student student) {
        if (this.isMailFormatValid(student.getMail())) {
            studentRepo.findById(id).map(
                            s -> {
                                s.setName(student.getName());
                                s.setMail(student.getMail());
                                return studentRepo.save(s);
                            })
                    .orElseThrow(() -> new StudentNotFoundException(id));
        } else {
            throw new MailFormatException(student.getMail());
        }

    }

    private boolean isMailFormatValid(String mail) {
        if (mail.indexOf("@") == -1) {
            return false;
        }
        String[] split = mail.split("@");
        return split[0].length() > 4 && split[1].equals("student.com");
    }
}
