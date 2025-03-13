/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import util.DatabaseConnection;
import model.User;
import java.sql.*;

public class UserDAO {

    public boolean existsUserOrEmail(String username, String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error en existsUserOrEmail: " + e.getMessage());
        }
        return false;
    }

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (user_name, surname, email, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUserName());
            stmt.setString(5, user.getPassword());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en createUser: " + e.getMessage());
        }
        return false;
    }

    public User getUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("user_name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error en getUser: " + e.getMessage());
        }
        return null;
    }
    
    public User getUserByUsername(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new User(
                rs.getInt("id"),
                rs.getString("user_name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("password")
            );
        }
    } catch (SQLException e) {
        System.out.println("Error en getUserByUsername: " + e.getMessage());
    }
    return null;
}
}

