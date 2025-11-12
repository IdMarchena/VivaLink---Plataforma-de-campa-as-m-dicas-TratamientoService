<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Tratamiento</title>
</head>
<body>
    <h2>Crear Tratamiento</h2>
    <form action="TratamientoServlet?action=crear" method="post">
        <label for="descripcion">Descripción:</label><br>
        <textarea name="descripcion" id="descripcion" rows="4" cols="50"></textarea><br><br>

        <label for="estado">Estado:</label><br>
        <select name="estado" id="estado">
            <option value="Activo">Activo</option>
            <option value="Finalizado">Finalizado</option>
            <option value="Suspendido">Suspendido</option>
        </select><br><br>

        <label for="fechaInicio">Fecha de inicio:</label><br>
        <input type="date" name="fechaInicio" id="fechaInicio" required><br><br>

        <label for="fechaFin">Fecha de finalización:</label><br>
        <input type="date" name="fechaFin" id="fechaFin" required><br><br>

        <label for="comentarios">Comentarios:</label><br>
        <textarea name="comentarios" id="comentarios" rows="4" cols="50"></textarea><br><br>

        <label for="idEntidadDeSalud">Entidad de Salud:</label><br>
        <input type="text" name="idEntidadDeSalud" id="idEntidadDeSalud" required><br><br>

        <input type="submit" value="Crear Tratamiento">
    </form>

    <br>
    <a href="TratamientoServlet?action=listar">Ver tratamientos</a>
</body>
</html>
