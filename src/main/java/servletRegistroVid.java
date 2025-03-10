import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "servletRegistroVid", urlPatterns = {"/servletRegistroVid"})
public class servletRegistroVid extends HttpServlet {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/pr2";
    private static final String JDBC_USER = "pr2";
    private static final String JDBC_PASSWORD = "pr2";
    private static final String TABLENAME = "videos";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String fechaCreacion = request.getParameter("fechaCreacion");
        String duracion = request.getParameter("duracion");  // En formato HH:mm
        String descripcion = request.getParameter("descripcion");
        String formato = request.getParameter("formato");
        String url = request.getParameter("url");  // Nuevo campo

        // Validar campos obligatorios
        if (titulo == null || autor == null || fechaCreacion == null || duracion == null || descripcion == null || formato == null || url == null) {
            request.setAttribute("mensaje", "Error: Todos los campos son obligatorios.");
            request.getRequestDispatcher("registroVid.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertSQL = "INSERT INTO " + TABLENAME + " (title, author, creation_date, duration, views, description, format, url) VALUES (?, ?, ?, ?, 0, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setDate(3, Date.valueOf(fechaCreacion)); // Convertir a tipo DATE
                stmt.setTime(4, Time.valueOf(duracion + ":00")); // Convertir a tipo TIME
                stmt.setString(5, descripcion);
                stmt.setString(6, formato);
                stmt.setString(7, url);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("registroVid.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("servletListadoVid"); // Redirige correctamente al listado
    }
}
