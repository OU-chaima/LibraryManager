package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.Borrow;
import com.library.model.Student;
import com.library.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {

    private BorrowService borrowService;
    private BookDAO bookDAO;
    private StudentDAO studentDAO;
    private BorrowDAO borrowDAO;

    @BeforeEach
    void setUp() throws ParseException {
        bookDAO = new BookDAO();
        studentDAO = new StudentDAO();
        borrowDAO = new BorrowDAO();
        borrowService = new BorrowService(borrowDAO);

        // Suppression des données existantes
        borrowDAO.deleteAllBorrows();
        bookDAO.deleteAllBooks();
        studentDAO.deleteAllStudents();

        // Ajout des étudiants
        studentDAO.addStudent(new Student(1, "Alice"));
        studentDAO.addStudent(new Student(2, "Bob"));

        // Ajout des livres
        bookDAO.add(new Book(1, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true));
        bookDAO.add(new Book(2, "Advanced Java", "John Doe", "Tech Publisher", "987654321", 2024, true));

        // Ajout d'un emprunt initial
        Student student = studentDAO.getStudentById(1).orElseThrow(() -> new RuntimeException("Student not found"));
        Book book = bookDAO.getBookById(1).orElseThrow(() -> new RuntimeException("Book not found"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date borrowDate = sdf.parse("12-12-2024");
        Date returnDate = sdf.parse("15-12-2024");
        Borrow borrow = new Borrow(1, student, book, borrowDate, returnDate);
        borrowDAO.addBorrow(borrow);
    }

    @Test
    void testBorrowBookSuccess() {
        Student student = studentDAO.getStudentById(2).orElseThrow(() -> new RuntimeException("Student not found"));
        Book book = bookDAO.getBookById(2).orElseThrow(() -> new RuntimeException("Book not found"));

        Borrow borrow = new Borrow(2, student, book, new Date(), null);
        borrowService.borrowBook(borrow);

        assertFalse(book.isAvailable(), "Le livre doit être marqué comme non disponible après l'emprunt");
    }

    @Test
    void testBorrowBookNotAvailable() {
        Book book = bookDAO.getBookById(1).orElseThrow(() -> new RuntimeException("Book not found"));
        assertFalse(book.isAvailable(), "Le livre est déjà emprunté, donc il ne devrait pas être disponible");
    }

    @Test
    void testBorrowBookStudentNotFound() {
        Book book = bookDAO.getBookById(2).orElseThrow(() -> new RuntimeException("Book not found"));
        Student nonExistentStudent = new Student(99, "Nonexistent");
        Borrow borrow = new Borrow(3, nonExistentStudent, book, new Date(), null);

        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(borrow), "Une exception doit être levée si l'étudiant n'existe pas");
    }

    @Test
    void testReturnBook() {
        Borrow borrow = borrowDAO.getAllBorrows().get(0); // Récupérer le premier emprunt

        borrow.getBook().setAvailable(true); // Simuler le retour
        borrowDAO.updateBorrow(borrow);
        assertTrue(borrow.getBook().isAvailable(), "Le livre doit être marqué comme disponible après son retour");
    }
}
