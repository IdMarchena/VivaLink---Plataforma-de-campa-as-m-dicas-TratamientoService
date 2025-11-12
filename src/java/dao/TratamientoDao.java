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
    void guardar(EntidadDeSalud usuario,String tipo);
    List<Tratamiento> buscarTratamientoPorSuNombre(String tipo);
    List<Tratamiento> listarTodosLosTratamietnos();
    void actualizarTratamiento(int id, Tratamiento usuario, String tipo);
    void eliminarTratamiento(int id);
    List<Tratamiento> buscarTratamientosPorFecha(LocalDate fecha);
    List<Tratamiento> buscarTratamientosPorEstado(String estado);
}
