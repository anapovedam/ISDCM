/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author alumne
 */
@WebServlet(urlPatterns = {"/servletUsuarios"})
public class servletUsuarios extends HttpServlet {
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/pr2";
    private static final String JDBC_USER = "pr2";
    private static final String JDBC_PASSWORD = "pr2";
    private static final String TABLENAME = "users";
    
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet servletUsuarios</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet servletUsuarios at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validación básica
        if (nombre == null || apellidos == null || email == null || username == null || password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            request.setAttribute("mensaje", "Error: Todos los campos son obligatorios y las contraseñas deben coincidir.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Verificar si el usuario ya existe
            String checkUserSQL = "SELECT * FROM " + TABLENAME + " WHERE USERNAME = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSQL)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    request.setAttribute("mensaje", "Error: El usuario ya existe.");
                    request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
                    return;
                }
            }
            // Insertar el nuevo usuario
            String insertSQL = "INSERT INTO " + TABLENAME + " (user_name, surname, email, username, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                stmt.setString(1, nombre);
                stmt.setString(2, apellidos);
                stmt.setString(3, email);
                stmt.setString(4, username);
                stmt.setString(5, password);
                stmt.executeUpdate();
                conn.close();
            } catch (SQLException err) {
                System.out.println(err.getMessage());
            }

            request.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            //e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
        }
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
            String nombre = request.getParameter("username");
            String contraseña = request.getParameter("password");
            if (nombre == null || contraseña == null) {
                request.setAttribute("mensaje", "Error: Todos los campos son obligatorios.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Verificar si el usuario ya existe
                String checkUserSQL = "SELECT * FROM USUARIOS WHERE USERNAME = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSQL)) {
                    checkStmt.setString(1, nombre);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        //Logeado correctamente
                        //request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
                        return;
                    }
                }
            } catch (Exception e) {
            //e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
        }
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
