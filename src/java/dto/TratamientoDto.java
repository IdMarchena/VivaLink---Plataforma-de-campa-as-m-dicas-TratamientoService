/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package dto;
import java.time.LocalDate;
/**
 *
 * @author Usuario
 */
public record TratamientoDto(int idTratamiento,
                            String descripcion,
                            LocalDate fechaInicio,
                            LocalDate fechaFin,
                            String estado,
                            EntidadDeSaludDto entidadDeSalud,
                            UsuarioDto usuario,
                            String comentarios) {

}
