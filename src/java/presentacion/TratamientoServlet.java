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

@WebServlet(name = "TratamientoServlet", urlPatterns = {"/TratamientoServlet"})
public class TratamientoServlet extends HttpServlet {

    private final TratamientoService tratamientoService;

    // Constructor
    public TratamientoServlet() throws SQLException {
        this.tratamientoService = new TratamientoServiceImpl("TipoDb"); // Puedes inyectar la implementación del servicio aquí
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

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
            // Otras acciones o por defecto
        }
    }

    // Acción para crear un tratamiento
    private void crearTratamiento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                request.setAttribute("mensaje", "Entidad de salud no encontrada.");
                request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
                return;
            }
            
            EntidadDeSaludDto entidadDeSaludDto = new EntidadDeSaludDto(id, "Tipo", "Nombre", "Direccion", "Telefono", idEntidadDeSalud, null); // Puedes crear un DTO adecuado aquí
            TratamientoDto tratamientoDto = new TratamientoDto(0, descripcion, fechaInicioDate, fechaFinDate, estado, entidadDeSaludDto, null, comentarios);
            tratamientoService.guardar(entidadDeSaludDto, "Tipo");  // Llamamos al servicio para guardar el tratamiento

            request.setAttribute("mensaje", "Tratamiento creado con éxito.");
            listarTratamientos(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al crear el tratamiento.");
            request.getRequestDispatcher("/jsp/crearTratamiento.jsp").forward(request, response);
        }
    }

    // Acción para listar todos los tratamientos
    private void listarTratamientos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TratamientoDto> tratamientos = tratamientoService.listarTodosLosTratamientos();
        request.setAttribute("tratamientos", tratamientos);
        request.getRequestDispatcher("/jsp/listarTratamientos.jsp").forward(request, response);
    }

    // Acción para listar tratamientos por fecha
    private void listarTratamientosPorFecha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fechaParam = request.getParameter("fecha");
        LocalDate fecha = LocalDate.parse(fechaParam);
        List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorFecha(fecha);
        request.setAttribute("tratamientos", tratamientos);
        request.getRequestDispatcher("/jsp/listarTratamientosPorFecha.jsp").forward(request, response);
    }

    // Acción para listar tratamientos por estado
    private void listarTratamientosPorEstado(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String estado = request.getParameter("estado");
        List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientosPorEstado(estado);
        request.setAttribute("tratamientos", tratamientos);
        request.getRequestDispatcher("/jsp/listarTratamientosPorEstado.jsp").forward(request, response);
    }

    // Acción para buscar un tratamiento por ID
    private void buscarTratamientoPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);
        try {
            TratamientoDto tratamiento = tratamientoService.buscarTratamientoPorId(id);
            if (tratamiento != null) {
                request.setAttribute("tratamiento", tratamiento);
                request.getRequestDispatcher("/jsp/verTratamiento.jsp").forward(request, response);
            } else {
                response.sendRedirect("/jsp/tratamientoNoEncontrado.jsp");
            }
        } catch (Exception e) {
            response.sendRedirect("/jsp/tratamientoNoEncontrado.jsp");
        }
    }

    // Acción para actualizar un tratamiento
    private void actualizarTratamiento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String comentarios = request.getParameter("comentarios");

        try {
            // Convertimos las fechas de String a LocalDate
            LocalDate fechaInicioDate = LocalDate.parse(fechaInicio);
            LocalDate fechaFinDate = LocalDate.parse(fechaFin);

            TratamientoDto tratamientoDto = new TratamientoDto(id, descripcion, fechaInicioDate, fechaFinDate, estado, null, null, comentarios);
            tratamientoService.actualizarTratamiento(id, tratamientoDto, "Tipo"); // Aquí se puede agregar más lógica para el tipo si es necesario

            request.setAttribute("mensaje", "Tratamiento actualizado con éxito.");
            listarTratamientos(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al actualizar el tratamiento.");
            request.getRequestDispatcher("/jsp/editarTratamiento.jsp").forward(request, response);
        }
    }

    // Acción para eliminar un tratamiento
    private void eliminarTratamiento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);

        try {
            tratamientoService.eliminarTratamiento(id);
            request.setAttribute("mensaje", "Tratamiento eliminado con éxito.");
            listarTratamientos(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al eliminar el tratamiento.");
            request.getRequestDispatcher("/jsp/listarTratamientos.jsp").forward(request, response);
        }
    }

    // Acción para buscar un tratamiento por nombre
    private void buscarTratamientoPorNombre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        List<TratamientoDto> tratamientos = tratamientoService.buscarTratamientoPorNombre(nombre);
        request.setAttribute("tratamientos", tratamientos);
        request.getRequestDispatcher("/jsp/listarTratamientosPorNombre.jsp").forward(request, response);
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
