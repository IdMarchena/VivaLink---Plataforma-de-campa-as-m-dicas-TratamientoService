/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.service;
import dto.TratamientoDto;
import dto.EntidadDeSaludDto;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Usuario
 */
public interface TratamientoService {
    TratamientoDto buscarTratamientoPorId(int id);
    boolean verificarSiElTratamientoExiste(int id);
    void guardar(EntidadDeSaludDto entidadDeSalud, String tipo);
    List<TratamientoDto> buscarTratamientoPorNombre(String tipo);
    List<TratamientoDto> listarTodosLosTratamientos();
    void actualizarTratamiento(int id, TratamientoDto tratamiento, String tipo);
    void eliminarTratamiento(int id);
    List<TratamientoDto> buscarTratamientosPorFecha(LocalDate fecha);
    List<TratamientoDto> buscarTratamientosPorEstado(String estado);
    
}
