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
        if (studentDAO.getStudentById(student.getId()).isPresent()) {
            throw new IllegalArgumentException("Un étudiant avec l'ID " + student.getId() + " existe déjà.");
        }
        studentDAO.addStudent(student);
    }

    // Afficher tous les étudiants
    public void displayStudents() {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("Aucun étudiant trouvé.");
        } else {
            students.forEach(student ->
                    System.out.println("ID: " + student.getId() + " | Nom: " + student.getName())
            );
        }
    }

    // Trouver un étudiant par son ID
    public Optional<Student> findStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    // Mettre à jour un étudiant
    public void updateStudent(Student student) {
        if (studentDAO.getStudentById(student.getId()).isEmpty()) {
            throw new IllegalArgumentException("L'étudiant avec l'ID " + student.getId() + " n'existe pas.");
        }
        studentDAO.updateStudent(student);
    }

    // Supprimer un étudiant
    public void deleteStudent(int id) {
        if (studentDAO.getStudentById(id).isEmpty()) {
            throw new IllegalArgumentException("L'étudiant avec l'ID " + id + " n'existe pas.");
        }
        studentDAO.deleteStudent(id);
    }

    // Récupérer tous les étudiants
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }
}