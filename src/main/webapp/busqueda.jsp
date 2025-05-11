<%-- 
    Document   : busqueda
    Created on : 11 may 2025, 16:24:27
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

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
    </body>
</html>
