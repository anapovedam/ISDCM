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
    <meta charset="UTF-8">
    <link href="https://vjs.zencdn.net/8.5.3/video-js.css" rel="stylesheet" />
    <script src="https://vjs.zencdn.net/8.5.3/video.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }

        .video-container {
            max-width: 800px;
            margin: 40px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }

        .video-title {
            font-size: 1.8em;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .video-js {
            width: 100%;
            height: auto;
            border-radius: 10px;
        }

        .description-box {
            margin-top: 20px;
            padding: 15px;
            background-color: #f1f1f1;
            border-left: 5px solid #007bff;
            border-radius: 5px;
        }

        .back-link {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="video-container">
        <div class="video-title"><%= video.getTitle() %></div>

        <video
            id="videoPlayer"
            class="video-js vjs-default-skin vjs-fluid"
            controls
            preload="auto"
            data-setup="{}"
        >
            <source src="<%= video.getUrl() %>" type="video/<%= video.getFormat() %>">
            Tu navegador no soporta reproducción de vídeo HTML5.
        </video>

            
        <div class="video-info">
            <p><strong>Autor:</strong> <%= video.getAuthor() %></p>
            <p><strong>Fecha de creación:</strong> <%= video.getCreationDate() %></p>
            <p><strong>Duración:</strong> <%= video.getDuration() %> min</p>
            <p><strong>Reproducciones:</strong> <%= video.getViews() %></p>
            <p><strong>Formato:</strong> <%= video.getFormat() %></p>
        </div>

        <div class="description-box">
            <p><strong>Descripción:</strong> <%= video.getDescription() %></p>
        </div>

        <a class="back-link" href="servletListadoVid">← Volver al listado</a>
    </div>
</body>
</html>
