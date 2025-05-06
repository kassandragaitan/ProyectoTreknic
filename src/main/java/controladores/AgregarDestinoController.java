package controladores;

import Utilidades.Alertas;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import bbdd.ConsultasDestinos;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import modelo.Categoria;

public class AgregarDestinoController implements Initializable {

    @FXML
    private TextField campoRutaArchivo;
    @FXML
    private Button botonSelccionarImagen;
    @FXML
    private ImageView imagenTrekNic;
    @FXML
    private TextField campoNombre;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private Button botonRegistrar;
    @FXML
    private ComboBox<Categoria> comboCategoria;
    private Categoria categoriaSeleccionada;

    private GestionDestinosController gestionDestinosController;  // Referencia al controlador principal

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);

        Conexion.conectar();
        comboCategoria.getItems().addAll(ConsultasCategoria.obtenerCategorias());
        Conexion.cerrarConexion();

        comboCategoria.setOnAction(e -> {
            categoriaSeleccionada = comboCategoria.getSelectionModel().getSelectedItem();
        });
    }

    public void setGestionDestinosController(GestionDestinosController controller) {
        this.gestionDestinosController = controller;
    }
private File archivoImagenSeleccionado;  // Ruta absoluta de la imagen seleccionada

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        // File chooser for image selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));

        // Open file dialog and get the selected file
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            // Get the name of the file (without the path)
            String nombreArchivo = archivo.getName();

            // Generate a new unique filename (UUID or timestamp)
            String nombreArchivoUnico = UUID.randomUUID().toString() + extensionArchivo(archivo);

            // Create a folder for storing images (if it doesn't exist)
            File carpetaImagen = new File("C:/xampp/htdocs/carpetaimg/destinos");
            if (!carpetaImagen.exists()) {
                carpetaImagen.mkdirs(); // Create the directory if it doesn't exist
            }

            // Define the target file path
            File objetivoArchivo = new File(carpetaImagen, nombreArchivoUnico);

            // Log the selected file path and the destination
            System.out.println("Selected file path: " + archivo.getAbsolutePath());
            System.out.println("Saving to: " + objetivoArchivo.getAbsolutePath());

            try {
                // Copy the selected file to the target location
                Files.copy(archivo.toPath(), objetivoArchivo.toPath(), StandardCopyOption.REPLACE_EXISTING);

            archivoImagenSeleccionado = archivo;  // Guarda el archivo original
campoRutaArchivo.setText("destinos/" + nombreArchivoUnico);  // Solo muestra la ruta relativa


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

    @FXML
    private void RegistrarDestino(ActionEvent event) {
        // Validate that the fields are not empty
        if (campoNombre.getText().isEmpty() || campoDescripcion.getText().isEmpty()
                || campoFecha.getValue() == null || campoRutaArchivo.getText().isEmpty()) {
            Alertas.aviso("Error", "Todos los campos deben estar completos.");
            return;
        }

        // Generate a unique name for the image
        String extension = campoRutaArchivo.getText().substring(campoRutaArchivo.getText().lastIndexOf("."));
        String nombreImagen = System.currentTimeMillis() + extension;

        // Create the folder by category (ensure that categoriaSeleccionada is not null)
        String nombreCategoria = categoriaSeleccionada.getNombre().replaceAll("\\s+", "_"); // Remove spaces

        // Log the directory creation and category name
        File carpetaCategoria = new File("C:/xampp/htdocs/carpetaimg/" + nombreCategoria);
        if (!carpetaCategoria.exists()) {
            carpetaCategoria.mkdirs(); // Create the subfolder if it doesn't exist
            System.out.println("Directory created: " + carpetaCategoria.getAbsolutePath());
        }

        // Define the target file path
        File objetivoArchivo = new File(carpetaCategoria, nombreImagen);
        System.out.println("Target file: " + objetivoArchivo.getAbsolutePath());

        // Copy the image to the subfolder
File origen = archivoImagenSeleccionado;  // <-- usa la ruta original
File destino = new File(carpetaCategoria, nombreImagen);


        try {
            // Log the source and target paths for debugging
            System.out.println("Source file: " + origen.getAbsolutePath());
            System.out.println("Target file: " + destino.getAbsolutePath());

            // Attempt to copy the file
            Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Alertas.error("Error al copiar imagen", "Hubo un problema al copiar la imagen: " + e.getMessage());
            return;
        }

        // Save the data to the database
        Conexion.conectar();
        boolean exito = ConsultasDestinos.registrarDestino(
                campoNombre.getText(),
                campoDescripcion.getText(),
                nombreCategoria + "/" + nombreImagen, // Save the relative path: subfolder/image.jpg
                campoFecha.getValue().toString(),
                categoriaSeleccionada.getIdCategoria()
        );
        Conexion.cerrarConexion();

        // Check if the registration was successful
        if (exito) {
            Alertas.informacion("Destino registrado exitosamente.");
            limpiarFormulario();  // Clear the form
            recargarTabla();  // Reload the table after registering the destination
        } else {
            Alertas.error("Error", "No se pudo registrar el destino.");
        }
    }

    private void recargarTabla() {
        if (gestionDestinosController != null) {
            gestionDestinosController.recargarTabla();  // Reload the table in the main controller
        }
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoDescripcion.clear();
        campoRutaArchivo.clear();
        campoFecha.setValue(null);
        comboCategoria.getSelectionModel().clearSelection();
    }
}
