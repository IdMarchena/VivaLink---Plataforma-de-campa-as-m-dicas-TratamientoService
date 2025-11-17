package presentacion;

import servicio.service.TratamientoService;
import servicio.serviceImpl.TratamientoServiceImpl;
import dto.TratamientoDto;
import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
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
import com.google.gson.GsonBuilder;

@WebServlet(name = "TratamientoServlet", urlPatterns = {"/TratamientoServlet"})
public class TratamientoServlet extends HttpServlet {

    private final TratamientoService tratamientoService;
    private final Gson gson;

    public TratamientoServlet() throws SQLException {
        this.tratamientoService = new TratamientoServiceImpl("postgres");
        
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setDateFormat("yyyy-MM-dd")
            .create();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (null == action) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Acción no reconocida: " + action));
            } else switch (action) {
                case "crear" -> crearTratamiento(request, response);
                case "listar" -> listarTratamientos(request, response);
                case "buscar" -> buscarTratamientoPorId(request, response);
                case "actualizar" -> actualizarTratamiento(request, response);
                case "eliminar" -> eliminarTratamiento(request, response);
                case "listarPorFecha" -> listarTratamientosPorFecha(request, response);
                case "listarPorEstado" -> listarTratamientosPorEstado(request, response);
                case "buscarPorDescripcion" -> buscarTratamientoPorDescripcion(request, response);
                default -> {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson("Acción no reconocida: " + action));
                }
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error interno del servidor: " + e.getMessage()));
        }
    }

    // Acción para crear un tratamiento - CORREGIDO
    private void crearTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Obtener todos los parámetros necesarios para Tratamiento
            String descripcion = request.getParameter("descripcion");
            String estado = request.getParameter("estado");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String comentarios = request.getParameter("comentarios");

            // Obtener parámetros para EntidadDeSalud
            String idEntidadDeSalud = request.getParameter("idEntidadDeSalud");
            String tipoEntidad = request.getParameter("tipoEntidad");
            String nombreEntidad = request.getParameter("nombreEntidad");
            String direccionEntidad = request.getParameter("direccionEntidad");
            String telefonoEntidad = request.getParameter("telefonoEntidad");
            String identificadorEntidad = request.getParameter("identificadorEntidad");

            // Obtener parámetros para Usuario
            String idUsuario = request.getParameter("idUsuario");
            String telefonoUsuario = request.getParameter("telefonoUsuario");
            String fechaNacimientoUsuario = request.getParameter("fechaNacimientoUsuario");
            String sexoUsuario = request.getParameter("sexoUsuario");
            String direccionUsuario = request.getParameter("direccionUsuario");

            // Validar parámetros requeridos para Tratamiento
            if (descripcion == null || descripcion.isEmpty() || 
                estado == null || estado.isEmpty() ||
                fechaInicio == null || fechaInicio.isEmpty() ||
                idEntidadDeSalud == null || idEntidadDeSalud.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos requeridos: descripcion, estado, fechaInicio, idEntidadDeSalud, idUsuario"));
                return;
            }

            // Convertir fechas
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = (fechaFin != null && !fechaFin.isEmpty()) ? LocalDate.parse(fechaFin) : null;
            LocalDate fechaNacimientoDate = (fechaNacimientoUsuario != null && !fechaNacimientoUsuario.isEmpty()) 
                ? LocalDate.parse(fechaNacimientoUsuario) 
                : LocalDate.now(); // Valor por defecto si no se proporciona
            
            int idEntidad = Integer.parseInt(idEntidadDeSalud);
            int idUser = Integer.parseInt(idUsuario);
            
            // Crear UsuarioDto con datos obtenidos de los parámetros
            UsuarioDto usuarioDto = new UsuarioDto(
                idUser,
                telefonoUsuario != null ? telefonoUsuario : "3000000000", // Valor por defecto
                fechaNacimientoDate,
                sexoUsuario != null ? sexoUsuario : "M", // Valor por defecto
                direccionUsuario != null ? direccionUsuario : "Dirección no especificada" // Valor por defecto
            );
            
            // Crear EntidadDeSaludDto con datos obtenidos de los parámetros
            EntidadDeSaludDto entidadDeSaludDto = new EntidadDeSaludDto(
                idEntidad,
                tipoEntidad != null ? tipoEntidad : "EPS", // Valor por defecto
                nombreEntidad != null ? nombreEntidad : "Entidad Temporal", // Valor por defecto
                direccionEntidad != null ? direccionEntidad : "Dirección temporal", // Valor por defecto
                telefonoEntidad != null ? telefonoEntidad : "3000000000", // Valor por defecto
                identificadorEntidad != null ? identificadorEntidad : "identificador-temporal", // Valor por defecto
                usuarioDto // El usuario gerente de la entidad
            );
            
            // Crear TratamientoDto con todos los datos
            TratamientoDto tratamientoDto = new TratamientoDto(
                0, // ID se generará automáticamente
                descripcion,
                fechaInicioDate,
                fechaFinDate,
                estado,
                entidadDeSaludDto,
                usuarioDto,
                comentarios != null ? comentarios : "" // Valor por defecto
            );
            
            // Guardar tratamiento usando el servicio
            tratamientoService.guardar(tratamientoDto);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson("Tratamiento creado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de entidad de salud o usuario inválido"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al crear tratamiento: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    // Acción para listar todos los tratamientos
    private void listarTratamientos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<TratamientoDto> tratamientos = tratamientoService.listarTodosLosTratamientos();
            
            if (tratamientos.isEmpty()) {
                response.getWriter().write(gson.toJson("No se encontraron tratamientos"));
            } else {
                response.getWriter().write(gson.toJson(tratamientos));
            }
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar tratamientos: " + e.getMessage()));
        }
    }

    // Acción para buscar tratamientos por descripción
    private void buscarTratamientoPorDescripcion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String descripcion = request.getParameter("descripcion");
        
        if (descripcion == null || descripcion.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro descripcion es requerido"));
            return;
        }
        
        try {
            List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientoPorDescripcion(descripcion);
            response.getWriter().write(gson.toJson(tratamientos));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar tratamientos por descripción: " + e.getMessage()));
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
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar tratamientos por fecha: " + e.getMessage()));
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
            
        } catch (Exception e) {
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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar tratamiento: " + e.getMessage()));
        }
    }

    // Acción para actualizar un tratamiento - CORREGIDO
    private void actualizarTratamiento(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Obtener parámetros para Tratamiento
            String idStr = request.getParameter("id");
            String descripcion = request.getParameter("descripcion");
            String estado = request.getParameter("estado");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String comentarios = request.getParameter("comentarios");

            // Obtener parámetros para EntidadDeSalud
            String idEntidadDeSalud = request.getParameter("idEntidadDeSalud");
            String tipoEntidad = request.getParameter("tipoEntidad");
            String nombreEntidad = request.getParameter("nombreEntidad");
            String direccionEntidad = request.getParameter("direccionEntidad");
            String telefonoEntidad = request.getParameter("telefonoEntidad");
            String identificadorEntidad = request.getParameter("identificadorEntidad");

            // Obtener parámetros para Usuario
            String idUsuario = request.getParameter("idUsuario");
            String telefonoUsuario = request.getParameter("telefonoUsuario");
            String fechaNacimientoUsuario = request.getParameter("fechaNacimientoUsuario");
            String sexoUsuario = request.getParameter("sexoUsuario");
            String direccionUsuario = request.getParameter("direccionUsuario");

            int id = Integer.parseInt(idStr);
            
            // Validar parámetros requeridos
            if (descripcion == null || descripcion.isEmpty() || 
                estado == null || estado.isEmpty() ||
                fechaInicio == null || fechaInicio.isEmpty() ||
                idEntidadDeSalud == null || idEntidadDeSalud.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos requeridos: descripcion, estado, fechaInicio, idEntidadDeSalud, idUsuario"));
                return;
            }

            // Convertir fechas
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = (fechaFin != null && !fechaFin.isEmpty()) ? LocalDate.parse(fechaFin) : null;
            LocalDate fechaNacimientoDate = (fechaNacimientoUsuario != null && !fechaNacimientoUsuario.isEmpty()) 
                ? LocalDate.parse(fechaNacimientoUsuario) 
                : LocalDate.now();

            int idEntidad = Integer.parseInt(idEntidadDeSalud);
            int idUser = Integer.parseInt(idUsuario);

            // Crear UsuarioDto con datos de parámetros
            UsuarioDto usuarioDto = new UsuarioDto(
                idUser,
                telefonoUsuario != null ? telefonoUsuario : "3000000000",
                fechaNacimientoDate,
                sexoUsuario != null ? sexoUsuario : "M",
                direccionUsuario != null ? direccionUsuario : "Dirección no especificada"
            );

            // Crear EntidadDeSaludDto con datos de parámetros
            EntidadDeSaludDto entidadDeSaludDto = new EntidadDeSaludDto(
                idEntidad,
                tipoEntidad != null ? tipoEntidad : "EPS",
                nombreEntidad != null ? nombreEntidad : "Entidad Temporal",
                direccionEntidad != null ? direccionEntidad : "Dirección temporal",
                telefonoEntidad != null ? telefonoEntidad : "3000000000",
                identificadorEntidad != null ? identificadorEntidad : "identificador-temporal",
                usuarioDto
            );

            // Crear TratamientoDto con todos los datos
            TratamientoDto tratamientoDto = new TratamientoDto(
                id,
                descripcion,
                fechaInicioDate,
                fechaFinDate,
                estado,
                entidadDeSaludDto,
                usuarioDto,
                comentarios != null ? comentarios : ""
            );
            
            tratamientoService.actualizarTratamiento(id, tratamientoDto);

            response.getWriter().write(gson.toJson("Tratamiento actualizado con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de tratamiento inválido"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al actualizar tratamiento: " + e.getMessage()));
            e.printStackTrace();
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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al eliminar tratamiento: " + e.getMessage()));
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