package com.example.StudentAPI;


import com.example.StudentAPI.student.Student;
import com.example.StudentAPI.student.StudentController;
import com.example.StudentAPI.student.StudentRepo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class StudentApiApplicationTests {

	@Test
	public void givenStudentDoesNotExist_whenGetStudent_then404() {

	}

}
