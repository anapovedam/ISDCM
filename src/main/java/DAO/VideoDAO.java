/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import util.DatabaseConnection;
import model.Video;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDAO {

    public boolean createVideo(Video video) {
        String sql = "INSERT INTO videos (title, author, author_id, creation_date, duration, views, description, format, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getAuthor());
            stmt.setInt(3, video.getAuthorID());
            stmt.setDate(4, video.getCreationDate());
            stmt.setTime(5, video.getDuration());
            stmt.setInt(6, video.getViews());
            stmt.setString(7, video.getDescription());
            stmt.setString(8, video.getFormat());
            stmt.setString(9, video.getUrl());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en createVideo: " + e.getMessage());
        }
        return false;
    }

    public Video getVideo(int id) {
        String sql = "SELECT * FROM videos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Video(
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
        } catch (SQLException e) {
            System.out.println("Error en getVideo: " + e.getMessage());
        }
        return null;
    }

    public List<Video> getAllVideos() {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT * FROM videos ORDER BY creation_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                videos.add(new Video(
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
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error en getAllVideos: " + e.getMessage());
        }
        return videos;
    }

    public boolean deleteVideo(int id) {
        String sql = "DELETE FROM videos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error en deleteVideo: " + e.getMessage());
        }
        return false;
    }
    
    public void incrementViews(int id) {
    // TODO: Llamar a la API que incrementa las visualizaciones
    // Por ejemplo:
    // POST https://api.ejemplo.com/videos/{id}/increment-view
    }
    
        public List<Video> getFilteredVideos(String titulo, String autor, String fecha, int minVistas, int maxVistas) {
        List<Video> allVideos = getAllVideos(); //fetchVideosFromApi();
        List<Video> filtered = new ArrayList<>();

        for (Video video : allVideos) {
            boolean matches = true;

            if (titulo != null && !titulo.isEmpty() && !video.getTitle().toLowerCase().contains(titulo.toLowerCase())) {
                matches = false;
            }
            if (autor != null && !autor.isEmpty() && !video.getAuthor().toLowerCase().contains(autor.toLowerCase())) {
                matches = false;
            }
            if (fecha != null && !fecha.isEmpty() && (video.getCreationDate() == null || !video.getCreationDate().toString().contains(fecha))) {
                matches = false;
            }
            if (video.getViews() < minVistas || video.getViews() > maxVistas) {
                matches = false;
            }

            if (matches) {
                filtered.add(video);
            }
        }

        return filtered;
    }
        

}

