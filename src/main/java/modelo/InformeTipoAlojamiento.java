package modelo;

/**
 * Clase que representa un informe estadístico de los tipos de alojamiento y su
 * frecuencia de aparición dentro del sistema.
 *
 * <p>
 * Se utiliza comúnmente para generar gráficos o reportes relacionados con la
 * cantidad de alojamientos agrupados por tipo (por ejemplo, hoteles, hostales,
 * cabañas, etc.) en la aplicación Treknic.
 * </p>
 *
 * @author k0343
 */
public class InformeTipoAlojamiento {

    /**
     * Tipo de alojamiento
     */
    private String tipo;

    /**
     * Número total de alojamientos registrados de este tipo.
     */
    private int cantidad;

    /**
     * Constructor que inicializa el tipo de alojamiento y su cantidad.
     *
     * @param tipo Tipo de alojamiento.
     * @param cantidad Cantidad de alojamientos registrados de ese tipo.
     */
    public InformeTipoAlojamiento(String tipo, int cantidad) {
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el tipo de alojamiento.
     *
     * @return Nombre del tipo de alojamiento.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene la cantidad de alojamientos registrados de este tipo.
     *
     * @return Número de alojamientos.
     */
    public int getCantidad() {
        return cantidad;
    }
}
