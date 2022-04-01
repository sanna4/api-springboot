package com.example.StudentAPI.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/students")
@RestController
public class StudentController {

    @Autowired
    StudentService service;

    @GetMapping
    public List<Student> getStudents() {
        return service.getStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable long id) {
        return service.getStudent(id);
     }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        service.addStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public long deleteStudent(@PathVariable long id) {
        service.deleteStudent(id);
        return id;
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable long id, @RequestBody Student student) {
        service.updateStudent(id, student);
    }


}
