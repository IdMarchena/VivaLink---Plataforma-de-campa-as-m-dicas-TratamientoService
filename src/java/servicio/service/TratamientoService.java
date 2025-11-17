/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio.service;
import dto.TratamientoDto;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Usuario
 */
public interface TratamientoService {
    TratamientoDto buscarTratamientoPorId(int id);
    boolean verificarSiElTratamientoExiste(int id);
    void guardar(TratamientoDto tratamiento); // CORREGIDO: recibe Tratamiento
    List<TratamientoDto> buscarTratamientoPorDescripcion(String descripcion); // CORREGIDO: nombre más descriptivo
    List<TratamientoDto> listarTodosLosTratamientos(); // CORREGIDO: nombre corregido
    void actualizarTratamiento(int id, TratamientoDto tratamiento); // CORREGIDO: sin parámetro tipo
    void eliminarTratamiento(int id);
    List<TratamientoDto> buscarTratamientosPorFecha(LocalDate fecha);
    List<TratamientoDto> buscarTratamientosPorEstado(String estado);
    
}
