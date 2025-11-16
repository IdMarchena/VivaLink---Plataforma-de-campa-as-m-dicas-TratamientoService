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
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Acción no reconocida: " + action));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error interno del servidor: " + e.getMessage()));
        }
    }

    // Acción para crear un tratamiento
    private void crearTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String idEntidadDeSalud = request.getParameter("idEntidadDeSalud");
        String comentarios = request.getParameter("comentarios");

        try {
            // Validar parámetros requeridos
            if (descripcion == null || descripcion.isEmpty() || 
                estado == null || estado.isEmpty() ||
                fechaInicio == null || fechaInicio.isEmpty() ||
                idEntidadDeSalud == null || idEntidadDeSalud.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos requeridos: descripcion, estado, fechaInicio, idEntidadDeSalud"));
                return;
            }

            // Convertir fechas
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = fechaFin != null && !fechaFin.isEmpty() ? LocalDate.parse(fechaFin) : null;
            
            int idEntidad = Integer.parseInt(idEntidadDeSalud);
            
            // Verificar si la entidad de salud existe
            if (!new EntidadDeSaludConsumer().entidadExiste(idEntidadDeSalud)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Entidad de salud no encontrada"));
                return;
            }
            
            // Crear DTOs necesarios
            EntidadDeSaludDto entidadDeSaludDto = new EntidadDeSaludDto(idEntidad, "Tipo", "Nombre", "Direccion", "Telefono", idEntidadDeSalud, null);
            TratamientoDto tratamientoDto = new TratamientoDto(0, descripcion, fechaInicioDate, fechaFinDate, estado, entidadDeSaludDto, null, comentarios);
            
            // Guardar tratamiento
            tratamientoService.guardar(entidadDeSaludDto, "Tipo");

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson("Tratamiento creado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de entidad de salud inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al crear tratamiento: " + e.getMessage()));
        }
    }

    // Acción para listar todos los tratamientos
    private void listarTratamientos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<TratamientoDto> tratamientos = tratamientoService.listarTodosLosTratamientos();
            response.getWriter().write(gson.toJson(tratamientos));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar tratamientos: " + e.getMessage()));
        }
    }

    // Acción para listar tratamientos por fecha
    private void listarTratamientosPorFecha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fechaParam = request.getParameter("fecha");
        
        if (fechaParam == null || fechaParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro fecha es requerido"));
            return;
        }
        
        try {
            LocalDate fecha = LocalDate.parse(fechaParam);
            List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorFecha(fecha);
            response.getWriter().write(gson.toJson(tratamientos));
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("Error al procesar la fecha: " + e.getMessage()));
        }
    }

    // Acción para listar tratamientos por estado
    private void listarTratamientosPorEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String estado = request.getParameter("estado");
        
        if (estado == null || estado.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro estado es requerido"));
            return;
        }
        
        try {
            List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorEstado(estado);
            response.getWriter().write(gson.toJson(tratamientos));
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar tratamientos por estado: " + e.getMessage()));
        }
    }

    // Acción para buscar un tratamiento por ID
    private void buscarTratamientoPorId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro id es requerido"));
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            TratamientoDto tratamiento = tratamientoService.buscarTratamientoPorId(id);
            
            if (tratamiento != null) {
                response.getWriter().write(gson.toJson(tratamiento));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Tratamiento no encontrado"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de tratamiento inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar tratamiento: " + e.getMessage()));
        }
    }

    // Acción para actualizar un tratamiento
    private void actualizarTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String comentarios = request.getParameter("comentarios");

        try {
            int id = Integer.parseInt(idStr);
            
            // Validar parámetros requeridos
            if (descripcion == null || descripcion.isEmpty() || 
                estado == null || estado.isEmpty() ||
                fechaInicio == null || fechaInicio.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos requeridos: descripcion, estado, fechaInicio"));
                return;
            }

            // Convertir fechas
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = fechaFin != null && !fechaFin.isEmpty() ? LocalDate.parse(fechaFin) : null;

            TratamientoDto tratamientoDto = new TratamientoDto(id, descripcion, fechaInicioDate, fechaFinDate, estado, null, null, comentarios);
            tratamientoService.actualizarTratamiento(id, tratamientoDto, "Tipo");

            response.getWriter().write(gson.toJson("Tratamiento actualizado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de tratamiento inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al actualizar tratamiento: " + e.getMessage()));
        }
    }

    // Acción para eliminar un tratamiento
    private void eliminarTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            tratamientoService.eliminarTratamiento(id);

            response.getWriter().write(gson.toJson("Tratamiento eliminado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de tratamiento inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al eliminar tratamiento: " + e.getMessage()));
        }
    }

    // Acción para buscar un tratamiento por nombre
    private void buscarTratamientoPorNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        
        if (nombre == null || nombre.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro nombre es requerido"));
            return;
        }
        
        try {
            List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientoPorNombre(nombre);
            response.getWriter().write(gson.toJson(tratamientos));
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar tratamientos por nombre: " + e.getMessage()));
        }
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