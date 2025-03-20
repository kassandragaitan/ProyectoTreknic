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
public class Itinerario {

    private int id_itinerario;
    private String nombre;
    private String descripcion;
    private int duracion;
    private Date fecha_creacion;
    private Boolean isActive;
    private String foto;

    public Itinerario() {
    }

    public Itinerario(int id_itinerario, String nombre, String descripcion, int duracion, Date fecha_creacion) {
        this.id_itinerario = id_itinerario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.fecha_creacion = fecha_creacion;
    }

    public Itinerario(int id_itinerario, String nombre, String descripcion, int duracion, Date fecha_creacion, Boolean isActive) {
        this.id_itinerario = id_itinerario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.fecha_creacion = fecha_creacion;
        this.isActive = isActive;
    }

    public int getId_itinerario() {
        return id_itinerario;
    }

    public void setId_itinerario(int id_itinerario) {
        this.id_itinerario = id_itinerario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
