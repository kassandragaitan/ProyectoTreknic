/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasAlojamientos;
import bbdd.ConsultasDestinos;
import bbdd.ConsultasTipoAlojamiento;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Actividad;
import modelo.Alojamiento;
import modelo.Destino;
import modelo.TipoAlojamiento;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class AgregarAlojamientoController implements Initializable {

    @FXML
    private TextField campoNombre;
    @FXML
    private ComboBox<TipoAlojamiento> comboTipo;
    @FXML
    private TextField campoContacto;
    @FXML
    private TextField campoImagen;
    @FXML
    private ComboBox<Destino> comboDestino;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ImageView imagenTrekNic;

    /**
     * Initializes the controller class.
     */
    private Alojamiento alojamientoActual;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Conexion.conectar();
        ConsultasTipoAlojamiento.cargarDatosTiposAlojamientoRegistrar(comboTipo);
        ConsultasDestinos.cargarComboDestino(comboDestino);
        Conexion.cerrarConexion();

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);
    }

   private GestionAlojamientoController gestionAlojamientoController;

// Método para establecer la referencia del controlador principal
public void setGestionAlojamientoController(GestionAlojamientoController controller) {
    this.gestionAlojamientoController = controller;
}

@FXML
private void RegistrarAlojamiento(ActionEvent event) {
    if (compruebaCampo.compruebaVacio(campoNombre)) {
        Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
    } else if (compruebaCampo.compruebaVacio(campoContacto)) {
        Alertas.aviso("Campo vacío", "El contacto no puede estar vacío.");
    } else if (comboDestino.getValue() == null || comboTipo.getValue() == null) {
        Alertas.aviso("Campo vacío", "Debe seleccionar tipo y destino.");
    } else {
        Alojamiento nuevo = new Alojamiento(
                esEdicion ? alojamientoActual.getIdAlojamiento() : 0,
                campoNombre.getText(),
                comboTipo.getValue().getIdTipo(),
                campoContacto.getText(),
                campoImagen.getText(),
                comboDestino.getValue().getId_destino()
        );

        boolean exito = esEdicion
                ? ConsultasAlojamientos.actualizarAlojamiento(nuevo)
                : ConsultasAlojamientos.registrarAlojamiento(nuevo);

        if (exito) {
            Alertas.informacion(esEdicion ? "Alojamiento actualizado correctamente." : "Alojamiento registrado correctamente.");
            recargarTabla();  // Llamada a recargar la tabla después de registrar
            cerrarVentana();
        } else {
            Alertas.error("Error", "No se pudo guardar el alojamiento.");
        }
    }
}

// Método para recargar la tabla de alojamientos
private void recargarTabla() {
    if (gestionAlojamientoController != null) {
        gestionAlojamientoController.cargarAlojamientos();  // Recargar la tabla en el controlador principal
    }
}



   public void verAlojamiento(Alojamiento a) {
    this.alojamientoActual = a;
    campoNombre.setText(a.getNombre());
    campoContacto.setText(a.getContacto());
    campoImagen.setText(a.getImagen());

    // Buscar tipo de alojamiento y seleccionarlo (CORREGIDO)
    for (TipoAlojamiento tipo : comboTipo.getItems()) {
        if (tipo.getIdTipo() == a.getIdTipo()) {
            comboTipo.setValue(tipo);
            break;
        }
    }

    // Buscar destino y seleccionarlo
    for (Destino dest : comboDestino.getItems()) {
        if (dest.getId_destino() == a.getIdDestino()) {
            comboDestino.setValue(dest);
            break;
        }
    }

    botonRegistrar.setText("Actualizar");
    esEdicion = true;
}

    public void setEdicionActiva(boolean editable) {
        campoNombre.setEditable(editable);
        campoContacto.setEditable(editable);
        campoImagen.setEditable(editable);
        comboDestino.setDisable(!editable);
        comboTipo.setDisable(!editable);
        botonRegistrar.setVisible(editable);

        double opacidad = editable ? 1.0 : 0.75;
        campoNombre.setOpacity(opacidad);
        campoContacto.setOpacity(opacidad);
        campoImagen.setOpacity(opacidad);
        comboDestino.setOpacity(opacidad);
        comboTipo.setOpacity(opacidad);
    }

    private void cerrarVentana() {
        Stage stage = (Stage) botonRegistrar.getScene().getWindow();
        stage.close();
    }

 @FXML
private void seleccionarImagen(ActionEvent event) {
    // File chooser for image selection
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));

    // Open file dialog and get the selected file
    File archivo = fileChooser.showOpenDialog(null);

    if (archivo != null) {
        // Generate a new unique filename (UUID or timestamp)
        String nombreArchivoUnico = UUID.randomUUID().toString() + extensionArchivo(archivo);

        // Create a folder for storing images (if it doesn't exist)
        File carpetaImagen = new File("C:/xampp/htdocs/carpetaimg/alojamientos");
        if (!carpetaImagen.exists()) {
            carpetaImagen.mkdirs(); // Create the directory if it doesn't exist
        }

        // Define the target file path
        File objetivoArchivo = new File(carpetaImagen, nombreArchivoUnico);

        try {
            // Copy the selected file to the target location
            Files.copy(archivo.toPath(), objetivoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Update the text field with the relative path
            campoImagen.setText("alojamientos/" + nombreArchivoUnico);  // Store only relative path in the database

        } catch (IOException e) {
            Alertas.error("Error al guardar la imagen", "Hubo un problema al guardar la imagen: " + e.getMessage());
        }
    }
}


// Helper method to get the file extension
    private String extensionArchivo(File file) {
        String nombreArchivo = file.getName();
        int indiceDeExtension = nombreArchivo.lastIndexOf(".");
        if (indiceDeExtension > 0) {
            return nombreArchivo.substring(indiceDeExtension); // e.g. ".png"
        }
        return "";
    }
}
