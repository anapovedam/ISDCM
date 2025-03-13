<%-- 
    Document   : registroVid
    Created on : 23 feb 2025, 22:08:07
    Author     : alumne
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Registrar Nuevo Vídeo</title>
    <link rel="stylesheet" href="css/registroVid.css">
</head>
<body>
    <h2>Registrar un Nuevo Vídeo</h2>
    
    <% if (request.getAttribute("mensaje") != null) { %>
        <p style="color: red;"><%= request.getAttribute("mensaje") %></p>
    <% } %>

    <form action="servletRegistroVid" method="post">
        <label for="titulo">Título:</label>
        <input type="text" id="titulo" name="titulo" placeholder="Ingrese el título del vídeo" required><br>

        <input type="hidden" id="autor" name="autor" value="<%= sessionUser.getAttribute("username") %>">

        <input type="hidden" id="fechaCreacion" name="fechaCreacion" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>">

        <label for="duracion">Duración (HH:mm):</label>
        <input type="text" id="duracion" name="duracion" placeholder="Ej: 01:30" pattern="^([0-9]{2}):([0-5][0-9])$"required><br>

        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" name="descripcion" required></textarea><br>

        <label for="formato">Formato:</label>
        <select id="formato" name="formato" required>
            <option value="" disabled selected>Seleccione un formato</option>
            <option value="MP4">MP4</option>
            <option value="AVI">AVI</option>
            <option value="MKV">MKV</option>
        </select><br>

        <label for="url">URL del Vídeo:</label>
        <input type="text" id="url" name="url" placeholder="Ingrese la URL del vídeo" required><br>

        <button type="submit">Registrar Vídeo</button>
    </form>

    <br>
    <a href="listadoVid.jsp">Volver al Listado de Vídeos</a>
</body>
</html>

