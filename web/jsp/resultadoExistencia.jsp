<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultado de búsqueda</title>
</head>
<body>
    <h2>Resultados de búsqueda</h2>

    <c:if test="${not empty tratamientos}">
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descripción</th>
                    <th>Estado</th>
                    <th>Fecha de Inicio</th>
                    <th>Fecha de Fin</th>
                    <th>Comentarios</th>
                    <th>Entidad de Salud</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tratamiento" items="${tratamientos}">
                    <tr>
                        <td>${tratamiento.id}</td>
                        <td>${tratamiento.descripcion}</td>
                        <td>${tratamiento.estado}</td>
                        <td>${tratamiento.fechaInicio}</td>
                        <td>${tratamiento.fechaFin}</td>
                        <td>${tratamiento.comentarios}</td>
                        <td>${tratamiento.entidadDeSalud.nombre}</td>
                        <td>
                            <a href="TratamientoServlet?action=buscar&id=${tratamiento.id}">Ver</a> |
                            <a href="TratamientoServlet?action=actualizar&id=${tratamiento.id}">Editar</a> |
                            <a href="TratamientoServlet?action=eliminar&id=${tratamiento.id}">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty tratamientos}">
        <p>No se encontraron tratamientos con ese nombre.</p>
    </c:if>

    <br>
    <a href="TratamientoServlet?action=crear">Crear nuevo tratamiento</a>
</body>
</html>
