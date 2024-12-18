package com.library.service;

import com.library.dao.StudentDAO;
import com.library.model.Student;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final StudentDAO studentDAO;

    // Constructeur
    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    

    // Ajouter un étudiant
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
    }

    // Display all students
    public void displayStudents() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            System.out.println("ID: " + student.getId() + " | Nom: " + student.getName());
        }
    }

    public Student findStudentById(int id) {
        Optional<Student> studentOpt = studentDAO.getStudentById(id);
        return studentOpt.orElse(null); // Retourne l'étudiant s'il existe, sinon retourne null.
    }
    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }
    public void deleteStudent(int id) {
        Optional<Student> student = studentDAO.getStudentById(id);
        if (student.isEmpty()) {
            throw new IllegalArgumentException("L'étudiant avec l'ID " + id + " n'existe pas.");
        }
        studentDAO.deleteStudent(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
}
