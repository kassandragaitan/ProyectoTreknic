package modelo;

import java.util.Date;

/**
 * Representa una notificación generada en el sistema para un usuario. Las
 * notificaciones pueden ser visualizadas y marcadas como leídas. Incluye
 * información sobre la descripción, fecha y estado de lectura.
 *
 * Se utiliza para mostrar alertas o registros de eventos dentro de la
 * plataforma.
 *
 * @author k0343
 */
public class Notificacion {

    /**
     * Identificador único de la notificación.
     */
    private int idNotificacion;

    /**
     * Descripción o mensaje de la notificación.
     */
    private String descripcion;

    /**
     * Fecha en que se generó la notificación.
     */
    private Date fecha;

    /**
     * Nombre del usuario asociado a la notificación.
     */
    private String nombreUsuario;

    /**
     * Indica si la notificación ha sido leída.
     */
    private boolean leido;

    /**
     * Constructor con todos los campos.
     *
     * @param idNotificacion ID de la notificación
     * @param descripcion Mensaje o descripción del evento
     * @param fecha Fecha de creación de la notificación
     * @param nombreUsuario Nombre del usuario al que pertenece
     * @param leido Estado de lectura de la notificación
     */
    public Notificacion(int idNotificacion, String descripcion, Date fecha, String nombreUsuario, boolean leido) {
        this.idNotificacion = idNotificacion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.nombreUsuario = nombreUsuario;
        this.leido = leido;
    }

    /**
     * Obtiene el ID de la notificación.
     *
     * @return ID de la notificación
     */
    public int getIdNotificacion() {
        return idNotificacion;
    }

    /**
     * Establece el ID de la notificación.
     *
     * @param idNotificacion Nuevo ID
     */
    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    /**
     * Devuelve la descripción o mensaje de la notificación.
     *
     * @return Texto de la notificación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece el mensaje de la notificación.
     *
     * @param descripcion Descripción del evento
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la fecha de creación de la notificación.
     *
     * @return Fecha de la notificación
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de la notificación.
     *
     * @param fecha Fecha asignada
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el nombre del usuario asociado.
     *
     * @return Nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre del usuario asociado.
     *
     * @param nombreUsuario Nombre del usuario
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Indica si la notificación ha sido leída.
     *
     * @return true si ha sido leída, false si está pendiente
     */
    public boolean isLeido() {
        return leido;
    }

    /**
     * Marca el estado de lectura de la notificación.
     *
     * @param leido true si se marca como leída, false si no
     */
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}
