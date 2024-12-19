package com.library.dao;

import com.library.model.Book;
import com.library.util.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO {

    public void add(Book book) {
        String sql = "INSERT INTO Books (id,title, author, publisher, isbn, publishedYear,available) VALUES (?,?, ?, ?, ?, ?,?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getIsbn());
            statement.setInt(6, book.getPublishedYear());
            statement.setBoolean(7, book.isAvailable());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
        }
    }

    public void delete(int bookId) {
        String sql = "DELETE FROM Books WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
        }
    }

    public Optional<Book> getBookById(int id) {
        String sql = "SELECT * FROM Books WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToBook(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
        }
        return Optional.empty();
    }

    public void update(Book book) {
        String sql = "UPDATE Books SET title = ?, author = ?, publisher = ?, isbn = ?, publishedYear = ?, available = ? WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setString(4, book.getIsbn());
            statement.setInt(5, book.getPublishedYear());
            statement.setBoolean(6, book.isAvailable());
            statement.setInt(7, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du livre : " + e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try (Connection connection = DbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                books.add(mapToBook(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
        }
        return books;
    }

    private Book mapToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublishedYear(resultSet.getInt("publishedYear"));
        book.setAvailable(resultSet.getBoolean("available"));
        return book;
    }

    public void deleteAllBooks() {
        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Books");  // Supprimer tous les livres
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
