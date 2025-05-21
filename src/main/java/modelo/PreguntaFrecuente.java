package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa una pregunta frecuente dentro del sistema. Utiliza propiedades
 * JavaFX para permitir enlace directo con interfaces gráficas.
 *
 * Esta clase es útil para cargar preguntas frecuentes en tablas u otros
 * controles de la interfaz mediante bindings.
 *
 * @author k0343
 */
public class PreguntaFrecuente {

    /**
     * Propiedad que representa la pregunta frecuente.
     */
    private final StringProperty pregunta;

    /**
     * Propiedad que representa la respuesta asociada a la pregunta.
     */
    private final StringProperty respuesta;

    /**
     * Constructor que inicializa una pregunta frecuente con su respuesta.
     *
     * @param pregunta Texto de la pregunta
     * @param respuesta Texto de la respuesta asociada
     */
    public PreguntaFrecuente(String pregunta, String respuesta) {
        this.pregunta = new SimpleStringProperty(pregunta);
        this.respuesta = new SimpleStringProperty(respuesta);
    }

    /**
     * Devuelve el texto de la pregunta.
     *
     * @return Pregunta como cadena
     */
    public String getPregunta() {
        return pregunta.get();
    }

    /**
     * Establece el texto de la pregunta.
     *
     * @param pregunta Nueva pregunta
     */
    public void setPregunta(String pregunta) {
        this.pregunta.set(pregunta);
    }

    /**
     * Devuelve la propiedad JavaFX de la pregunta, útil para binding.
     *
     * @return Propiedad de la pregunta
     */
    public StringProperty preguntaProperty() {
        return pregunta;
    }

    /**
     * Devuelve el texto de la respuesta.
     *
     * @return Respuesta como cadena
     */
    public String getRespuesta() {
        return respuesta.get();
    }

    /**
     * Establece el texto de la respuesta.
     *
     * @param respuesta Nueva respuesta
     */
    public void setRespuesta(String respuesta) {
        this.respuesta.set(respuesta);
    }

    /**
     * Devuelve la propiedad JavaFX de la respuesta, útil para binding.
     *
     * @return Propiedad de la respuesta
     */
    public StringProperty respuestaProperty() {
        return respuesta;
    }
}
