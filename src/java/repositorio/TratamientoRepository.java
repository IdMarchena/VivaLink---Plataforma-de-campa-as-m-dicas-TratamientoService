package repositorio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import dao.TratamientoDao;
import factory.TratamientoDaoFactory;
import modelo.Tratamiento;

/**
 *
 * @author Usuario
 */
public class TratamientoRepository {
    private final TratamientoDao tratamientoDao;

    // Constructor que inicializa el Dao dependiendo del tipo de base de datos
    public TratamientoRepository() throws SQLException {
        tratamientoDao = TratamientoDaoFactory.dao("postgres");
    }

    // Buscar un tratamiento por ID
    public Tratamiento buscarTratamientoPorId(int id) {
        return tratamientoDao.buscarTratamientoPorId(id);
    }

    // Verificar si un tratamiento existe por ID
    public boolean verificarSiElTratamientoExiste(int id) {
        return tratamientoDao.verificarSiElTratamientoExiste(id);
    }

    // Guardar un tratamiento en la base de datos
    public void guardarTratamiento(Tratamiento tratamiento) {
        tratamientoDao.guardar(tratamiento);
    }

    // Buscar tratamientos por nombre
    public List<Tratamiento> buscarTratamientosPorNombre(String tipo) {
        return tratamientoDao.buscarTratamientoPorDescripcion(tipo);
    }

    // Listar todos los tratamientos
    public List<Tratamiento> listarTodosLosTratamientos() {
        return tratamientoDao.listarTodosLosTratamientos();
    }

    // Actualizar un tratamiento en la base de datos
    public void actualizarTratamiento(int id, Tratamiento tratamiento) {
        tratamientoDao.actualizarTratamiento(id, tratamiento);
    }

    // Eliminar un tratamiento de la base de datos
    public void eliminarTratamiento(int id) {
        tratamientoDao.eliminarTratamiento(id);
    }

    // Buscar tratamientos por fecha
    public List<Tratamiento> buscarTratamientosPorFecha(LocalDate fecha) {
        return tratamientoDao.buscarTratamientosPorFecha(fecha);
    }

    // Buscar tratamientos por estado
    public List<Tratamiento> buscarTratamientosPorEstado(String estado) {
        return tratamientoDao.buscarTratamientosPorEstado(estado);
    }
}
