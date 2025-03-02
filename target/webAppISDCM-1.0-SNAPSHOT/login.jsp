<%-- 
    Document   : login
    Created on : 23 feb 2025, 22:07:40
    Author     : Ana Poveda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar Sesión</title>
    </head>
    <body>
        <div class="container">
            <h2>Iniciar Sesión</h2>

            <%-- Mostrar mensaje de error o éxito si existe --%>
            <% if (request.getAttribute("mensaje") != null) { %>
                <p class="<%= request.getAttribute("mensaje").toString().contains("Error") ? "error" : "success" %>">
                    <%= request.getAttribute("mensaje") %>
                </p>
            <% } %>

            <form action="servletUsuarios" method="get">
                <input type="text" name="username" placeholder="Nombre de Usuario" required>
                <input type="password" name="password" placeholder="Contraseña" required>
                <button type="submit">Iniciar Sesión</button>
            </form>

            <p>¿No tienes una cuenta? <a href="registroUsu.jsp">Regístrate aquí</a></p>
        </div>
    </body>
</html>
