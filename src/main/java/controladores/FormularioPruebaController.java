package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasMovimientos;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;

public class FormularioPruebaController implements Initializable {

    @FXML
    private TextField campoTitulo;

    @FXML
    private TextArea campoDescripcion;

    @FXML
    private Button btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si fuese necesaria
    }

    @FXML
    private void guardarPrueba(ActionEvent event) {
        String titulo = campoTitulo.getText().trim();
        String descripcion = campoDescripcion.getText().trim();

        if (compruebaCampo.compruebaVacio(campoTitulo)) {
            Alertas.aviso("Campo vacío", "El título no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else {
            try (Connection conn = Conexion.conectar();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO funcionalidades_prueba (titulo, descripcion) VALUES (?, ?)")
            ) {
                stmt.setString(1, titulo);
                stmt.setString(2, descripcion);
                stmt.executeUpdate();

                ConsultasMovimientos.registrarMovimiento(
                        "Ha registrado una funcionalidad de prueba: " + titulo,
                        new Date(),
                        Usuario.getUsuarioActual().getIdUsuario()
                );

                Alertas.informacion("Funcionalidad guardada correctamente.");
                limpiarFormulario();
                cerrarVentana();

            } catch (Exception e) {
                e.printStackTrace();
                Alertas.error("Error", "No se pudo guardar la funcionalidad.");
            }
        }
    }

    private void limpiarFormulario() {
        campoTitulo.clear();
        campoDescripcion.clear();
        btnGuardar.setText("Guardar");
    }

    private void cerrarVentana() {
        Stage stage = (Stage) campoTitulo.getScene().getWindow();
        stage.close();
    }
}
