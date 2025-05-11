package controllers;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.servletREST; // Importa servletREST
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

        servletREST rest = new servletREST(); 
        List<Video> listaVideos = null;

        try {
            // Obtiene la lista de todos los videos al inicio
            listaVideos = rest.getAllVideos();
            request.setAttribute("listaVideos", listaVideos);

            request.getRequestDispatcher("listadoVid.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al obtener la lista de videos: " + e.getMessage());
            request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
        }
    }
}
