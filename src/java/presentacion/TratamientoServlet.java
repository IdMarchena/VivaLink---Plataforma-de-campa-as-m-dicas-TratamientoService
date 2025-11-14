package presentacion;

import servicio.service.TratamientoService;
import servicio.serviceImpl.TratamientoServiceImpl;
import dto.TratamientoDto;
import dto.EntidadDeSaludDto;
import external.EntidadDeSaludConsumer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import com.google.gson.Gson;

@WebServlet(name = "TratamientoServlet", urlPatterns = {"/TratamientoServlet"})
public class TratamientoServlet extends HttpServlet {

    private final TratamientoService tratamientoService;
    private final Gson gson;

    // Constructor
    public TratamientoServlet() throws SQLException {
        this.tratamientoService = new TratamientoServiceImpl("TipoDb");
        this.gson = new Gson();
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Filtra las acciones según el parámetro "action"
            if ("crear".equals(action)) {
                crearTratamiento(request, response);
            } else if ("listar".equals(action)) {
                listarTratamientos(request, response);
            } else if ("buscar".equals(action)) {
                buscarTratamientoPorId(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarTratamiento(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarTratamiento(request, response);
            } else if ("listarPorFecha".equals(action)) {
                listarTratamientosPorFecha(request, response);
            } else if ("listarPorEstado".equals(action)) {
                listarTratamientosPorEstado(request, response);
            } else if ("buscarPorNombre".equals(action)) {
                buscarTratamientoPorNombre(request, response);
            } else {
                // Acción no reconocida
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Acción no reconocida: " + action, 
                    null, 
                    HttpServletResponse.SC_BAD_REQUEST
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(jsonResponse));
            }
        } catch (Exception e) {
            JsonResponse<Object> errorResponse = new JsonResponse<>(
                false, 
                "Error interno del servidor: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(errorResponse));
        }
    }

    // Acción para crear un tratamiento
    private void crearTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String idEntidadDeSalud = request.getParameter("idEntidadDeSalud");
        String idUsuario = request.getParameter("idUsuario");
        String comentarios = request.getParameter("comentarios");
        int id = Integer.parseInt(idEntidadDeSalud);

        try {
            // Convertimos las fechas de String a LocalDate
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = LocalDate.parse(fechaFin);
            
            // Verificar si la entidad de salud existe
            if (!new EntidadDeSaludConsumer().entidadExiste(idEntidadDeSalud)) {
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Entidad de salud no encontrada", 
                    null, 
                    HttpServletResponse.SC_NOT_FOUND
                );
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }
            
            EntidadDeSaludDto entidadDeSaludDto = new EntidadDeSaludDto(id, "Tipo", "Nombre", "Direccion", "Telefono", idEntidadDeSalud, null);
            TratamientoDto tratamientoDto = new TratamientoDto(0, descripcion, fechaInicioDate, fechaFinDate, estado, entidadDeSaludDto, null, comentarios);
            tratamientoService.guardar(entidadDeSaludDto, "Tipo");

            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Tratamiento creado con éxito", 
                null, 
                HttpServletResponse.SC_CREATED
            );
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al crear el tratamiento: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para listar todos los tratamientos
    private void listarTratamientos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TratamientoDto> tratamientos = tratamientoService.listarTodosLosTratamientos();
        
        JsonResponse<List<TratamientoDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Tratamientos obtenidos exitosamente", 
            tratamientos, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para listar tratamientos por fecha
    private void listarTratamientosPorFecha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fechaParam = request.getParameter("fecha");
        
        if (fechaParam == null || fechaParam.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro fecha es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        try {
            LocalDate fecha = LocalDate.parse(fechaParam);
            List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorFecha(fecha);
            
            JsonResponse<List<TratamientoDto>> jsonResponse = new JsonResponse<>(
                true, 
                "Tratamientos por fecha obtenidos exitosamente", 
                tratamientos, 
                HttpServletResponse.SC_OK
            );
            
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Error al procesar la fecha: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para listar tratamientos por estado
    private void listarTratamientosPorEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String estado = request.getParameter("estado");
        
        if (estado == null || estado.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro estado es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorEstado(estado);
        
        JsonResponse<List<TratamientoDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Tratamientos por estado obtenidos exitosamente", 
            tratamientos, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para buscar un tratamiento por ID
    private void buscarTratamientoPorId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");
        
        if (idd == null || idd.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro id es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        try {
            int id = Integer.parseInt(idd);
            TratamientoDto tratamiento = tratamientoService.buscarTratamientoPorId(id);
            
            if (tratamiento != null) {
                JsonResponse<TratamientoDto> jsonResponse = new JsonResponse<>(
                    true, 
                    "Tratamiento encontrado", 
                    tratamiento, 
                    HttpServletResponse.SC_OK
                );
                response.getWriter().write(gson.toJson(jsonResponse));
            } else {
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Tratamiento no encontrado", 
                    null, 
                    HttpServletResponse.SC_NOT_FOUND
                );
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(jsonResponse));
            }
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de tratamiento inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Error al buscar el tratamiento: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para actualizar un tratamiento
    private void actualizarTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String comentarios = request.getParameter("comentarios");

        try {
            int id = Integer.parseInt(idd);
            // Convertimos las fechas de String a LocalDate
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = LocalDate.parse(fechaFin);

            TratamientoDto tratamientoDto = new TratamientoDto(id, descripcion, fechaInicioDate, fechaFinDate, estado, null, null, comentarios);
            tratamientoService.actualizarTratamiento(id, tratamientoDto, "Tipo");

            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Tratamiento actualizado con éxito", 
                null, 
                HttpServletResponse.SC_OK
            );
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de tratamiento inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al actualizar el tratamiento: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para eliminar un tratamiento
    private void eliminarTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");

        try {
            int id = Integer.parseInt(idd);
            tratamientoService.eliminarTratamiento(id);

            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Tratamiento eliminado con éxito", 
                null, 
                HttpServletResponse.SC_OK
            );
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de tratamiento inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al eliminar el tratamiento: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para buscar un tratamiento por nombre
    private void buscarTratamientoPorNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        
        if (nombre == null || nombre.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro nombre es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientoPorNombre(nombre);
        
        JsonResponse<List<TratamientoDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Tratamientos por nombre obtenidos exitosamente", 
            tratamientos, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}