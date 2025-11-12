package dao;

import dao.conexion.DatabaseConnection;
import factory.DatabaseConnectionFactory;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Tratamiento;
import modelo.Usuario;

public class TratamientoDaoPostgre implements TratamientoDao {
    private final Connection conn;

    // Constructor que recibe el tipo de base de datos
    public TratamientoDaoPostgre(String tipoDb) throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnectionFactory.connection(tipoDb);
        this.conn = dbConnection.getConection();
    }

    // Método para buscar un tratamiento por ID
    @Override
    public Tratamiento buscarTratamientoPorId(int id) {
        String sql = "SELECT * FROM tratamientos WHERE id_tratamiento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getObject("fecha_inicio", LocalDate.class),
                    rs.getObject("fecha_fin", LocalDate.class),
                    rs.getString("estado"),
                    new EntidadDeSalud(rs.getInt("id_entidad"), rs.getString("tipo"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("identificador"), null),
                    new Usuario(rs.getInt("id_usuario"), rs.getString("telefono"), rs.getObject("fecha_nacimiento", LocalDate.class), rs.getString("sexo"), rs.getString("direccion")),
                    rs.getString("comentarios")
                );
            }
        } catch (SQLException e) {
        }
        return null; // Retorna null si no se encuentra el tratamiento
    }

    // Verificar si un tratamiento existe por ID
    @Override
    public boolean verificarSiElTratamientoExiste(int id) {
        String sql = "SELECT COUNT(*) FROM tratamientos WHERE id_tratamiento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    // Guardar un tratamiento
    @Override
    public void guardar(EntidadDeSalud entidad, String tipo) {
        String sql = "INSERT INTO tratamientos (descripcion, fecha_inicio, fecha_fin, estado, id_entidad, id_usuario, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo); // Descripción del tratamiento (puedes personalizar este campo)
            ps.setDate(2, Date.valueOf(LocalDate.now())); // Fecha de inicio, asumiendo que es hoy
            ps.setDate(3, Date.valueOf(LocalDate.now().plusMonths(1))); // Fecha de fin (puedes modificarlo según sea necesario)
            ps.setString(4, "Activo"); // Estado inicial
            ps.setInt(5, entidad.getId()); // Relación con la entidad de salud
            ps.setInt(6, entidad.getUsuario().getId()); // Relación con el usuario (gerente o médico)
            ps.setString(7, "Tratamiento en progreso"); // Comentarios iniciales
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    // Buscar tratamientos por nombre (esto asumo que buscas tratamientos relacionados a una descripción)
    @Override
    public List<Tratamiento> buscarTratamientoPorSuNombre(String tipo) {
        String sql = "SELECT * FROM tratamientos WHERE descripcion LIKE ?";
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tipo + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getObject("fecha_inicio", LocalDate.class),
                    rs.getObject("fecha_fin", LocalDate.class),
                    rs.getString("estado"),
                    new EntidadDeSalud(rs.getInt("id_entidad"), rs.getString("tipo"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("identificador"), null),
                    new Usuario(rs.getInt("id_usuario"), rs.getString("telefono"), rs.getObject("fecha_nacimiento", LocalDate.class), rs.getString("sexo"), rs.getString("direccion")),
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
        }
        return tratamientos;
    }

    // Listar todos los tratamientos
    @Override
    public List<Tratamiento> listarTodosLosTratamietnos() {
        String sql = "SELECT * FROM tratamientos";
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getObject("fecha_inicio", LocalDate.class),
                    rs.getObject("fecha_fin", LocalDate.class),
                    rs.getString("estado"),
                    new EntidadDeSalud(rs.getInt("id_entidad"), rs.getString("tipo"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("identificador"), null),
                    new Usuario(rs.getInt("id_usuario"), rs.getString("telefono"), rs.getObject("fecha_nacimiento", LocalDate.class), rs.getString("sexo"), rs.getString("direccion")),
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
        }
        return tratamientos;
    }

    // Actualizar un tratamiento
    @Override
    public void actualizarTratamiento(int id, Tratamiento tratamiento, String tipo) {
        String sql = "UPDATE tratamientos SET descripcion = ?, fecha_inicio = ?, fecha_fin = ?, estado = ?, id_entidad = ?, id_usuario = ?, comentarios = ? WHERE id_tratamiento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setDate(2, Date.valueOf(tratamiento.getFechaInicio()));
            ps.setDate(3, Date.valueOf(tratamiento.getFechaFin()));
            ps.setString(4, tratamiento.getEstado());
            ps.setInt(5, tratamiento.getEntidadDeSalud().getId());
            ps.setInt(6, tratamiento.getUsuario().getId());
            ps.setString(7, tratamiento.getComentarios());
            ps.setInt(8, id);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    // Eliminar un tratamiento
    @Override
    public void eliminarTratamiento(int id) {
        String sql = "DELETE FROM tratamientos WHERE id_tratamiento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    // Buscar tratamientos por fecha
    @Override
    public List<Tratamiento> buscarTratamientosPorFecha(LocalDate fecha) {
        String sql = "SELECT * FROM tratamientos WHERE fecha_inicio = ?";
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getObject("fecha_inicio", LocalDate.class),
                    rs.getObject("fecha_fin", LocalDate.class),
                    rs.getString("estado"),
                    new EntidadDeSalud(rs.getInt("id_entidad"), rs.getString("tipo"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("identificador"), null),
                    new Usuario(rs.getInt("id_usuario"), rs.getString("telefono"), rs.getObject("fecha_nacimiento", LocalDate.class), rs.getString("sexo"), rs.getString("direccion")),
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
        }
        return tratamientos;
    }

    // Buscar tratamientos por estado
    @Override
    public List<Tratamiento> buscarTratamientosPorEstado(String estado) {
        String sql = "SELECT * FROM tratamientos WHERE estado = ?";
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getObject("fecha_inicio", LocalDate.class),
                    rs.getObject("fecha_fin", LocalDate.class),
                    rs.getString("estado"),
                    new EntidadDeSalud(rs.getInt("id_entidad"), rs.getString("tipo"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("identificador"), null),
                    new Usuario(rs.getInt("id_usuario"), rs.getString("telefono"), rs.getObject("fecha_nacimiento", LocalDate.class), rs.getString("sexo"), rs.getString("direccion")),
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
        }
        return tratamientos;
    }
}
