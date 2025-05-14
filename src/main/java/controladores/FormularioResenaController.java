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
import javafx.stage.Stage;
import modelo.Destino;
import modelo.Usuario;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import modelo.Resena;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboDestino.setItems(FXCollections.observableArrayList(ConsultasResenas.obtenerDestinos()));
        comboUsuario.setItems(FXCollections.observableArrayList(ConsultasResenas.obtenerUsuarios()));

        spinnerClasificacion.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0)
        );

    }

    @FXML
    private void guardarResena(ActionEvent event) {
        if (comboDestino.getValue() == null) {
            Alertas.aviso("Campo vacío", "Debe seleccionar un destino.");
        } else if (comboUsuario.getValue() == null) {
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
                Resena nueva = new Resena(
                        0,
                        comboDestino.getValue().getNombre(),
                        campoComentario.getText().trim(),
                        0,
                        comboUsuario.getValue().getNombre()
                );

                boolean exito = ConsultasResenas.insertarResena(
                        comboDestino.getValue().getId_destino(),
                        comboUsuario.getValue().getIdUsuario(),
                        campoComentario.getText().trim(),
                        0
                );

                if (exito) {
                    ConsultasMovimientos.registrarMovimiento(
                            "Se registró una reseña sin clasificación para el destino: " + comboDestino.getValue().getNombre(),
                            new Date(),
                            comboUsuario.getValue().getIdUsuario()
                    );
                    Alertas.informacion("Reseña registrada correctamente.");
                    limpiarFormulario();
                } else {
                    Alertas.error("Error", "No se pudo guardar la reseña.");
                }
            }
        } else {
            Resena nueva = new Resena(
                    0,
                    comboDestino.getValue().getNombre(),
                    campoComentario.getText().trim(),
                    spinnerClasificacion.getValue(),
                    comboUsuario.getValue().getNombre()
            );

            boolean exito = ConsultasResenas.insertarResena(
                    comboDestino.getValue().getId_destino(),
                    comboUsuario.getValue().getIdUsuario(),
                    campoComentario.getText().trim(),
                    spinnerClasificacion.getValue()
            );

            if (exito) {
                ConsultasMovimientos.registrarMovimiento(
                        "Se registró una reseña para el destino: " + comboDestino.getValue().getNombre()
                        + " con puntuación " + spinnerClasificacion.getValue(),
                        new Date(),
                        comboUsuario.getValue().getIdUsuario()
                );
                Alertas.informacion("Reseña registrada correctamente.");
                limpiarFormulario();
            } else {
                Alertas.error("Error", "No se pudo guardar la reseña.");
            }
        }
    }

    private void limpiarFormulario() {
        comboDestino.getSelectionModel().clearSelection();
        comboUsuario.getSelectionModel().clearSelection();
        campoComentario.clear();
        spinnerClasificacion.getValueFactory().setValue(0);
    }

}
