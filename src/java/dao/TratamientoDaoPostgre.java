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

    public TratamientoDaoPostgre(DatabaseConnection connection) throws SQLException {
        DatabaseConnection db = DatabaseConnectionFactory.connection("postgres");
        this.conn = db.getConnection();
    }

    // Método para buscar un tratamiento por ID
    @Override
    public Tratamiento buscarTratamientoPorId(int id) {
        String sql = """
            SELECT t.*, e.tipo, e.nombre, e.direccion, e.telefono, e.identificador,
                   u.telefono as user_telefono, u.fecha_nacimiento, u.sexo, u.direccion as user_direccion
            FROM tratamientos t
            JOIN entidades_de_salud e ON t.id_entidad = e.id
            JOIN usuarios u ON t.id_usuario = u.id
            WHERE t.id_tratamiento = ?
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Crear EntidadDeSalud con los datos de la consulta
                EntidadDeSalud entidad = new EntidadDeSalud(
                    rs.getInt("id_entidad"),
                    rs.getString("tipo"),
                    rs.getString("nombre"), 
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("identificador"),
                    null
                );

                // Crear Usuario con los datos de la consulta
                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("user_telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("sexo"),
                    rs.getString("user_direccion")
                );

                return new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                    rs.getString("estado"),
                    entidad,
                    usuario,
                    rs.getString("comentarios")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando tratamiento por ID: " + e.getMessage(), e);
        }
        return null;
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
            throw new RuntimeException("Error verificando existencia del tratamiento: " + e.getMessage(), e);
        }
        return false;
    }

    // Guardar un tratamiento - IMPLEMENTACIÓN CORRECTA
    @Override
    public void guardar(Tratamiento tratamiento) {
        String sql = "INSERT INTO tratamientos (descripcion, fecha_inicio, fecha_fin, estado, id_entidad, id_usuario, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tratamiento.getDescripcion());
            ps.setDate(2, Date.valueOf(tratamiento.getFechaInicio()));
            
            if (tratamiento.getFechaFin() != null) {
                ps.setDate(3, Date.valueOf(tratamiento.getFechaFin()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            ps.setString(4, tratamiento.getEstado());
            ps.setInt(5, tratamiento.getEntidadDeSalud().getId());
            ps.setInt(6, tratamiento.getUsuario().getId());
            ps.setString(7, tratamiento.getComentarios());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando tratamiento: " + e.getMessage(), e);
        }
    }

    // Buscar tratamientos por descripción - NOMBRE CORREGIDO
    @Override
    public List<Tratamiento> buscarTratamientoPorDescripcion(String descripcion) {
        String sql = """
            SELECT t.*, e.tipo, e.nombre, e.direccion, e.telefono, e.identificador,
                   u.telefono as user_telefono, u.fecha_nacimiento, u.sexo, u.direccion as user_direccion
            FROM tratamientos t
            JOIN entidades_de_salud e ON t.id_entidad = e.id
            JOIN usuarios u ON t.id_usuario = u.id
            WHERE t.descripcion LIKE ?
        """;
        
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + descripcion + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EntidadDeSalud entidad = new EntidadDeSalud(
                    rs.getInt("id_entidad"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("identificador"),
                    null
                );

                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("user_telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("sexo"),
                    rs.getString("user_direccion")
                );

                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                    rs.getString("estado"),
                    entidad,
                    usuario,
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando tratamientos por descripción: " + e.getMessage(), e);
        }
        return tratamientos;
    }

    // Listar todos los tratamientos - NOMBRE CORREGIDO
    @Override
    public List<Tratamiento> listarTodosLosTratamientos() {
        String sql = """
            SELECT t.*, e.tipo, e.nombre, e.direccion, e.telefono, e.identificador,
                   u.telefono as user_telefono, u.fecha_nacimiento, u.sexo, u.direccion as user_direccion
            FROM tratamientos t
            JOIN entidades_de_salud e ON t.id_entidad = e.id
            JOIN usuarios u ON t.id_usuario = u.id
            ORDER BY t.id_tratamiento
        """;
        
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EntidadDeSalud entidad = new EntidadDeSalud(
                    rs.getInt("id_entidad"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("identificador"),
                    null
                );

                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("user_telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("sexo"),
                    rs.getString("user_direccion")
                );

                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                    rs.getString("estado"),
                    entidad,
                    usuario,
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando tratamientos: " + e.getMessage(), e);
        }
        return tratamientos;
    }

    // Actualizar un tratamiento - SIN PARÁMETRO TIPO
    @Override
    public void actualizarTratamiento(int id, Tratamiento tratamiento) {
        String sql = """
            UPDATE tratamientos 
            SET descripcion = ?, fecha_inicio = ?, fecha_fin = ?, estado = ?, 
                id_entidad = ?, id_usuario = ?, comentarios = ? 
            WHERE id_tratamiento = ?
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tratamiento.getDescripcion());
            ps.setDate(2, Date.valueOf(tratamiento.getFechaInicio()));
            
            if (tratamiento.getFechaFin() != null) {
                ps.setDate(3, Date.valueOf(tratamiento.getFechaFin()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            ps.setString(4, tratamiento.getEstado());
            ps.setInt(5, tratamiento.getEntidadDeSalud().getId());
            ps.setInt(6, tratamiento.getUsuario().getId());
            ps.setString(7, tratamiento.getComentarios());
            ps.setInt(8, id);
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("No se encontró el tratamiento con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando tratamiento: " + e.getMessage(), e);
        }
    }

    // Eliminar un tratamiento
    @Override
    public void eliminarTratamiento(int id) {
        String sql = "DELETE FROM tratamientos WHERE id_tratamiento = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new RuntimeException("No se encontró el tratamiento con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando tratamiento: " + e.getMessage(), e);
        }
    }

    // Buscar tratamientos por fecha
    @Override
    public List<Tratamiento> buscarTratamientosPorFecha(LocalDate fecha) {
        String sql = """
            SELECT t.*, e.tipo, e.nombre, e.direccion, e.telefono, e.identificador,
                   u.telefono as user_telefono, u.fecha_nacimiento, u.sexo, u.direccion as user_direccion
            FROM tratamientos t
            JOIN entidades_de_salud e ON t.id_entidad = e.id
            JOIN usuarios u ON t.id_usuario = u.id
            WHERE t.fecha_inicio = ?
        """;
        
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EntidadDeSalud entidad = new EntidadDeSalud(
                    rs.getInt("id_entidad"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("identificador"),
                    null
                );

                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("user_telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("sexo"),
                    rs.getString("user_direccion")
                );

                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                    rs.getString("estado"),
                    entidad,
                    usuario,
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando tratamientos por fecha: " + e.getMessage(), e);
        }
        return tratamientos;
    }

    // Buscar tratamientos por estado
    @Override
    public List<Tratamiento> buscarTratamientosPorEstado(String estado) {
        String sql = """
            SELECT t.*, e.tipo, e.nombre, e.direccion, e.telefono, e.identificador,
                   u.telefono as user_telefono, u.fecha_nacimiento, u.sexo, u.direccion as user_direccion
            FROM tratamientos t
            JOIN entidades_de_salud e ON t.id_entidad = e.id
            JOIN usuarios u ON t.id_usuario = u.id
            WHERE t.estado = ?
        """;
        
        List<Tratamiento> tratamientos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                EntidadDeSalud entidad = new EntidadDeSalud(
                    rs.getInt("id_entidad"),
                    rs.getString("tipo"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("identificador"),
                    null
                );

                Usuario usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("user_telefono"),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    rs.getString("sexo"),
                    rs.getString("user_direccion")
                );

                tratamientos.add(new Tratamiento(
                    rs.getInt("id_tratamiento"),
                    rs.getString("descripcion"),
                    rs.getDate("fecha_inicio").toLocalDate(),
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                    rs.getString("estado"),
                    entidad,
                    usuario,
                    rs.getString("comentarios")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando tratamientos por estado: " + e.getMessage(), e);
        }
        return tratamientos;
    }

    // MÉTODOS OBSOLETOS - ELIMINADOS
    // Los siguientes métodos ya no son necesarios porque la interfaz fue corregida
}