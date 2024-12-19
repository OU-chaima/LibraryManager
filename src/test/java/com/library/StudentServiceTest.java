package com.library;

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
            stmt.execute("DELETE FROM Borrows");
            // Suppression de toutes les lignes de la table Students
            stmt.execute("DELETE FROM Students");
            // Réinitialiser l'auto-incrémentation
            stmt.execute("ALTER TABLE Students AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddStudent() {
        // Utilisation d'un ID dynamique pour éviter les conflits
        int studentId = (int) (Math.random() * 1000); // Générer un ID aléatoire
        Student student = new Student(studentId, "Alice");
        studentService.addStudent(student);

        Optional<Student> retrievedStudentOpt = studentService.findStudentById(studentId);
        assertTrue(retrievedStudentOpt.isPresent(), "L'étudiant n'a pas été trouvé.");
        Student retrievedStudent = retrievedStudentOpt.get(); // Accéder à l'étudiant depuis l'Optional
        assertEquals("Alice", retrievedStudent.getName());
    }

    @Test
    void testUpdateStudent() {
        // Utilisation d'un ID dynamique pour éviter les conflits
        int studentId = (int) (Math.random() * 1000);
        Student student = new Student(studentId, "Alice");
        studentService.addStudent(student);

        Student updatedStudent = new Student(studentId, "Bob");
        studentService.updateStudent(updatedStudent);

        Optional<Student> retrievedStudentOpt = studentService.findStudentById(studentId);
        assertTrue(retrievedStudentOpt.isPresent(), "L'étudiant mis à jour n'a pas été trouvé.");
        Student retrievedStudent = retrievedStudentOpt.get(); // Accéder à l'étudiant depuis l'Optional
        assertEquals("Bob", retrievedStudent.getName());
    }

    @Test
    void testDeleteStudent() {
        // Utilisation d'un ID dynamique pour éviter les conflits
        int studentId = (int) (Math.random() * 1000);
        Student student = new Student(studentId, "Alice");
        studentService.addStudent(student);

        studentService.deleteStudent(studentId);

        Optional<Student> retrievedStudentOpt = studentService.findStudentById(studentId);
        assertFalse(retrievedStudentOpt.isPresent(), "L'étudiant n'a pas été supprimé.");
    }

    @Test
    void testGetAllStudents() {
        // Utilisation d'IDs dynamiques pour éviter les conflits
        int studentId1 = (int) (Math.random() * 1000);
        int studentId2 = (int) (Math.random() * 1000);
        Student student1 = new Student(studentId1, "Alice");
        Student student2 = new Student(studentId2, "Bob");
        studentService.addStudent(student1);
        studentService.addStudent(student2);

        assertEquals(2, studentService.getAllStudents().size());
    }
}
