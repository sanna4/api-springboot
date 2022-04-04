package com.example.StudentAPI.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundException());
    }

    public Student addStudent(Student student) {
        try {
            checkMail(student.getMail());
        } catch (RuntimeException e)  { throw e; }
        return studentRepo.save(student);
    }

    public void deleteStudent(long id) {
        studentRepo.delete(studentRepo.findById(id)
                        .orElseThrow(() -> new StudentNotFoundException()));
    }

    public void updateStudent(long id, Student student) {
        try {
            checkMail(student.getMail());
        } catch (RuntimeException e) { throw e; }
        studentRepo.findById(id).map(
                s -> {
                    s.setName(student.getName());
                    s.setMail(student.getMail());
                    return studentRepo.save(s);
                }
        ).orElseThrow(() -> new StudentNotFoundException());
    }

    private void checkMail(String mail) {
        String[] split = mail.split("@");
        if (mail.indexOf("@") == -1 || split[0].length() < 5 || !split[1].equals("student.com")) {
            throw new MailFormatException();
        }
        if (studentRepo.findStudentByMail(mail).isPresent()) {
            throw new MailTakenException();
        }
    }
}
