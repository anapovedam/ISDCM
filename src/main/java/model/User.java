/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Ana Poveda
 */
public class User {
    private int id;
    private String user_name;
    private String surname;
    private String email;
    private String username;
    private String password;
    
    private static final String DB_HOST = "jdbc:derby://localhost:1527/pr2";
    private static final String DB_USER = "pr2";
    private static final String DB_PASSWORD = "pr2";
    private static final String TABLENAME = "users";
    
    public User(){
        this.id = -1;
        this.user_name = null;
        this.surname = null;
        this.email = null;
        this.username = null;
        this.password = null;
    }    
    
    public User(int ID, String nombre, String apellidos, String email, String userName, String password){
        System.out.println("Cargando Usuario: " + ID + " - " + nombre + " - " + apellidos + " - " + email + " - " + userName + " - " + password);
        this.id = ID;
        this.user_name = nombre;
        this.surname = apellidos;
        this.email = email;
        this.username = userName;
        this.password = password;
    }
    
    public User(String nombre, String apellidos, String email, String userName, String password){
        System.out.println("Cargando Usuario: " + nombre + " - " + apellidos + " - " + email + " - " + userName + " - " + password);
        this.user_name = nombre;
        this.surname = apellidos;
        this.email = email;
        this.username = userName;
        this.password = password;
    }
       
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean existsUserOrEmail(){
        boolean existsUserOrEmail = true;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "SELECT COUNT(*) as COUNT FROM " + TABLENAME + " WHERE username='" + this.username + "' OR email='" + this.email + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                existsUserOrEmail = (rs.getInt("COUNT") > 0);
            }
            
            return existsUserOrEmail;            
        } catch (SQLException err) {
            System.out.println(err.getMessage());       
        }
        return existsUserOrEmail;
    }
    
    public boolean createUser(){
        boolean result = false;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "INSERT INTO " + TABLENAME
                    + "(user_name, surname, email, username, password)"
                   + " VALUES ('" + this.user_name + "', '" + this.surname + "', '" + this.email + "', '" + this.username + "', '" + this.password + "')";
            System.out.println("Sentencia SQL: " + sql);
            stmt.executeUpdate(sql);
            
            result = true;
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        return result;
    }
    
    public User getUser(){
        User user = null;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "SELECT * FROM " + TABLENAME + " WHERE username='" + this.username + "' AND password='" + this.password + "'";
            System.out.println("Sentencia SQL: " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int ID = rs.getInt("id");
                String user_name = rs.getString("user_name");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                String username = rs.getString("username");
                String password = rs.getString("password");
                                
                user = new User(ID, user_name, surname, email, username, password);
            }            
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        return user;
    }
}
