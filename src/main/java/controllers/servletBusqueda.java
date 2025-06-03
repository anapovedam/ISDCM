package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.servletREST;
import model.Video;

@WebServlet(name = "servletBusqueda", urlPatterns = {"/servletBusqueda"})
public class servletBusqueda extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Add character encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String fecha = request.getParameter("fecha");

        // Add debug logging
        System.out.println("DEBUG - titulo: '" + titulo + "'");
        System.out.println("DEBUG - autor: '" + autor + "'");
        System.out.println("DEBUG - fecha: '" + fecha + "'");

        Map<String, String> filtros = new HashMap<>();
        if (titulo != null && !titulo.trim().isEmpty()) {
            filtros.put("title", titulo.trim());
        }
        if (autor != null && !autor.trim().isEmpty()) {
            filtros.put("author", autor.trim());
        }
        if (fecha != null && !fecha.trim().isEmpty()) {
            filtros.put("datecreated", fecha.trim());
        }

        // Debug the filters map
        System.out.println("DEBUG - filtros: " + filtros);

        servletREST rest = new servletREST();
        List<Video> listaVideos = null;

        try {
            listaVideos = rest.getVideoFiltered(filtros);

            // Set the filtered video list as an attribute in the request
            request.setAttribute("listaVideos", listaVideos);

            // Forward the request to the listadoVid.jsp to display the results
            request.getRequestDispatcher("listadoVid.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            request.setAttribute("error", "Error al obtener la lista de videos: " + e.getMessage());
            request.getRequestDispatcher("listadoVid.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
