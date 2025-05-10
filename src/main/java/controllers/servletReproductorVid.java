package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Video;
import DAO.VideoDAO;

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

        // Obtener el ID del vídeo desde los parámetros
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

        VideoDAO dao = new VideoDAO();

        // ✅ Incrementar visualizaciones
        dao.incrementViews(videoId);

        // 🔄 Obtener el vídeo actualizado
        Video video = dao.getVideo(videoId);

        if (video != null) {
            request.setAttribute("video", video);
            request.getRequestDispatcher("reproductorVid.jsp").forward(request, response);
        } else {
            response.sendRedirect("listadoVid.jsp");
        }
    }
}
