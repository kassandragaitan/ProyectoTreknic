
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;

/**
 *
 * @author k0343
 */
public class ItinerarioTabla {
    
  private int idItinerario;
    private String nombre;
    private Date fechaCreacion;
    private String descripcion;
    private int duracion;
    private int idUsuario;
        private Boolean isActive;
    private String foto;

    public ItinerarioTabla(int idItinerario, String nombre, Date fechaCreacion, String descripcion, int duracion, int idUsuario) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.idUsuario = idUsuario;
    }

    public ItinerarioTabla(int idItinerario, String nombre, Date fechaCreacion, String descripcion, int duracion, int idUsuario, Boolean isActive) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.idUsuario = idUsuario;
        this.isActive = isActive;

    }

    // Getters y Setters
    public int getIdItinerario() { return idItinerario; }
    public String getNombre() { return nombre; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public String getDescripcion() { return descripcion; }
    public int getDuracion() { return duracion; }
    public int getIdUsuario() { return idUsuario; }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    
    
}