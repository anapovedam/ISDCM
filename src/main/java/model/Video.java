/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.PreparedStatement;


/**
 *
 * @author Ana Poveda
 */
public class Video {
    private int id;
    private String title;
    private int author_id;
    private String author;
    private Date creation_date;
    private Time duration;
    private int views;
    private String description;
    private String format;
    private String url;
    
    private static final String DB_HOST = "jdbc:derby://localhost:1527/pr2";
    private static final String DB_USER = "pr2";
    private static final String DB_PASSWORD = "pr2";
    private static final String TABLENAME = "videos";
    
    public Video(){
        this.id = -1;
        this.title = null;
        this.author_id = -1;
        this.author = null;
        this.creation_date = null;
        this.duration = null;
        this.views = 0;
        this.description = null;
        this.format = null;
        this.url = null;
    } 
    
    public Video(int id, String title, int author_id, String author, Date creation_date, Time duration, int views, String description, String format, String url){
        System.out.println("Creando Video: " + id + " - " + title + " - " + author_id + " - " + author + " - " + creation_date + " - " + duration + " - " + views + " - " + description + " - " + format + " - " + url);
        this.id = id;
        this.title = title;
        this.author_id = author_id;
        this.author = author;
        this.creation_date = creation_date;
        this.duration = duration;
        this.views = views;
        this.description = description;
        this.format = format;
        this.url = url;
    }
    
    public Video(String title, int author_id, String author, Date creation_date, Time duration, int views, String description, String format, String url){
        System.out.println("Cargando Video: UNREGISTERED - " + " - " + title + " - " + author_id + " - " + author + " - " + creation_date + " - " + duration + " - " + views + " - " + description + " - " + format + " - " + url);
        this.id = -1;
        this.title = title;
        this.author_id = author_id;
        this.author = author;
        this.creation_date = creation_date;
        this.duration = duration;
        this.views = views;
        this.description = description;
        this.format = format;
        this.url = url;
    } 
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorID() {
        return author_id;
    }

    public void setAuthorID(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creation_date;
    }

    public void setCreationDate(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescripcton(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public Video getVideo() {
    Video video = null;
    try (Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + TABLENAME + " WHERE id = ?")) {
        stmt.setInt(1, this.id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {                
            video = new Video(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("author_id"),
                rs.getString("author"),
                rs.getDate("creation_date"),
                rs.getTime("duration"),
                rs.getInt("views"),
                rs.getString("description"),
                rs.getString("format"),
                rs.getString("url")
            );
        }
    } catch (SQLException err) {
        System.out.println("Error en getVideo(): " + err.getMessage());
    }
    return video;
}

    
    public boolean createVideo(){
        boolean result = false;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "INSERT INTO " + TABLENAME 
                    + "(title, author, author_id, creation_date, duration, views, description, format, url)"
                    + " VALUES (" + this.getTitle() + ", '" + this.getAuthor() + "', '" + this.getAuthorID() + "', '" + this.getCreationDate() + "', '" + this.getDuration() + "', " + this.getViews() + ", '" + this.getDescription() + "', '" + this.getFormat() + "', '" + this.getUrl() + "')";
            System.out.println("Sentencia SQL: " + sql);
            stmt.executeUpdate(sql);
            
            result = true;
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        return result;
    }
    
    public Video deleteVideo(){
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "DELETE FROM " + TABLENAME + " WHERE ID=" + this.getId();
            System.out.println("Sentencia SQL: " + sql);
            stmt.executeUpdate(sql);
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        
        return new Video();
    }
}
