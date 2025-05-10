package controllers;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import DAO.VideoDAO;
import model.Video;

@WebServlet(name = "servletListadoVid", urlPatterns = {"/servletListadoVid"})
public class servletListadoVid extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener los parámetros del formulario de filtros
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String fecha = request.getParameter("fecha");
        String minVistasStr = request.getParameter("minVistas");
        String maxVistasStr = request.getParameter("maxVistas");

        // Convertir los parámetros de vistas a números si es posible
        int minVistas = minVistasStr != null && !minVistasStr.isEmpty() ? Integer.parseInt(minVistasStr) : 0;
        int maxVistas = maxVistasStr != null && !maxVistasStr.isEmpty() ? Integer.parseInt(maxVistasStr) : Integer.MAX_VALUE;

        // Crear un objeto VideoDAO para manejar la base de datos
        VideoDAO videoDAO = new VideoDAO();

        // Filtrar los vídeos según los parámetros recibidos
        List<Video> listaVideos = videoDAO.getFilteredVideos(titulo, autor, fecha, minVistas, maxVistas);

        // Enviar la lista de vídeos filtrados a la vista
        request.setAttribute("listaVideos", listaVideos);

        // Redirigir a la vista correspondiente
        request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
    }
}
