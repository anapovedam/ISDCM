<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error | WebAppISDCM</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
            background-color: #f8d7da;
            color: #721c24;
        }
        .container {
            max-width: 500px;
            margin: auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            font-size: 24px;
        }
        p {
            font-size: 18px;
        }
        .button {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            color: white;
            background-color: #721c24;
            text-decoration: none;
            border-radius: 5px;
        }
        .button:hover {
            background-color: #a94442;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>¡Oops! Ha ocurrido un error</h1>
        <p><%= request.getAttribute("mensajeError") != null ? request.getAttribute("mensajeError") : "Se ha producido un error inesperado." %></p>
        <a href="login.jsp" class="button">Volver al inicio</a>
    </div>
</body>
</html>
