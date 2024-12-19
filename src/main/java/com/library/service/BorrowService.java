
package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.StudentDAO;
import com.library.model.Student;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;

public class BorrowService {

    private BorrowDAO borrowDAO;

    // Constructeur avec BorrowDAO
    public BorrowService(BorrowDAO borrowDAO) {
        this.borrowDAO = borrowDAO;
    }

    public void borrowBook(Borrow borrow) {
        // Vérification si l'étudiant existe
        if (borrow.getStudent() == null || borrow.getStudent().getId() <= 0 || getStudentById(borrow.getStudent().getId()) == null) {
            throw new RuntimeException("L'étudiant n'existe pas.");
        }

        // Sauvegarde de l'emprunt dans la base de données
        borrowDAO.addBorrow(borrow);
    }
    private Student getStudentById(int studentId) {
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.getStudentById(studentId).orElse(null);
    }


    // Afficher les emprunts
    public void displayBorrows() {
        for (Borrow borrow : borrowDAO.getAllBorrows()) {
            System.out.println("Emprunt ID: " + borrow.getId() + ", Livre: " + borrow.getBook().getTitle() + ", Étudiant: " + borrow.getStudent().getName());
        }
    }
}
