package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BorrowDAO {

    private static final Logger logger = Logger.getLogger(BorrowDAO.class.getName());

    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM Borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int borrowId = rs.getInt("id");
                int studentId = rs.getInt("student_id");
                int bookId = rs.getInt("book_id");
                Date borrowDate = rs.getDate("borrowDate");
                Date returnDate = rs.getDate("returnDate");

                Student student = getStudentById(studentId);
                Book book = getBookById(bookId);

                Borrow borrow = new Borrow(borrowId, student, book, borrowDate, returnDate);
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            logger.severe("Erreur lors de la récupération des emprunts : " + e.getMessage());
        }
        return borrows;
    }

    public void addBorrow(Borrow borrow) {
        if (!borrow.getBook().isAvailable()) {
            throw new IllegalArgumentException("Le livre n'est pas disponible.");
        }

        String query = "INSERT INTO Borrows (student_id, book_id, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));
            stmt.setDate(4, borrow.getReturnDate() != null
                    ? new java.sql.Date(borrow.getReturnDate().getTime())
                    : null);

            stmt.executeUpdate();
            borrow.getBook().setAvailable(false);
            new BookDAO().update(borrow.getBook());
        } catch (SQLException e) {
            logger.severe("Erreur lors de l'ajout de l'emprunt : " + e.getMessage());
        }
    }

    public void deleteAllBorrows() {
        String query = "DELETE FROM Borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            logger.severe("Erreur lors de la suppression des emprunts : " + e.getMessage());
        }
    }

    public void updateBorrow(Borrow borrow) {
        String query = "UPDATE Borrows SET returnDate = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(borrow.getReturnDate().getTime()));
            stmt.setInt(2, borrow.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Erreur lors de la mise à jour de l'emprunt : " + e.getMessage());
        }
    }

    private Student getStudentById(int studentId) {
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.getStudentById(studentId).orElse(null);
    }

    private Book getBookById(int bookId) {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.getBookById(bookId).orElse(null);
    }
}
