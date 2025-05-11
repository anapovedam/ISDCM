package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Video;
import util.servletREST; // Importa servletREST

@WebServlet(name = "servletReproductorVid", urlPatterns = {"/servletReproductorVid"})
public class servletReproductorVid extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String videoIdParam = request.getParameter("videoId");
        if (videoIdParam == null) {
            response.sendRedirect("listadoVid.jsp");
            return;
        }

        int videoId;
        try {
            videoId = Integer.parseInt(videoIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("listadoVid.jsp");
            return;
        }

        servletREST rest = new servletREST(); // Crea instancia de servletREST

        try {
            rest.incrementViews(videoId);

            Video video = rest.getVideoById(videoId);

            if (video != null) {
                request.setAttribute("video", video);
                request.getRequestDispatcher("reproductorVid.jsp").forward(request, response);
            } else {
                response.sendRedirect("listadoVid.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al obtener el video: " + e.getMessage());
            request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
        }
    }
}
