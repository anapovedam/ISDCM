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

        <p>Bienvenido, <%= sessionUser.getAttribute("username") %>
        <h3 style="margin-bottom: 10px;">Filtrar vídeos</h3>
<form action="servletListadoVid" method="get" style="display: flex; flex-wrap: wrap; gap: 10px; align-items: center; justify-content: center; margin-bottom: 20px;">
    <input type="text" name="titulo" placeholder="Título" 
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; border: 1px solid #ccc;" />

    <input type="text" name="autor" placeholder="Autor" 
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; border: 1px solid #ccc;" />

    <input type="date" name="fecha" title="Fecha de creación" 
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; border: 1px solid #ccc;" />

    <input type="number" name="minVistas" placeholder="Min. vistas" min="0" 
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; border: 1px solid #ccc;" />

    <input type="number" name="maxVistas" placeholder="Max. vistas" min="0" 
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; border: 1px solid #ccc;" />

    <input type="submit" value="Filtrar"
           style="padding: 6px 10px; font-size: 14px; border-radius: 6px; background-color: #2b78e4; color: white; border: none; cursor: pointer;" />

    <a href="servletListadoVid" 
       style="text-decoration: none; background-color: #ccc; padding: 6px 10px; color: black; border-radius: 6px; transition: background 0.2s;">Limpiar</a>
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
                    <th>Enlace</th>
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
                            <td>
                                <form action="servletReproductorVid" method="get">
                                    <input type="hidden" name="videoId" value="<%= vid.getId() %>">
                                    <button type="submit">Ver vídeo</button>
                                </form>
                            </td>

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
    </div>
            
</body>
</html>


