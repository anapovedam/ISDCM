package controllers;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import DAO.VideoDAO;
import DAO.UserDAO;
import model.Video;
import model.User;

@WebServlet(name = "servletRegistroVid", urlPatterns = {"/servletRegistroVid"})
public class servletRegistroVid extends HttpServlet {

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
            request.getRequestDispatcher("registroVid.jsp").forward(request, response);
            return;
        }

        try {
            // Crear un VideoDAO
            VideoDAO videoDAO = new VideoDAO();

            // Obtener el ID del autor desde la base de datos
            String autorId = getAutorId(autor);  // Cambiado para usar el ID del autor
            if (autorId == null) {
                request.setAttribute("mensaje", "Error: Autor no encontrado.");
                request.getRequestDispatcher("registroVid.jsp").forward(request, response);
                return;
            }

            // Crear el objeto Video
            Video video = new Video(
                0,  // ID no definido (será autogenerado por la base de datos)
            titulo,
            Integer.parseInt(getAutorId(autor)),  // Convertir el autorId de String a Integer
            getAutorFullName(autor),
            Date.valueOf(fechaCreacion),  // Convertir la fecha al formato adecuado
            Time.valueOf(duracion + ":00"),  // Convertir la duración al formato adecuado
            0,  // Vistas iniciales en 0
            descripcion,
            formato,
            url
            );

            // Insertar el video en la base de datos usando el DAO
            boolean success = videoDAO.createVideo(video);
            if (success) {
                response.sendRedirect("servletListadoVid"); // Redirige al listado de videos
            } else {
                request.setAttribute("mensaje", "Error al registrar el video.");
                request.getRequestDispatcher("registroVid.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error en el servidor: " + e.getMessage());
            request.getRequestDispatcher("registroVid.jsp").forward(request, response);
        }

    }
    /**
     * Obtiene el ID del autor desde la tabla Usuarios dado su username.
     * @param username Nombre de usuario a buscar.
     * @return ID del usuario si existe, null en caso contrario.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private String getAutorId(String username) {
    // Crear una instancia de UserDAO
    UserDAO userDAO = new UserDAO();
    
    // Usar el método getUser para obtener el usuario por username
    User user = userDAO.getUserByUsername(username);
    
        if (user != null) {
            return String.valueOf(user.getId()); // Retornar el ID del usuario
        }

        return null; // Si el usuario no se encuentra, retornar null
    }
    
    private String getAutorFullName(String username) {
    UserDAO userDAO = new UserDAO();
    User user = userDAO.getUserByUsername(username);

    if (user != null) {
        return user.getName() + " " + user.getSurname(); // Nombre + Apellido
    }
    return null;
}

}   
