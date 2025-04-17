package modelo;

import java.util.Date;

public class Itinerario {

    private int idItinerario;
    private String nombre;
    private String descripcion;
    private int duracion;
    private Date fechaCreacion;
    private Boolean isActive;
    private String foto;
    private int idUsuario;

    public Itinerario() {
    }

    // Constructor básico
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, int duracion) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
    }

    // Constructor completo
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, int duracion, int idUsuario, Boolean isActive, String foto) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.idUsuario = idUsuario;
        this.isActive = isActive;
        this.foto = foto;
    }

    // Constructor sin isActive ni foto
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, int duracion, int idUsuario) {
        this(idItinerario, nombre, fechaCreacion, descripcion, duracion, idUsuario, null, null);
    }

    // Getters y Setters (estilo JavaBeans)
    public int getIdItinerario() {
        return idItinerario;
    }

    public void setIdItinerario(int idItinerario) {
        this.idItinerario = idItinerario;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
