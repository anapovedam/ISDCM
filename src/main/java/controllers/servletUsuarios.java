package controllers;

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
import DAO.UserDAO;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;  // you already have org.json on classpath
import org.json.JSONException;

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

        if (action == null) {
            System.err.println("Error: acción nula");
            request.setAttribute("mensaje", "Error: Acción no válida.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        switch (action) {
            case "register":
                registerUser(request, response);
                break;
            case "login":
                loginUser(request, response);
                break;
            default:
                System.err.println("Error: acción desconocida en doPost -> " + action);
                request.setAttribute("mensaje", "Error: Acción no reconocida.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
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

        // Validación de contraseña
        if (!isValidPassword(password)) {
            request.setAttribute("mensaje", "Error: La contraseña debe tener mínimo 8 caracteres, incluir letras, números y símbolos.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
            return;
        }
        
        // Validación de los campos del formulario de registro
        if (name == null || surname == null || email == null || username == null || password == null || confirmPassword == null) {
            request.setAttribute("mensaje", "Error: Todos los campos son obligatorios.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
            return;
        }
        
        if(!password.equals(confirmPassword)) {
            request.setAttribute("mensaje", "Error: Las contraseñas deben coincidir.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
            return;
        }
        
        
        UserDAO userDAO = new UserDAO();
        if (userDAO.existsUserOrEmail(username, email)) {
            request.setAttribute("mensaje", "Error: El usuario o el correo electrónico ya están registrados.");
            request.getRequestDispatcher("registroUsu.jsp").forward(request, response);
            return;
        }
        
        
        User user = new User(name, surname, email, username, password);
        if (userDAO.createUser(user)) {
            request.setAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("mensaje", "Error al registrar el usuario. Inténtalo más tarde.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
            
        String formData = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                        + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/ISDCM-21_REST_Service_v2/res/videos/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();
         try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                // Backend returned { "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..." }
                JSONObject jsonResponse = new JSONObject(httpResponse.body());
                String jwt = jsonResponse.getString("token");

                // Store JWT (and maybe username) in session
                HttpSession session = request.getSession(true);
                session.setAttribute("jwtToken", jwt);
                session.setAttribute("username", username);

                // Redirect to the page that lists videos (or your home)
                response.sendRedirect("servletListadoVid");  
            } else {
                // 401 or other → credentials invalid
                HttpSession session = request.getSession(true);
                session.setAttribute("error", "Error: Credenciales incorrectas.");
                response.sendRedirect("login.jsp");
            }
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error durante la autenticación: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            System.err.println("Error: acción nula");
            request.setAttribute("mensaje", "Error: Acción no válida.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        switch (action) {
            case "login":
                loginUser(request, response);
                break;
            case "logout":
                logoutUsuario(request, response);
                break;
            default:
                System.err.println("Error: acción desconocida-> " + action);
                request.setAttribute("mensaje", "Error: Acción no reconocida.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    private void logoutUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
    
    private boolean isValidPassword(String password) {
        // Expresión regular: Mínimo 8 caracteres, al menos una letra, un número y un símbolo
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }
}
