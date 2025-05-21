package modelo;

/**
 * Clase que representa un informe resumen de la cantidad de actividades
 * asociadas a un destino turístico.
 *
 * <p>
 * Se utiliza principalmente para generar reportes o estadísticas visuales (como
 * gráficos de barras o circulares) dentro del sistema Treknic.
 * </p>
 *
 * <p>
 * Esta clase se emplea como modelo de datos para cargar información agrupada
 * por destino desde la base de datos.
 * </p>
 *
 * @author k0343
 */
public class InformeActividadDestino {

    /**
     * Nombre del destino turístico.
     */
    private String destino;

    /**
     * Número total de actividades asociadas al destino.
     */
    private int actividades;

    /**
     * Constructor que inicializa el informe con el nombre del destino y la
     * cantidad de actividades registradas.
     *
     * @param destino Nombre del destino.
     * @param actividades Cantidad de actividades asociadas al destino.
     */
    public InformeActividadDestino(String destino, int actividades) {
        this.destino = destino;
        this.actividades = actividades;
    }

    /**
     * Obtiene el nombre del destino.
     *
     * @return Nombre del destino turístico.
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene el número de actividades asociadas al destino.
     *
     * @return Cantidad de actividades.
     */
    public int getActividades() {
        return actividades;
    }
}
