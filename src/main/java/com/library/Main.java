package com.library;

import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.dao.BorrowDAO; // Importer BorrowDAO

import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Création des services
        BookService bookService = new BookService();
        StudentService studentService = new StudentService();
        BorrowDAO borrowDAO = new BorrowDAO();
        BorrowService borrowService = new BorrowService(borrowDAO);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Menu =====");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Afficher les livres");
            System.out.println("3. Ajouter un étudiant");
            System.out.println("4. Afficher les étudiants");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Afficher les emprunts");
            System.out.println("7. Quitter");

            System.out.print("Choisir une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la ligne restante après l'entier

            switch (choice) {
                case 1:
                    System.out.print("Entrez le titre du livre: ");
                    String title = scanner.nextLine();
                    System.out.print("Entrez l'auteur du livre: ");
                    String author = scanner.nextLine();
                    System.out.print("Entrez l'éditeur du livre: ");
                    String publisher = scanner.nextLine();
                    System.out.print("Entrez l'ISBN du livre: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Entrez l'année de publication du livre: ");
                    int year = scanner.nextInt();

                    Book book = new Book(0, title, author, publisher, isbn, year, true);
                    bookService.addBook(book);
                    System.out.println("Livre ajouté avec succès!");
                    break;

                case 2:
                    bookService.displayBooks();
                    break;

                case 3:
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String studentName = scanner.nextLine();
                    Student student = new Student(0, studentName);
                    studentService.addStudent(student);
                    System.out.println("Étudiant ajouté avec succès!");
                    break;

                case 4:
                    studentService.displayStudents();
                    break;

                case 5:
                    System.out.print("Entrez l'ID de l'étudiant: ");
                    int studentId = scanner.nextInt();
                    System.out.print("Entrez l'ID du livre: ");
                    int bookId = scanner.nextInt();

                    Student studentForBorrow = studentService.findStudentById(studentId);
                    Book bookForBorrow = bookService.findBookById(bookId);

                    if (studentForBorrow != null && bookForBorrow != null && bookForBorrow.isAvailable()) {
                        Borrow borrow = new Borrow(0, studentForBorrow, bookForBorrow, new Date(), null);
                        borrowService.borrowBook(borrow);
                        System.out.println("Livre emprunté avec succès!");
                    } else {
                        if (studentForBorrow == null) {
                            System.out.println("Étudiant introuvable.");
                        } else if (bookForBorrow == null) {
                            System.out.println("Livre introuvable.");
                        } else {
                            System.out.println("Livre non disponible pour emprunt.");
                        }
                    }
                    break;

                case 6:
                    borrowService.displayBorrows();
                    break;

                case 7:
                    running = false;
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
