
package com.library.dao;

import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.service.BorrowService;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import static com.mysql.cj.conf.PropertyKey.logger;

public class BorrowDAO {
    private static final Logger logger = Logger.getLogger(BorrowService.class.getName());
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
        if (!borrow.getBook().isAvailable()) {
            throw new IllegalArgumentException("Le livre n'est pas disponible.");
        }

        String query = "INSERT INTO Borrows (student_id, book_id, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, borrow.getStudent().getId());
            stmt.setInt(2, borrow.getBook().getId());
            stmt.setDate(3, new java.sql.Date(borrow.getBorrowDate().getTime()));

            // Auto-set returnDate if null
            if (borrow.getReturnDate() == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(borrow.getBorrowDate());
                calendar.add(Calendar.DAY_OF_YEAR, 14);
                stmt.setDate(4, new java.sql.Date(calendar.getTime().getTime()));
            } else {
                stmt.setDate(4, new java.sql.Date(borrow.getReturnDate().getTime()));
            }

            stmt.executeUpdate();

            // Marquer le livre comme non disponible
            borrow.getBook().setAvailable(false);
            new BookDAO().update(borrow.getBook());
        } catch (SQLException e) {
            logger.severe("Erreur lors de l'ajout de l'emprunt");
        }
    }


    public void save(Borrow borrow) {
        addBorrow(borrow);
    }
}
