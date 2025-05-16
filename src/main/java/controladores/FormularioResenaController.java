/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.ConsultasMovimientos;
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

    private Destino destinoSeleccionado;
    private Usuario usuarioSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    @FXML
    private void guardarResena(ActionEvent event) {
        if (destinoSeleccionado == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else if (usuarioSeleccionado == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un usuario.");
        } else if (compruebaCampo.compruebaVacio(campoComentario)) {
            Alertas.aviso("Campo vacío", "El comentario no puede estar vacío.");
        } else if (spinnerClasificacion.getValue() < 0 || spinnerClasificacion.getValue() > 5) {
            Alertas.aviso("Clasificación inválida", "La clasificación debe estar entre 0 y 5.");
        } else if (spinnerClasificacion.getValue() == 0) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar clasificación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas dejar la clasificación en 0?");
            confirmacion.setContentText("Una clasificación de 0 indica que no se ha valorado este destino.");

            ButtonType continuar = new ButtonType("Sí, continuar", ButtonBar.ButtonData.YES);
            ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.NO);
            confirmacion.getButtonTypes().setAll(continuar, cancelar);

            if (confirmacion.showAndWait().orElse(cancelar) == continuar) {
                boolean exito = ConsultasResenas.insertarResena(
                        destinoSeleccionado.getId_destino(),
                        usuarioSeleccionado.getIdUsuario(),
                        campoComentario.getText().trim(),
                        0
                );
                if (exito) {
                    ConsultasMovimientos.registrarMovimiento(
                            "Se registró una reseña sin clasificación para el destino: "
                            + destinoSeleccionado.getNombre(),
                            new Date(),
                            usuarioSeleccionado.getIdUsuario()
                    );
                    Alertas.informacion("Reseña registrada correctamente.");
                    limpiarFormulario();
                } else {
                    Alertas.error("Error", "No se pudo guardar la reseña.");
                }
            }
        } else {
            int puntuacion = spinnerClasificacion.getValue();
            boolean exito = ConsultasResenas.insertarResena(
                    destinoSeleccionado.getId_destino(),
                    usuarioSeleccionado.getIdUsuario(),
                    campoComentario.getText().trim(),
                    puntuacion
            );

            if (exito) {
                ConsultasMovimientos.registrarMovimiento(
                        "Se registró una reseña para el destino: "
                        + destinoSeleccionado.getNombre()
                        + " con puntuación " + puntuacion,
                        new Date(),
                        usuarioSeleccionado.getIdUsuario()
                );
                Alertas.informacion("Reseña registrada correctamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error", "No se pudo guardar la reseña.");
            }
        }
    }

    private void limpiarFormulario() {
        comboDestino.getSelectionModel().selectFirst();
        destinoSeleccionado = null;

        comboUsuario.getSelectionModel().selectFirst();
        usuarioSeleccionado = null;

        campoComentario.clear();
        spinnerClasificacion.getValueFactory().setValue(0);
    }
}
