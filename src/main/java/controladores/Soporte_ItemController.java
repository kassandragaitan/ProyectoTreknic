package controladores;

import Utilidades.Alertas;
import bbdd.Conexion;
import bbdd.ConsultasSoporte;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.PreguntaFrecuente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador para el ítem visual de una pregunta frecuente en la interfaz de
 * soporte. Se encarga de mostrar la información de una pregunta, permitir su
 * eliminación, y actualizar la vista si se realiza alguna acción.
 *
 * Este controlador se utiliza en listas o paneles donde se muestran múltiples
 * preguntas frecuentes, generalmente como parte de un sistema de ayuda o
 * soporte dentro de la aplicación.
 *
 * @author k0343
 */
public class Soporte_ItemController implements Initializable {

    @FXML
    private ImageView iconoImagen;
    @FXML
    private Label etiquetaTitulo;
    @FXML
    private Label etiquetaDescripcion;
    @FXML
    private Button botonEliminar;

    private PreguntaFrecuente preguntaActual;
    private Runnable recargarPreguntas;
    @FXML
    private AnchorPane itemContainer;

    /**
     * Inicializa el controlador. Asigna la acción del botón de eliminación.
     *
     * @param url ubicación del archivo FXML (no utilizado)
     * @param rb recursos de internacionalización (no utilizado)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonEliminar.setOnAction(e -> eliminarPregunta());
    }

    /**
     * Establece la acción que se ejecutará después de eliminar una pregunta,
     * como por ejemplo recargar la lista de ítems.
     *
     * @param recargarPreguntas Runnable con la lógica de recarga
     */
    public void setRecargarPreguntas(Runnable recargarPreguntas) {
        this.recargarPreguntas = recargarPreguntas;
    }

    /**
     * Asigna la pregunta frecuente que se mostrará en el ítem visual. También
     * actualiza el contenido de las etiquetas y el icono.
     *
     * @param pregunta objeto de tipo PreguntaFrecuente a mostrar
     */
    public void setItem(PreguntaFrecuente pregunta) {
        this.preguntaActual = pregunta;
        etiquetaTitulo.setText(pregunta.getPregunta());
        etiquetaDescripcion.setText(pregunta.getRespuesta());

        String icono = "/img/documento.png";
        iconoImagen.setImage(new Image(getClass().getResourceAsStream(icono)));
    }

    /**
     * Elimina la pregunta actual tras una confirmación del usuario. Si se
     * elimina correctamente, se registra la acción en el sistema y se actualiza
     * la interfaz mediante la función de recarga.
     */
    @FXML
    private void eliminarPregunta() {
        if (preguntaActual != null) {
            boolean confirmacion = Alertas.confirmacionSoporte("¿Eliminar?", "¿Estás seguro de que deseas eliminar esta pregunta?");
            if (confirmacion) {
                boolean eliminada = ConsultasSoporte.eliminarPregunta(preguntaActual.getPregunta());
                if (eliminada) {
                    Conexion.conectar();
                    String mensaje = "Ha eliminado la pregunta frecuente: " + preguntaActual.getPregunta();
                    int idUsuario = modelo.Usuario.getUsuarioActual().getIdUsuario();

                    bbdd.ConsultasNotificaciones.registrarMovimiento(
                            mensaje,
                            new java.util.Date(),
                            idUsuario
                    );
                    Conexion.cerrarConexion();

                    Alertas.informacion("La pregunta ha sido eliminada correctamente.");

                    if (recargarPreguntas != null) {
                        recargarPreguntas.run();
                    }
                } else {
                    Alertas.error("Error", "No se pudo eliminar la pregunta.");
                }
            }
        }
    }

}
