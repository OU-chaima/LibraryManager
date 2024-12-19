package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;

import java.util.List;
import java.util.Optional;

public class BookService {
    private final BookDAO bookDAO;

    // Constructeur pour injecter BookDAO
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void addBook(Book book) {
        bookDAO.add(book);
    }

    public void displayBooks() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public Optional<Book> findBookById(int bookId) {
        return bookDAO.getBookById(bookId); // Retourne directement l'Optional fourni par BookDAO
    }
    public void deleteBook(int bookId) {
        bookDAO.delete(bookId);
    }

    public void updateBook(Book book) {
        bookDAO.update(book);
    }
}
