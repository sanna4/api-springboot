package com.example.StudentAPI.student;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Student {
    @Id @Getter @Setter @GeneratedValue private long id;
    @Getter @Setter private String name;
    @Getter @Setter private String mail;

    public Student() {}

    public Student(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
}
