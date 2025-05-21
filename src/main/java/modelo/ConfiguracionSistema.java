package modelo;

/**
 * Clase singleton que gestiona la configuración global del sistema,
 * como el color primario y estilos CSS personalizados.
 * 
 * <p>Esta clase permite mantener una configuración centralizada accesible
 * desde cualquier parte de la aplicación.</p>
 * 
 * @author k0343
 */
public class ConfiguracionSistema {

    /** Instancia única de la configuración del sistema. */
    private static ConfiguracionSistema instancia;

    /** Color primario usado en la interfaz (por defecto: azul). */
    private String colorPrimario = "#3498db";

    /** CSS personalizado para aplicar a la interfaz. */
    private String cssPersonalizado = "";

    /**
     * Constructor privado para evitar instanciación externa.
     * Usar {@link #getInstancia()} para obtener la instancia única.
     */
    private ConfiguracionSistema() {
    }

    /**
     * Obtiene la instancia única de configuración del sistema.
     * 
     * @return instancia única de {@code ConfiguracionSistema}.
     */
    public static ConfiguracionSistema getInstancia() {
        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }
        return instancia;
    }

    /**
     * Obtiene el color primario configurado para la interfaz.
     * 
     * @return color primario en formato hexadecimal (por ejemplo, "#3498db").
     */
    public String getColorPrimario() {
        return colorPrimario;
    }

    /**
     * Establece el color primario para la interfaz.
     * 
     * @param colorPrimario color en formato hexadecimal (por ejemplo, "#e74c3c").
     */
    public void setColorPrimario(String colorPrimario) {
        this.colorPrimario = colorPrimario;
    }

    /**
     * Obtiene el contenido CSS personalizado aplicado al sistema.
     * 
     * @return cadena con reglas CSS personalizadas.
     */
    public String getCssPersonalizado() {
        return cssPersonalizado;
    }

    /**
     * Establece el contenido CSS personalizado para aplicar en la interfaz.
     * 
     * @param cssPersonalizado reglas CSS como texto.
     */
    public void setCssPersonalizado(String cssPersonalizado) {
        this.cssPersonalizado = cssPersonalizado;
    }
}
