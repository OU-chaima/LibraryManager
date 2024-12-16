package com.library;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private StudentService studentService;
    private StudentDAO studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAO();
        studentService = new StudentService();
    }

    @Test
    void testAddStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);

        // Vérifier qu'il y a bien 1 étudiant dans la liste
        assertEquals(1, studentDAO.getAllStudents().size());

        // Vérifier si l'Optional contient un étudiant et récupérer le nom
        Optional<Student> studentOpt = studentDAO.getStudentById(1);
        assertTrue(studentOpt.isPresent()); // Vérifier que l'Optional contient bien un étudiant
        assertEquals("Alice", studentOpt.get().getName()); // Récupérer l'étudiant si l'Optional est présent
    }


    @Test
    void testUpdateStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);
        Student student2 = new Student(1, "Bob");
        studentService.updateStudent(student2); // Modification effectuée sur student2

        // Vérification après la mise à jour
        assertTrue(studentDAO.getStudentById(1).isPresent());  // Vérifier que l'étudiant existe
        assertEquals("Bob", studentDAO.getStudentById(1).get().getName());  // Vérifier le nom de l'étudiant
    }


    @Test
    void testDeleteStudent() {
        Student student = new Student(1, "Alice");
        studentService.addStudent(student);
        studentService.deleteStudent(1);

        // Vérifier si l'Optional est vide
        assertTrue(studentDAO.getStudentById(1).isEmpty());
    }

    @Test
    void testGetAllStudents() {
        Student student=new Student(1, "Alice");
        studentService.addStudent(student);
        Student student2=new Student(2, "Bob");
        studentService.addStudent(student2);
        assertEquals(2, studentDAO.getAllStudents().size());
    }
}
