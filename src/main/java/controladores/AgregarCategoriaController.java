/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import bbdd.ConsultasMovimientos;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Categoria;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarCategoriaController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

    private GestionCategoriaController gestionCategoriaController;  // Referencia al controlador principal

// Método para establecer la referencia del controlador principal
    public void setGestionCategoriaController(GestionCategoriaController controller) {
        this.gestionCategoriaController = controller;
    }
    private int idUsuarioActivo = Usuario.getUsuarioActual().getIdUsuario();

    @FXML
    private void RegistrarCategoria(ActionEvent event) {
        if (campoNombre.getText().trim().isEmpty()) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (campoDescripcion.getText().trim().isEmpty()) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else {

            Categoria categoria = new Categoria(campoNombre.getText(), campoDescripcion.getText());

            if (ConsultasCategoria.registrarCategoria(categoria)) {
                Conexion.conectar();
                ConsultasMovimientos.registrarMovimiento("Ha registrado la categoria " + campoNombre.getText(),
                        Date.valueOf(LocalDate.now()), Usuario.getUsuarioActual().getIdUsuario());
                Alertas.informacion("Categoría registrada exitosamente.");
                campoNombre.clear();
                campoDescripcion.clear();

                recargarTabla();
            } else {
                Alertas.error("Error en el registro", "Ocurrió un error al registrar la categoría.");
            }
        }
    }

    private void recargarTabla() {
        if (gestionCategoriaController != null) {
            gestionCategoriaController.recargarTabla();  // Llamar a recargar la tabla en el controlador principal
        }
    }

}
