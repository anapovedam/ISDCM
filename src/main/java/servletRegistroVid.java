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
        
        System.out.println("titulo: " + titulo);
        System.out.println("autor: " + autor);
        System.out.println("fechaCreacion: " + fechaCreacion);
        System.out.println("duracion: " + duracion);
        System.out.println("descripcion: " + descripcion);
        System.out.println("formato: " + formato);
        System.out.println("url: " + url);
        

        // Validar campos obligatorios
        if (titulo == null || autor == null || fechaCreacion == null || duracion == null || descripcion == null || formato == null || url == null) {
            request.setAttribute("mensaje", "Error: Todos los campos son obligatorios.");
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
             // Obtener el ID del autor desde la tabla Usuarios
            String autorId = getAutorId(conn, autor);
            if (autorId == null) {
                request.setAttribute("mensaje", "Error: Autor no encontrado en la base de datos.");
                request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
                return;
            }
            
            String insertSQL = "INSERT INTO " + TABLENAME + " (title, author, author_id, creation_date, duration, views, description, format, url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, titulo);
                stmt.setString(2, autor);
                stmt.setString(3, autorId);
                stmt.setDate(4, Date.valueOf(fechaCreacion)); // Convertir a tipo DATE
                stmt.setTime(5, Time.valueOf(duracion + ":00")); // Convertir a tipo TIME
                stmt.setInt(6, 0);
                stmt.setString(7, descripcion);
                stmt.setString(8, formato);
                stmt.setString(9, url);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos: " + e.getMessage());
            request.getRequestDispatcher("/views/registroVid.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("servletListadoVid"); // Redirige correctamente al listado
    }
    
    /**
     * Obtiene el ID del autor desde la tabla Usuarios dado su username.
     * @param conn Conexión activa a la base de datos.
     * @param username Nombre de usuario a buscar.
     * @return ID del usuario si existe, null en caso contrario.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private String getAutorId(Connection conn, String username) throws SQLException {
        String query = "SELECT id FROM Users WHERE username = ?";
        System.out.println("getAutorId: " + username);
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                }
            }
        }
        return null; // Usuario no encontrado
    }
    
}
