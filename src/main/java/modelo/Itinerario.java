package modelo;

import java.util.Date;

/**
 * Representa un itinerario turístico creado por un usuario en la aplicación.
 * Contiene detalles como nombre, duración, descripción, fecha de creación, y
 * datos del usuario que lo creó.
 *
 * Esta clase se utiliza tanto para mostrar información en tablas como para
 * registrar y modificar itinerarios.
 *
 * @author k0343
 */
public class Itinerario {

    /**
     * Identificador único del itinerario.
     */
    private int idItinerario;

    /**
     * Nombre del itinerario.
     */
    private String nombre;

    /**
     * Descripción del itinerario.
     */
    private String descripcion;

    /**
     * Duración del itinerario
     */
    private String duracion;

    /**
     * Fecha de creación del itinerario.
     */
    private Date fechaCreacion;

    /**
     * Estado del itinerario (activo/inactivo).
     */
    private Boolean isActive;

    /**
     * Ruta o nombre del archivo de la imagen asociada al itinerario.
     */
    private String foto;

    /**
     * Identificador del usuario que creó el itinerario.
     */
    private int idUsuario;

    /**
     * Nombre del usuario que creó el itinerario.
     */
    private String nombreUsuario;

    public Itinerario() {
    }

    /**
     * Constructor completo con todos los campos disponibles.
     */
    public Itinerario(int idItinerario, String nombre, String descripcion, String duracion, Date fechaCreacion, Boolean isActive, String foto, int idUsuario, String nombreUsuario) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.fechaCreacion = fechaCreacion;
        this.isActive = isActive;
        this.foto = foto;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Constructor básico sin foto ni estado.
     */
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, String duracion) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
    }

    /**
     * Constructor sin nombre de usuario.
     */
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, String duracion, int idUsuario, Boolean isActive, String foto) {
        this.idItinerario = idItinerario;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.idUsuario = idUsuario;
        this.isActive = isActive;
        this.foto = foto;
    }

    /**
     * Constructor sin estado ni imagen.
     */
    public Itinerario(int idItinerario, String nombre, Date fechaCreacion, String descripcion, String duracion, int idUsuario) {
        this(idItinerario, nombre, fechaCreacion, descripcion, duracion, idUsuario, null, null);
    }

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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
