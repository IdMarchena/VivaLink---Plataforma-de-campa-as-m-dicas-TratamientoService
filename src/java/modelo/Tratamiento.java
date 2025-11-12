/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import modelo.EntidadDeSalud;
import modelo.Usuario;

import java.time.LocalDate;

public class Tratamiento {

    private int idTratamiento;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // Este campo puede ser nulo si el tratamiento aún está activo
    private String estado; // Activo, Finalizado, Suspendido, etc.
    private EntidadDeSalud entidadDeSalud; // Relación con la entidad de salud
    private Usuario usuario; // Relación con el usuario (médico o gerente)
    private String comentarios; // Notas o comentarios adicionales

    // Constructor
    public Tratamiento(int idTratamiento, String descripcion, LocalDate fechaInicio, LocalDate fechaFin,
                          String estado, EntidadDeSalud entidadDeSalud, Usuario usuario, String comentarios) {
        this.idTratamiento = idTratamiento;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.entidadDeSalud = entidadDeSalud;
        this.usuario = usuario;
        this.comentarios = comentarios;
    }

    // Getters y Setters
    public int getIdTratamiento() {
        return idTratamiento;
    }

    public void setIdTratamiento(int idTratamiento) {
        this.idTratamiento = idTratamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EntidadDeSalud getEntidadDeSalud() {
        return entidadDeSalud;
    }

    public void setEntidadDeSalud(EntidadDeSalud entidadDeSalud) {
        this.entidadDeSalud = entidadDeSalud;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "TratamientoDto{" +
                "idTratamiento=" + idTratamiento +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado='" + estado + '\'' +
                ", entidadDeSalud=" + entidadDeSalud +
                ", usuario=" + usuario +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}

