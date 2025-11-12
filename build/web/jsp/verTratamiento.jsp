<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Tratamiento</title>
</head>
<body>
    <h2>Detalles del Tratamiento</h2>

    <c:if test="${not empty tratamiento}">
        <p><strong>ID:</strong> ${tratamiento.id}</p>
        <p><strong>Descripción:</strong> ${tratamiento.descripcion}</p>
        <p><strong>Estado:</strong> ${tratamiento.estado}</p>
        <p><strong>Fecha de Inicio:</strong> ${tratamiento.fechaInicio}</p>
        <p><strong>Fecha de Fin:</strong> ${tratamiento.fechaFin}</p>
        <p><strong>Comentarios:</strong> ${tratamiento.comentarios}</p>
        <p><strong>Entidad de Salud:</strong> ${tratamiento.entidadDeSalud.nombre}</p>
    </c:if>

    <br>
    <a href="TratamientoServlet?action=listar">Volver a la lista</a>
</body>
</html>
