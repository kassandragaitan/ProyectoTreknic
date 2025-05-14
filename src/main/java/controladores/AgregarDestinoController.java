package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import modelo.ConexionFtp;
import modelo.Destino;

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
    private Button botonRegistrar;
    @FXML
    private ComboBox<Categoria> comboCategoria;

    private Categoria categoriaSeleccionada;
    private GestionDestinosController gestionDestinosController;
    private File archivoImagenSeleccionado;
    private boolean edicionActiva = false;
    private Destino destinoEditando;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoRutaArchivo.setEditable(false);

        Image imagen = new Image(getClass().getResourceAsStream("/img/Encabezado.png"));
        imagenTrekNic.setImage(imagen);

        Conexion.conectar();
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        ConsultasCategoria.cargarDatosCategorias(lista);
        comboCategoria.setItems(lista);
        Conexion.cerrarConexion();

        comboCategoria.setOnAction(e -> categoriaSeleccionada = comboCategoria.getSelectionModel().getSelectedItem());
    }

    public void setGestionDestinosController(GestionDestinosController controller) {
        this.gestionDestinosController = controller;
    }

    public void setEdicionActiva(boolean estado) {
        this.edicionActiva = estado;
        botonRegistrar.setText(estado ? "Actualizar" : "Registrar");
    }

    public void cargarDestino(Destino destino) {
        this.destinoEditando = destino;
        campoNombre.setText(destino.getNombre());
        campoDescripcion.setText(destino.getDescripcion());
        campoRutaArchivo.setText(destino.getImagen());

        for (Categoria cat : comboCategoria.getItems()) {
            if (cat.getNombre().equals(destino.getCategoria())) {
                comboCategoria.getSelectionModel().select(cat); 
                categoriaSeleccionada = cat;
                break;
            }
        }
    }

    String nuevoNombre = String.valueOf(System.currentTimeMillis());
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser selector = new FileChooser();
        selector.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File file = selector.showOpenDialog(null);
        if (file != null) {
            archivoImagenSeleccionado = file;
            campoRutaArchivo.setText(file.getAbsolutePath());
        }
    }

      private String subirImagenAlFTP(File localFile) {
        String ext = localFile.getName()
                              .substring(localFile.getName().lastIndexOf('.'));
        String remoteName = System.currentTimeMillis() + ext;

        if (!ConexionFtp.conectar()) {
            System.err.println("No se pudo conectar al FTP");
            return null;
        }
        boolean ok = ConexionFtp.subirArchivo(localFile, remoteName);
        ConexionFtp.desconectar();
        if (!ok) {
            System.err.println("Error subiendo " + localFile + " como " + remoteName);
            return null;
        }
        return remoteName;
    }
      
    @FXML
    private void RegistrarDestino(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.aviso("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.aviso("Campo vacío", "La descripción no puede estar vacía.");
        } else if (categoriaSeleccionada == null) {
            Alertas.aviso("Combo vacío", "Debe seleccionar una categoría.");
        } else if (!edicionActiva && compruebaCampo.compruebaVacio(campoRutaArchivo)) {
            Alertas.aviso("Campo vacío", "Debe seleccionar una imagen.");
        } else {
            Conexion.conectar();
            if (!edicionActiva && ConsultasDestinos.existeDestino(campoNombre.getText())) {
                Alertas.aviso("Duplicado", "Ya existe un destino con ese nombre.");
                Conexion.cerrarConexion();

            } else {
                Conexion.conectar();
                if (!edicionActiva && ConsultasDestinos.existeDestino(campoNombre.getText())) {
                    Alertas.aviso("Duplicado", "Ya existe un destino con ese nombre.");
                    Conexion.cerrarConexion();
                    return;
                }

                String nombreFicheroBD;
        if (edicionActiva) {
            nombreFicheroBD = destinoEditando.getImagen();
        } else {
            String remoto = subirImagenAlFTP(archivoImagenSeleccionado);
            if (remoto == null) {
                Alertas.error("Error FTP", "No se pudo subir la imagen al servidor.");
                return;
            }
            nombreFicheroBD = remoto;
        }

        Conexion.conectar();
        boolean exito;
        if (edicionActiva) {
            exito = ConsultasDestinos.actualizarDestino(
                destinoEditando.getId_destino(),
                campoNombre.getText(),
                campoDescripcion.getText(),
                nombreFicheroBD,
                categoriaSeleccionada.getIdCategoria()
            );
        } else {
            exito = ConsultasDestinos.registrarDestino(
                campoNombre.getText(),
                campoDescripcion.getText(),
                nombreFicheroBD,
                null,
                categoriaSeleccionada.getIdCategoria()
            );
        }
        Conexion.cerrarConexion();

        if (exito) {
            Alertas.informacion(
                edicionActiva ? 
                  "Destino actualizado correctamente." : 
                  "Destino registrado exitosamente."
            );
            if (gestionDestinosController != null) {
                gestionDestinosController.recargarTabla();
            }
            botonRegistrar.getScene().getWindow().hide();
        } else {
            Alertas.error(
                "Error", 
                edicionActiva ? 
                  "No se pudo actualizar el destino." : 
                  "No se pudo registrar el destino."
            );
        }
    }
}
    }}