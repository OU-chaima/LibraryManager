package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookService bookService;
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO();
        bookService = new BookService(bookDAO);  // Injecter le même BookDAO
    }

    @Test
    void testAddBook() {
        Book book = new Book(10, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);
        bookService.addBook(book);
        assertEquals(1, bookDAO.getAllBooks().size());
        assertEquals("Java Programming", bookDAO.getAllBooks().get(0).getTitle());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book(10, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);
        bookService.addBook(book);
        book.setTitle("Advanced Java");
        book.setAuthor("Jane Doe");
        bookService.updateBook(book);
        Book updatedBook = bookDAO.getAllBooks().get(0);
        assertEquals("Advanced Java", updatedBook.getTitle());
        assertEquals("Jane Doe", updatedBook.getAuthor());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book(10, "Java Programming", "John Doe", "Tech Publisher", "123456789", 2024, true);
        bookService.addBook(book);
        bookService.deleteBook(book.getId());
        assertTrue(bookDAO.getAllBooks().isEmpty());
    }
}
