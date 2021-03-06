package com.example.StudentAPI.student;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest
class StudentControllerTest {

    @MockBean
    private StudentService service;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void shouldReturnStudentList() throws Exception {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("Mario Rossi", "mario.rossi@student.com"));
        when(service.getStudents())
                .thenReturn(students);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Mario Rossi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mail", Matchers.is("mario.rossi@student.com")));
    }

    @Test
    void shuoldReturnStudent() throws Exception {
        when(service.getStudent(1))
                .thenReturn(new Student("Mario Rossi", "mario.rossi@student.com"));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/students/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Mario Rossi")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail", Matchers.is("mario.rossi@student.com")));
    }

    @Test
    void shouldAddStudent() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mario Rossi\", \"mail\":\"mario.rossi@student.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(service).addStudent(any(Student.class));
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/v1/students/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("1"));
    }
    @Test
    void shouldUpdateStudent() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mario Rossi\", \"mail\":\"mario.rossi@student.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("1"));
    }

    @Test
    void shouldRespondWithNotFoundStatus() throws Exception {
        when(service.getStudent(anyLong())) //delete, put
                .thenThrow(new StudentNotFoundException());
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/students/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Student not found"));
    }

    @Test
    void shouldRespondWithBadRequestStatus_MailFormat() throws Exception {
        when(service.addStudent(any(Student.class))) //put
                .thenThrow(new MailFormatException());
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mocking\", \"mail\":\"mock@student.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid mail format"));
    }

    @Test
    void shouldRespondWithBadRequest_MailTaken() throws Exception {
        when(service.addStudent(any(Student.class)))
                .thenThrow(new MailTakenException());
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Mocking\", \"mail\":\"mock@student.com\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Mail already used"));
    }


}