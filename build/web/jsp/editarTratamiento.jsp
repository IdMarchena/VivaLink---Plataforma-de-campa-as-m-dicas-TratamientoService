<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Tratamiento</title>
</head>
<body>
    <h2>Editar Tratamiento</h2>

    <form action="TratamientoServlet?action=actualizar" method="post">
        <input type="hidden" name="id" value="${tratamiento.id}">

        <label for="descripcion">Descripción:</label><br>
        <textarea name="descripcion" id="descripcion" rows="4" cols="50">${tratamiento.descripcion}</textarea><br><br>

        <label for="estado">Estado:</label><br>
        <select name="estado" id="estado">
            <option value="Activo" ${tratamiento.estado == 'Activo' ? 'selected' : ''}>Activo</option>
            <option value="Finalizado" ${tratamiento.estado == 'Finalizado' ? 'selected' : ''}>Finalizado</option>
            <option value="Suspendido" ${tratamiento.estado == 'Suspendido' ? 'selected' : ''}>Suspendido</option>
        </select><br><br>

        <label for="fechaInicio">Fecha de inicio:</label><br>
        <input type="date" name="fechaInicio" id="fechaInicio" value="${tratamiento.fechaInicio}" required><br><br>

        <label for="fechaFin">Fecha de finalización:</label><br>
        <input type="date" name="fechaFin" id="fechaFin" value="${tratamiento.fechaFin}" required><br><br>

        <label for="comentarios">Comentarios:</label><br>
        <textarea name="comentarios" id="comentarios" rows="4" cols="50">${tratamiento.comentarios}</textarea><br><br>

        <input type="submit" value="Actualizar Tratamiento">
    </form>

    <br>
    <a href="TratamientoServlet?action=listar">Volver a la lista</a>
</body>
</html>
