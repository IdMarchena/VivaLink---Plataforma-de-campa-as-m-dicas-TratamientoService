package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

import java.time.LocalDate;
import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Tratamiento;
import modelo.Usuario;

/**
 *
 * @author Usuario
 */
public interface TratamientoDao {
    Tratamiento buscarTratamientoPorId(int id);
    boolean verificarSiElTratamientoExiste(int id);
    void guardar(Tratamiento tratamiento); // CORREGIDO: recibe Tratamiento
    List<Tratamiento> buscarTratamientoPorDescripcion(String descripcion); // CORREGIDO: nombre más descriptivo
    List<Tratamiento> listarTodosLosTratamientos(); // CORREGIDO: nombre corregido
    void actualizarTratamiento(int id, Tratamiento tratamiento); // CORREGIDO: sin parámetro tipo
    void eliminarTratamiento(int id);
    List<Tratamiento> buscarTratamientosPorFecha(LocalDate fecha);
    List<Tratamiento> buscarTratamientosPorEstado(String estado);
}
