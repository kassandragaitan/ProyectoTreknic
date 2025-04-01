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
public class Notificacion {
    private int idNotificacion;
    private String descripcion;
    private Date fecha;
    private String notificacion;
    private int idUsuario;
    private String nombreDestinatario;  // Nuevo campo para el nombre del destinatario

    public Notificacion(int idNotificacion, String descripcion, Date fecha, String notificacion, int idUsuario, String nombreDestinatario) {
        this.idNotificacion = idNotificacion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.notificacion = notificacion;
        this.idUsuario = idUsuario;
        this.nombreDestinatario = nombreDestinatario;
    }

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Getters y Setters...
    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }
}

