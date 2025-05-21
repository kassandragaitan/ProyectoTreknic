package modelo;

/**
 * Representa un tipo de alojamiento dentro del sistema turístico. Este modelo
 * se utiliza para clasificar alojamientos como hotel, hostal, cabaña, etc.
 *
 * Incluye el identificador único y el nombre descriptivo del tipo.
 *
 * @author k0343
 */
public class TipoAlojamiento {

    /**
     * Identificador único del tipo de alojamiento.
     */
    private int idTipo;

    /**
     * Nombre o descripción del tipo de alojamiento (ej. Hotel, Hostal, Cabaña).
     */
    private String tipo;

    /**
     * Constructor vacío. Necesario para frameworks y controladores que
     * requieren instanciación por reflexión.
     */
    public TipoAlojamiento() {
    }

    /**
     * Constructor con solo el nombre del tipo de alojamiento.
     *
     * @param tipo Nombre del tipo
     */
    public TipoAlojamiento(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Constructor completo.
     *
     * @param idTipo ID del tipo de alojamiento
     * @param tipo Nombre o descripción del tipo
     */
    public TipoAlojamiento(int idTipo, String tipo) {
        this.idTipo = idTipo;
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID del tipo de alojamiento.
     *
     * @return ID del tipo
     */
    public int getIdTipo() {
        return idTipo;
    }

    /**
     * Establece el ID del tipo de alojamiento.
     *
     * @param idTipo Nuevo ID
     */
    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    /**
     * Obtiene el nombre del tipo de alojamiento.
     *
     * @return Nombre del tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el nombre del tipo de alojamiento.
     *
     * @param tipo Nombre nuevo del tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Representación textual del tipo de alojamiento, usada por defecto en
     * controles como ComboBox.
     *
     * @return Nombre del tipo
     */
    @Override
    public String toString() {
        return tipo;
    }
}
