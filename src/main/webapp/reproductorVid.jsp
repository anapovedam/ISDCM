<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Video" %>
<%
    Video video = (Video) request.getAttribute("video");
    if (video == null) {
        response.sendRedirect("listadoVid.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Reproductor de Vídeo</title>
    <link href="https://vjs.zencdn.net/8.5.3/video-js.css" rel="stylesheet" />
    <script src="https://vjs.zencdn.net/8.5.3/video.min.js"></script>
</head>
<body>
    <h2><%= video.getTitle() %></h2>
    <video id="videoPlayer" class="video-js vjs-default-skin" controls preload="auto" width="640" height="360"
           data-setup="{}">
        <source src="<%= video.getUrl() %>" type="video/<%= video.getFormat() %>">
        Tu navegador no soporta reproducción de vídeo HTML5.
    </video>
    <p><strong>Descripción:</strong> <%= video.getDescription() %></p>
    <p><a href="servletListadoVid">Volver al listado</a></p>
</body>
</html>
