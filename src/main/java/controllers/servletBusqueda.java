/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelDAo
/**
 *
 * @author alumne
 */
@WebServlet(name = "servletBusqueda", urlPatterns = {"/servletBusqueda"})
public class servletBusqueda extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
     *
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
