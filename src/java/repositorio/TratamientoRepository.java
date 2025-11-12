package repositorio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import dao.TratamientoDao;
import factory.TratamientoDaoFactory;
import modelo.Tratamiento;
import modelo.EntidadDeSalud;

/**
 *
 * @author Usuario
 */
public class TratamientoRepository {
    private final TratamientoDao tratamientoDao;

    // Constructor que inicializa el Dao dependiendo del tipo de base de datos
    public TratamientoRepository(String tipoDb) throws SQLException {
        tratamientoDao = TratamientoDaoFactory.dao(tipoDb);
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
    public void guardarTratamiento(EntidadDeSalud entidad, String tipo) {
        tratamientoDao.guardar(entidad, tipo);
    }

    // Buscar tratamientos por nombre
    public List<Tratamiento> buscarTratamientosPorNombre(String tipo) {
        return tratamientoDao.buscarTratamientoPorSuNombre(tipo);
    }

    // Listar todos los tratamientos
    public List<Tratamiento> listarTodosLosTratamientos() {
        return tratamientoDao.listarTodosLosTratamietnos();
    }

    // Actualizar un tratamiento en la base de datos
    public void actualizarTratamiento(int id, Tratamiento tratamiento, String tipo) {
        tratamientoDao.actualizarTratamiento(id, tratamiento, tipo);
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
