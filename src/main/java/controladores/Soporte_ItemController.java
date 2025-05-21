package controladores;

import Utilidades.Alertas;
import bbdd.Conexion;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasSoporte;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.PreguntaFrecuente;
import modelo.PruebaFuncionalidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.AnchorPane;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonEliminar.setOnAction(e -> eliminarPregunta());
    }

    public void setRecargarPreguntas(Runnable recargarPreguntas) {
        this.recargarPreguntas = recargarPreguntas;
    }

    public void setItem(Object item) {
        String icono = "/img/documento.png";

        if (item instanceof PreguntaFrecuente) {
            preguntaActual = (PreguntaFrecuente) item;
            etiquetaTitulo.setText(preguntaActual.getPregunta());
            etiquetaDescripcion.setText(preguntaActual.getRespuesta());
        } else if (item instanceof PruebaFuncionalidad) {
            PruebaFuncionalidad funcionalidad = (PruebaFuncionalidad) item;
            etiquetaTitulo.setText(funcionalidad.getTitulo());
            etiquetaDescripcion.setText(funcionalidad.getDescripcion());
        } else {
            etiquetaTitulo.setText("Elemento desconocido");
            etiquetaDescripcion.setText("No se pudo mostrar información.");
        }

        iconoImagen.setImage(new Image(getClass().getResourceAsStream(icono)));
    }

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
