package modelo;

import java.util.Date;

/**
 * Representa un reporte generado dentro del sistema, utilizado para registrar
 * eventos, informes o estadísticas relevantes, junto con la información del
 * usuario que lo emitió.
 *
 * Esta clase puede usarse en módulos de análisis, seguimiento o gestión
 * administrativa.
 *
 * @author k0343
 */
public class Reporte {

    /**
     * Identificador único del reporte.
     */
    private int idReporte;

    /**
     * Tipo de reporte (por ejemplo: "Estadístico", "Errores", "Actividad").
     */
    private String tipo;

    /**
     * Descripción detallada del contenido del reporte.
     */
    private String descripcion;

    /**
     * Fecha en la que se generó el reporte.
     */
    private Date fecha;

    /**
     * Identificador del usuario que generó el reporte.
     */
    private int idUsuario;

    /**
     * Constructor vacío.
     */
    public Reporte() {
    }

    /**
     * Constructor completo con todos los campos.
     *
     * @param idReporte ID único del reporte
     * @param tipo Tipo o categoría del reporte
     * @param descripcion Descripción detallada
     * @param fecha Fecha de creación del reporte
     * @param idUsuario ID del usuario que generó el reporte
     */
    public Reporte(int idReporte, String tipo, String descripcion, Date fecha, int idUsuario) {
        this.idReporte = idReporte;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el ID del reporte.
     *
     * @return ID numérico del reporte
     */
    public int getIdReporte() {
        return idReporte;
    }

    /**
     * Establece el ID del reporte.
     *
     * @param idReporte Nuevo ID
     */
    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    /**
     * Devuelve el tipo de reporte.
     *
     * @return Tipo textual (ej. "Estadístico", "Error", etc.)
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de reporte.
     *
     * @param tipo Tipo del reporte
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve la descripción del reporte.
     *
     * @return Texto descriptivo del contenido del reporte
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece una nueva descripción para el reporte.
     *
     * @param descripcion Contenido del reporte
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha en que se creó el reporte.
     *
     * @return Fecha del reporte
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de creación del reporte.
     *
     * @param fecha Fecha a asignar
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID del usuario que generó el reporte.
     *
     * @return ID de usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario autor del reporte.
     *
     * @param idUsuario ID del usuario que lo emitió
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
