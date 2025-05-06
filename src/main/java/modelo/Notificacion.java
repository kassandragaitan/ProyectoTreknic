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
    private String nombreDestinatario;
    private String tipoNotificacion;
    private String prioridad;
    private boolean leido;

    public Notificacion(int idNotificacion, String descripcion, Date fecha, String notificacion, String nombreDestinatario, String tipoNotificacion, String prioridad, boolean leido) {
        this.idNotificacion = idNotificacion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.notificacion = notificacion;
        this.nombreDestinatario = nombreDestinatario;
        this.tipoNotificacion = tipoNotificacion;
        this.prioridad = prioridad;
        this.leido = leido;
    }

    public Notificacion(String notificacion, String descripcion, String nombreDestinatario, Date fecha) {
        this.notificacion = notificacion;
        this.descripcion = descripcion;
        this.nombreDestinatario = nombreDestinatario;
        this.fecha = fecha;
    }

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

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

}
