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
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Nuevo Vídeo</title>
    <link rel="stylesheet" href="css/registroVid.css">
</head>
<body>
    <div class="container">
        

        <div>
            <h2>Registrar un Nuevo Vídeo</h2>
            <div class="top-right">
            <a href="servletListadoVid" class="link">Volver al Listado</a>
        </div>
            
        </div>
        <% if (request.getAttribute("mensaje") != null) { %>
            <p class="mensaje"><%= request.getAttribute("mensaje") %></p>
        <% } %>

        <form action="servletRegistroVid" method="post">
            <div class="form-group">
                <label for="titulo">Título:</label>
                <input type="text" id="titulo" name="titulo" placeholder="Ingrese el título del vídeo" required>
            </div>

            <input type="hidden" id="autor" name="autor" value="<%= sessionUser.getAttribute("username") %>">
            <input type="hidden" id="fechaCreacion" name="fechaCreacion" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>">

            <div class="form-group">
                <label for="duracion">Duración (HH:mm):</label>
                <input type="text" id="duracion" name="duracion" placeholder="Ej: 01:30" pattern="^([0-9]{2}):([0-5][0-9])$" required>
            </div>

            <div class="form-group">
                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" placeholder="Ingrese una breve descripción" required></textarea>
            </div>

            <div class="form-group">
                <label for="formato">Formato:</label>
                <select id="formato" name="formato" required>
                    <option value="" disabled selected>Seleccione un formato</option>
                    <option value="MP4">MP4</option>
                    <option value="AVI">AVI</option>
                    <option value="MKV">MKV</option>
                </select>
            </div>

            <div class="form-group">
                <label for="url">URL del Vídeo:</label>
                <input type="url" id="url" name="url" placeholder="Ingrese la URL del vídeo" required>
            </div>

            <button type="submit" class="btn">Registrar Vídeo</button>
        </form>
    </div>
</body>
</html>
