package com.library;

import com.library.dao.BookDAO;
import com.library.service.BorrowService;
import com.library.service.BookService;
import com.library.service.StudentService;
import com.library.model.Book;
import com.library.model.Student;
import com.library.model.Borrow;
import com.library.dao.BorrowDAO;

import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookDAO bookDAO = new BookDAO();
        BookService bookService = new BookService(bookDAO);
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
            int choice;

            // Gestion des entrées incorrectes
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.nextLine(); // Réinitialiser l'entrée
                continue;
            }
            scanner.nextLine(); // Consommer la ligne restante après l'entier

            switch (choice) {
                case 1: // Ajouter un livre
                    System.out.print("Entrez le titre du livre: ");
                    String title = scanner.nextLine();
                    System.out.print("Entrez l'auteur du livre: ");
                    String author = scanner.nextLine();
                    System.out.print("Entrez l'éditeur du livre: ");
                    String publisher = scanner.nextLine();
                    System.out.print("Entrez l'ISBN du livre: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Entrez l'année de publication du livre: ");
                    int year;

                    try {
                        year = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("Année invalide. Retour au menu principal.");
                        scanner.nextLine(); // Réinitialiser l'entrée
                        break;
                    }

                    Book book = new Book(0, title, author, publisher, isbn, year, true);
                    bookService.addBook(book);
                    System.out.println("Livre ajouté avec succès !");
                    break;

                case 2: // Afficher les livres
                    bookService.displayBooks();
                    break;

                case 3: // Ajouter un étudiant
                    System.out.print("Entrez le nom de l'étudiant: ");
                    String studentName = scanner.nextLine();
                    Student student = new Student(0, studentName);
                    studentService.addStudent(student);
                    System.out.println("Étudiant ajouté avec succès !");
                    break;

                case 4: // Afficher les étudiants
                    studentService.displayStudents();
                    break;

                case 5: // Emprunter un livre
                    System.out.print("Entrez l'ID de l'étudiant: ");
                    int studentId;
                    int bookId;

                    try {
                        studentId = scanner.nextInt();
                        System.out.print("Entrez l'ID du livre: ");
                        bookId = scanner.nextInt();
                    } catch (Exception e) {
                        System.out.println("ID invalide. Retour au menu principal.");
                        scanner.nextLine(); // Réinitialiser l'entrée
                        break;
                    }
                    Optional<Student> studentForBorrow = studentService.findStudentById(studentId);
                    Optional<Book> bookForBorrow = bookService.findBookById(bookId);

                    if (studentForBorrow.isPresent() && bookForBorrow.isPresent()) {
                        Book bookToBorrow = bookForBorrow.get();
                        if (bookToBorrow.isAvailable()) {
                            Borrow borrow = new Borrow(0, studentForBorrow.get(), bookToBorrow, new Date(), null);
                            borrowService.borrowBook(borrow);
                            System.out.println("Livre emprunté avec succès !");
                        } else {
                            System.out.println("Le livre n'est pas disponible pour emprunt.");
                        }
                    } else {
                        if (studentForBorrow.isEmpty()) {
                            System.out.println("Étudiant introuvable.");
                        }
                        if (bookForBorrow.isEmpty()) {
                            System.out.println("Livre introuvable.");
                        }
                    }
                    break;

                case 6: // Afficher les emprunts
                    borrowService.displayBorrows();
                    break;

                case 7: // Quitter
                    running = false;
                    System.out.println("Au revoir !");
                    break;

                default: // Option invalide
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }

        scanner.close();
    }
}
