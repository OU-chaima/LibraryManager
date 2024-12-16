
package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    // Récupérer tous les emprunts
    public List<Borrow> getAllBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        String query = "SELECT * FROM Borrows";
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int borrowId = rs.getInt("id");
                int studentId = rs.getInt("student_id");  // Récupérer l'ID de l'étudiant
                int bookId = rs.getInt("book_id");        // Récupérer l'ID du livre
                Date borrowDate = rs.getDate("borrowDate");
                Date returnDate = rs.getDate("returnDate");

                // Charger l'étudiant et le livre à partir de leurs ID
                Student student = getStudentById(studentId);
                Book book = getBookById(bookId);

                Borrow borrow = new Borrow(borrowId, student, book, borrowDate, returnDate);
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    // Méthode pour récupérer un étudiant par son ID
    private Student getStudentById(int studentId) {
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.getStudentById(studentId).orElse(null);
    }

    // Méthode pour récupérer un livre par son ID
    private Book getBookById(int bookId) {
        BookDAO bookDAO = new BookDAO();
        return bookDAO.getBookById(bookId).orElse(null);
    }

    public void addBorrow(Borrow borrow) {
        String query = "INSERT INTO Borrows (student_id, book_id, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));

            // If returnDate is null, set it as NULL in the database
            if (borrow.getReturnDate() != null) {
                stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.NULL);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Borrow borrow) {
        addBorrow(borrow);
    }
}
