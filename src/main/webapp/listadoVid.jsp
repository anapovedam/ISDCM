<%-- 
    Document   : listadoVid
    Created on : 23 feb 2025, 22:08:17
    Author     : alumne
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Video" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Video> listaVideos = (List<Video>) request.getAttribute("listaVideos");
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Listado de Vídeos</title>
     <link rel="stylesheet" href="css/listadoVid.css">
</head>
<body>
        <!-- Incluir el header -->
        <jsp:include page="header.jsp" />
        <div class="container">
        <h2>Listado de Vídeos</h2>

        <p>Bienvenido, <%= sessionUser.getAttribute("username") %> | 
        <a href="servletUsuarios?action=logout">Cerrar sesión</a></p>

        <!-- Botón para actualizar la lista de vídeos -->
        <form action="servletListadoVid" method="get">
            
            <button type="submit">Actualizar Lista</button>
        </form>

        <table border="1">
            <thead>
                <tr>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>Fecha</th>
                    <th>Duración</th>
                    <th>Reproducciones</th>
                    <th>Descripción</th>
                    <th>Formato</th>
                </tr>
            </thead>
            <tbody>
                <% if (listaVideos != null && !listaVideos.isEmpty()) { %>
                    <% for (Video vid : listaVideos) { %>
                        <tr>                          
                            <td><%= vid.getTitle() %></td>
                            <td><%= vid.getAuthor() %></td>
                            <td><%= vid.getCreationDate() %></td>
                            <td><%= vid.getDuration() %> min</td>
                            <td><%= vid.getViews() %></td>
                            <td><%= vid.getDescription() %></td>
                            <td><%= vid.getFormat() %></td>
                            <td><a href="<%= vid.getUrl() %>" target="_blank">Ver vídeo</a></td>
                        </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="7">No hay vídeos disponibles.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <br>
        <a href="registroVid.jsp">Subir un nuevo vídeo</a>
    </div>
            
</body>
</html>


