<%-- 
    Document   : registroUsu
    Created on : 23 feb 2025, 22:07:52
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro de Usuario</title>
        <link rel="stylesheet" href="css/registroUsu.css">
    </head>
    <body>
        <div class="container">
            <h2>Registro de Usuario</h2>

        <%-- Mostrar mensaje de error o éxito si existe --%>
        <% if (request.getAttribute("mensaje") != null) { %>
            <p class="<%= request.getAttribute("mensaje").toString().contains("Error") ? "error" : "success" %>">
                <%= request.getAttribute("mensaje") %>
            </p>
        <% } %>

            <form action="servletUsuarios" method="post">
                <input type="hidden" name="action" value="register">
                <input type="text" name="name" placeholder="Nombre" required>
                <input type="text" name="surname" placeholder="Apellidos" required>
                <input type="email" name="email" placeholder="Correo electrónico" required>
                <input type="text" name="username" placeholder="Nombre usuario" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <input type="password" name="confirmPassword" placeholder="Repetir Contraseña" required>
                <button type="submit">Registrar Usuario</button>
            </form>

            <p>¿Ya tienes una cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>
        </div>
    </body>
</html>
