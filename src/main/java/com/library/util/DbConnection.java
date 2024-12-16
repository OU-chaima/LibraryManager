package com.library.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://mysql-18ebf45b-ouazzanchaimae-1a43.h.aivencloud.com:27468/library_db?useSSL=true&requireSSL=true";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_Y5bc6dNh-WLL_uPRBFs";

    public static Connection getConnection() throws SQLException {
        try {
            // Charger le driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir la connexion
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC introuvable", e);
        } catch (SQLException e) {
            throw new SQLException("Erreur de connexion à la base de données", e);
        }
    }
}
