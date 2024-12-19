package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import com.library.util.DbConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
        cleanDatabase();
    }

    private void cleanDatabase() {
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Students");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);

        Student retrievedStudent = studentService.findStudentById(1);
        assertNotNull(retrievedStudent);
        assertEquals("Alice", retrievedStudent.getName());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);

        Student updatedStudent = new Student(1, "Bob");
        studentService.updateStudent(updatedStudent);

        Student retrievedStudent = studentService.findStudentById(1);
        assertNotNull(retrievedStudent);
        assertEquals("Bob", retrievedStudent.getName());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);

        studentService.deleteStudent(1);

        assertNull(studentService.findStudentById(1));
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student(1, "Alice");
        Student student2 = new Student(2, "Bob");
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        assertEquals(2, studentService.getAllStudents().size());
    }
}