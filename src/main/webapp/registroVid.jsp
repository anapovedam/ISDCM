<%-- 
    Document   : registroVid
    Created on : 23 feb 2025, 22:08:07
    Author     : alumne
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <input type="text" id="titulo" name="titulo" required><br>

        <label for="autor">Autor:</label>
        <input type="text" id="autor" name="autor" required><br>

        <label for="fechaCreacion">Fecha de Creación:</label>
        <input type="date" id="fechaCreacion" name="fechaCreacion" required><br>

        <label for="duracion">Duración (HH:mm):</label>
        <input type="text" id="duracion" name="duracion" placeholder="HH:mm" required><br>

        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" name="descripcion" required></textarea><br>

        <label for="formato">Formato:</label>
        <select id="formato" name="formato" required>
            <option value="MP4">MP4</option>
            <option value="AVI">AVI</option>
            <option value="MKV">MKV</option>
        </select><br>

        <label for="url">URL del Vídeo:</label>
        <input type="text" id="url" name="url" required><br>

        <button type="submit">Registrar Vídeo</button>
    </form>

    <br>
    <a href="listadoVid.jsp">Volver al Listado de Vídeos</a>
</body>
</html>

