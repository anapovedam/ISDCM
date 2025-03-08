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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import model.User;

/**
 *
 * @author Ana Poveda
 */
@WebServlet(name = "servletUsuarios",urlPatterns = {"/servletUsuarios"})
public class servletUsuarios extends HttpServlet {
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/pr2";
    private static final String JDBC_USER = "pr2";
    private static final String JDBC_PASSWORD = "pr2";
    private static final String TABLENAME = "users";
    
    public static String attributeUserRegisteredOK = "IS_USER_REGISTERED_OK";
    public static String attributeUserOrEmailExists = "ERROR_USER_REGISTERED_YET";
    
    private static final String Name = "name";
    private static final String Surname= "surname";
    private static final String Email= "email";
    private static final String Username = "username";
    private static final String Password = "password";
    private static final String ConfirmPassword = "confirmPassword";
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("Recibida petición POST en servletUsuarios");
        System.out.println("Acción recibida: " + action);
        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("registerUser");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validación de los campos del formulario de registro
        if (name == null || surname == null || email == null || username == null || password == null || confirmPassword == null || !password.equals(confirmPassword)) {
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
                stmt.setString(1, name);
                stmt.setString(2, surname);
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
        
    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            System.out.println("login");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username == null || password == null) {
                request.setAttribute("mensaje", "Error: Todos los campos son obligatorios.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            // Verificar si el usuario ya existe
                String checkUserSQL = "SELECT * FROM " + TABLENAME + " WHERE USERNAME = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(checkUserSQL)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        //Logeado correctamente
                        //request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        response.sendRedirect("login.jsp");
                        return;
                    } else {
                        request.setAttribute("mensaje", "Error: Credenciales incorrectas.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                }
            } catch (Exception e) {
            //e.printStackTrace();
            request.setAttribute("mensaje", "Error en la base de datos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            logoutUsuario(request, response);
        }
    }
    
    private void logoutUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}
