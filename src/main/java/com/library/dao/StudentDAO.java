package com.library.dao;

import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDAO {
    private static final Logger LOGGER = Logger.getLogger(StudentDAO.class.getName());

    // Ajouter un étudiant
    public void addStudent(Student student) {
        String query = "INSERT INTO Students (id, name) VALUES (?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, student.getId());
            statement.setString(2, student.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'étudiant avec ID: " + student.getId(), e);
            throw new RuntimeException("Erreur lors de l'ajout de l'étudiant.", e);
        }
    }

    // Récupérer un étudiant par ID
    public Optional<Student> getStudentById(int id) {
        String query = "SELECT * FROM Students WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToStudent(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'étudiant avec ID: " + id, e);
        }
        return Optional.empty();
    }

    // Récupérer tous les étudiants
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(mapToStudent(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des étudiants", e);
        }
        return students;
    }

    // Mettre à jour un étudiant
    public void updateStudent(Student student) {
        String query = "UPDATE Students SET name = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getName());
            statement.setInt(2, student.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("Aucun étudiant trouvé avec l'ID: " + student.getId());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'étudiant avec ID: " + student.getId(), e);
            throw new RuntimeException("Erreur lors de la mise à jour de l'étudiant.", e);
        }
    }

    // Supprimer un étudiant
    public void deleteStudent(int id) {
        String query = "DELETE FROM Students WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("Aucun étudiant trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'étudiant avec ID: " + id, e);
            throw new RuntimeException("Erreur lors de la suppression de l'étudiant.", e);
        }
    }

    // Mapper un ResultSet en objet Student
    private Student mapToStudent(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Student(id, name);
    }
}