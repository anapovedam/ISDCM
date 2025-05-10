package isdcm.webappreproduccionvideos.rest;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import isdcm.webappreproduccionvideos.dao.VideoDAO;
import isdcm.webappreproduccionvideos.model.Video;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "servletListadoVid", urlPatterns = {"/servletListadoVid"})
public class servletReproduccionVid extends HttpServlet {
    
    private static final String DB_HOST = "jdbc:derby://localhost:1527/pr2";
    private static final String DB_USER = "pr2";
    private static final String DB_PASSWORD = "pr2";
    private static final String TABLENAME = "videos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        VideoDAO videoDAO = new VideoDAO();
        List<Video> listaVideos = videoDAO.getAllVideos();

        request.setAttribute("listaVideos", listaVideos);
        request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
    }
    
    public static int updateVideoReproducciones(int idVideo){
        int reproducciones = 0;
        try {
            Connection conn = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            
            String sql = "SELECT VIEWS FROM " + TABLENAME + " WHERE ID=" + idVideo;
            System.out.println("Sentencia SQL: " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {                
                reproducciones = rs.getInt("VISUALIZATIONS") + 1;
            }
            
            String query = "UPDATE " + TABLENAME + " SET VISUALIZATIONS= ? WHERE ID= ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, reproducciones);
            preparedStmt.setInt(2, idVideo);
            
            preparedStmt.executeUpdate();
            conn.close();
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        }
        
        return reproducciones;
    }
}