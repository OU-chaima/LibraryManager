package com.library;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;
    private final BorrowDAO borrowDAO=new BorrowDAO();

    @BeforeEach
    void setUp() {
        // Initialisation du DAO et du service
        bookDAO = new BookDAO();
        bookService = new BookService(bookDAO);
        borrowDAO.deleteAllBorrows();
        // Nettoyage de la base de données pour éviter les doublons
        cleanDatabase();
    }

    private void cleanDatabase() {
        // Supprimer tous les livres de la base de données avant chaque test
        bookDAO.deleteAllBooks();  // Implémentez cette méthode pour supprimer tous les livres.
    }

    @Test
    void testAddBook() {
        // Créer un livre avec un ID dynamique ou généré pour éviter les conflits
        int bookId = (int) (Math.random() * 1000);  // Génération d'un ID unique
        Book book = new Book(bookId, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);

        // Ajouter le livre
        bookService.addBook(book);

        // Vérifier que le livre est bien ajouté
        assertEquals(1, bookDAO.getAllBooks().size(), "Le livre n'a pas été ajouté correctement.");
        assertEquals("Java Programming", bookDAO.getAllBooks().get(0).getTitle(), "Le titre du livre ajouté est incorrect.");
    }

    @Test
    void testUpdateBook() {
        // Créer un livre à ajouter
        int bookId = (int) (Math.random() * 1000);  // Génération d'un ID unique
        Book book = new Book(bookId, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);
        bookService.addBook(book);

        // Mettre à jour le livre
        book.setTitle("Advanced Java");
        book.setAuthor("Jane Doe");
        bookService.updateBook(book);

        // Vérifier les modifications
        Book updatedBook = bookDAO.getAllBooks().get(0);
        assertEquals("Advanced Java", updatedBook.getTitle(), "Le titre du livre mis à jour est incorrect.");
        assertEquals("Jane Doe", updatedBook.getAuthor(), "L'auteur du livre mis à jour est incorrect.");
    }

    @Test
    void testDeleteBook() {
        // Créer un livre à supprimer
        int bookId = (int) (Math.random() * 1000);  // Génération d'un ID unique
        Book book = new Book(bookId, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);
        bookService.addBook(book);

        // Supprimer le livre
        bookService.deleteBook(book.getId());

        // Vérifier que la liste est vide après la suppression
        assertTrue(bookDAO.getAllBooks().isEmpty(), "Le livre n'a pas été supprimé correctement.");
    }
}
