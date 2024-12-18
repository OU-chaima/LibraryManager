package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;

import java.util.List;

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

    public Book findBookById(int id) {
        return bookDAO.getBookById(id)
                .orElseGet(() -> {
                    System.out.println("Aucun livre trouvé avec l'ID " + id);
                    return null;
                });
    }

    public void deleteBook(int bookId) {
        bookDAO.delete(bookId);
    }

    public void updateBook(Book book) {
        bookDAO.update(book);
    }
}
