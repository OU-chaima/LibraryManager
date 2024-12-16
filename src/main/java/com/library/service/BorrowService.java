
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

    // Méthode pour emprunter un livre
    public void borrowBook(Borrow borrow) {
        // Sauvegarde de l'emprunt dans la base de données
        borrowDAO.addBorrow(borrow);
    }

    // Afficher les emprunts
    public void displayBorrows() {
        for (Borrow borrow : borrowDAO.getAllBorrows()) {
            System.out.println("Emprunt ID: " + borrow.getId() + ", Livre: " + borrow.getBook().getTitle() + ", Étudiant: " + borrow.getStudent().getName());
        }
    }
}
