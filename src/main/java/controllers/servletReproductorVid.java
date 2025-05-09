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

        int videoId = Integer.parseInt(request.getParameter("videoId"));
        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(videoId);

        if (video != null) {
            request.setAttribute("video", video);
            request.getRequestDispatcher("reproductorVid.jsp").forward(request, response);
        } else {
            response.sendRedirect("listadoVid.jsp");
        }
    }
}
