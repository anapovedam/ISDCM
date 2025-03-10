import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Video;

@WebServlet(name = "servletListadoVid", urlPatterns = {"/servletListadoVid"})
public class servletListadoVid extends HttpServlet {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/pr2";
    private static final String JDBC_USER = "pr2";
    private static final String JDBC_PASSWORD = "pr2";
    private static final String TABLENAME = "videos";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Video> listaVideos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT * FROM " + TABLENAME;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titulo = rs.getString("title");
                    int authorId = rs.getInt("author_id");
                    String autor = rs.getString("author");
                    Date fechaCreacion = rs.getDate("creation_date"); 
                    Time duracion = rs.getTime("duration"); 
                    int numReproducciones = rs.getInt("views");
                    String descripcion = rs.getString("description");
                    String formato = rs.getString("format");
                    String url = rs.getString("url");

                    Video vid = new Video(id, titulo, authorId, autor, fechaCreacion, duracion, numReproducciones, descripcion, formato, url);
                    listaVideos.add(vid);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener vídeos: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("listaVideos", listaVideos);
        request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
    }
}
