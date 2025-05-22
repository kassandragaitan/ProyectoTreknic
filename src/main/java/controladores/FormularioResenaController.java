/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasNotificaciones;
import bbdd.ConsultasResenas;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import modelo.Destino;
import modelo.Usuario;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador JavaFX para el formulario de registro de reseñas. Permite
 * seleccionar un destino y un usuario, ingresar un comentario y asignar una
 * clasificación entre 0 y 5. También registra la operación como notificación.
 *
 * Autor: k0343
 */
public class FormularioResenaController implements Initializable {

    @FXML
    private ComboBox<Destino> comboDestino;
    @FXML
    private ComboBox<Usuario> comboUsuario;
    @FXML
    private TextArea campoComentario;
    @FXML
    private Spinner<Integer> spinnerClasificacion;
    @FXML
    private Button botonGuardar;
    @FXML
    private ImageView imagenTrekNic;

    private Destino destinoSeleccionado;
    private Usuario usuarioSeleccionado;

    /**
     * Inicializa los componentes del formulario. Carga los destinos y usuarios
     * en sus respectivos ComboBox y configura el spinner de clasificación.
     *
     * @param url URL de inicialización (no utilizada directamente).
     * @param rb ResourceBundle para internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagenTrekNic.setImage(new Image(getClass().getResourceAsStream("/img/Encabezado.png")));

        // Carga de destinos
        ObservableList<Destino> destinos = FXCollections.observableArrayList(
                ConsultasResenas.obtenerDestinos()
        );
        Destino phDest = new Destino();
        phDest.setId_destino(-1);
        phDest.setNombre("Seleccione");
        destinos.add(0, phDest);
        comboDestino.setItems(destinos);
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;

        comboDestino.setOnAction(e -> {
            Destino sel = comboDestino.getSelectionModel().getSelectedItem();
            destinoSeleccionado = (sel != null && sel.getId_destino() != -1) ? sel : null;
        });

        // Carga de usuarios
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(
                ConsultasResenas.obtenerUsuarios()
        );
        Usuario comboUsua = new Usuario();
        comboUsua.setIdUsuario(-1);
        comboUsua.setNombre("Seleccione");
        usuarios.add(0, comboUsua);
        comboUsuario.setItems(usuarios);
        comboUsuario.getSelectionModel().selectFirst();
        usuarioSeleccionado = null;

        comboUsuario.setOnAction(e -> {
            Usuario sel = comboUsuario.getSelectionModel().getSelectedItem();
            usuarioSeleccionado = (sel != null && sel.getIdUsuario() != -1) ? sel : null;
        });

        spinnerClasificacion.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0)
        );
    }

    /**
     * Maneja el evento del botón "Guardar". Valida los campos, muestra
     * confirmación si la clasificación es 0, registra la reseña en la base de
     * datos y guarda la notificación del movimiento.
     *
     * @param event Evento de acción disparado al hacer clic en el botón
     * Guardar.
     */
    @FXML
    private void guardarResena(ActionEvent event) {
        if (destinoSeleccionado == null) {
            Alertas.error("Selección inválida", "Debe seleccionar un destino.");
        } else if (usuarioSeleccionado == null) {
            Alertas.error("Selección inválida", "Debe seleccionar un usuario.");
        } else if (compruebaCampo.compruebaVacio(campoComentario)) {
            Alertas.error("Campo vacío", "El comentario no puede estar vacío.");
        } else if (spinnerClasificacion.getValue() < 0 || spinnerClasificacion.getValue() > 5) {
            Alertas.error("Clasificación inválida", "La clasificación debe estar entre 0 y 5.");
        } else if (spinnerClasificacion.getValue() == 0) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar clasificación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas dejar la clasificación en 0?");
            confirmacion.setContentText("Una clasificación de 0 indica que no se ha valorado este destino.");

            ButtonType continuar = new ButtonType("Sí, continuar", ButtonBar.ButtonData.YES);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.NO);
            confirmacion.getButtonTypes().setAll(continuar, cancelar);

            if (confirmacion.showAndWait().orElse(cancelar) == continuar) {
                registrarResena(0);
            }
        } else {
            registrarResena(spinnerClasificacion.getValue());
        }
    }

    /**
     * Registra la reseña con la puntuación especificada, muestra mensaje de
     * confirmación y registra el movimiento como notificación.
     *
     * @param puntuacion Valor de la clasificación entre 0 y 5.
     */
    private void registrarResena(int puntuacion) {
        boolean exito = ConsultasResenas.insertarResena(
                destinoSeleccionado.getId_destino(),
                usuarioSeleccionado.getIdUsuario(),
                campoComentario.getText().trim(),
                puntuacion
        );

        if (exito) {
            String mensaje = (puntuacion == 0)
                    ? "Se registró una reseña sin clasificación para el destino: " + destinoSeleccionado.getNombre()
                    : "Se registró una reseña para el destino: " + destinoSeleccionado.getNombre() + " con puntuación " + puntuacion;

            int idUsuario = usuarioSeleccionado.getIdUsuario();
            ConsultasNotificaciones.registrarMovimiento(mensaje, new Date(), idUsuario);

            Alertas.informacion("Reseña registrada correctamente.");
            limpiarFormulario();
        } else {
            Alertas.error("Error", "No se pudo guardar la reseña.");
        }
    }

    /**
     * Limpia todos los campos del formulario y restablece los valores por
     * defecto.
     */
    private void limpiarFormulario() {
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;

        comboUsuario.getSelectionModel().selectFirst();
        usuarioSeleccionado = null;

        campoComentario.clear();
        spinnerClasificacion.getValueFactory().setValue(0);
    }
}
