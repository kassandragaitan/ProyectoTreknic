package controladores;

import Utilidades.Alertas;
import Utilidades.compruebaCampo;
import bbdd.Conexion;
import bbdd.ConsultasCategoria;
import bbdd.ConsultasDestinos;
import bbdd.ConsultasNotificaciones;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.Categoria;
import modelo.ConexionFtp;
import modelo.Destino;
import modelo.Usuario;

/**
 * Controlador JavaFX para el formulario de registro y edición de destinos
 * turísticos. Permite ingresar o modificar la información de un destino,
 * incluyendo su nombre, descripción, categoría e imagen. Gestiona la carga de
 * la imagen al servidor FTP y registra notificaciones.
 *
 * Autor: k0343
 */
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

    /**
     * Controlador JavaFX para el formulario de registro y edición de destinos
     * turísticos. Permite ingresar o modificar la información de un destino,
     * incluyendo su nombre, descripción, categoría e imagen. Gestiona la carga
     * de la imagen al servidor FTP y registra notificaciones.
     *
     * Autor: k0343
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        campoRutaArchivo.setEditable(false);

        imagenTrekNic.setImage(new Image(getClass().getResourceAsStream("/img/Encabezado.png")));

        Conexion.conectar();
        ObservableList<Categoria> lista = FXCollections.observableArrayList();
        ConsultasCategoria.cargarDatosCategorias(lista);
        Conexion.cerrarConexion();

        Categoria placeholder = new Categoria(-1, "Seleccione");
        lista.add(0, placeholder);

        comboCategoria.setItems(lista);
        comboCategoria.getSelectionModel().selectFirst();
        categoriaSeleccionada = null;

        comboCategoria.setOnAction(e -> {
            Categoria sel = comboCategoria.getSelectionModel().getSelectedItem();
            if (sel != null && sel.getIdCategoria() != -1) {
                categoriaSeleccionada = sel;
            } else {
                categoriaSeleccionada = null;
            }
        });
    }

    /**
     * Asocia el controlador de gestión de destinos para permitir recargar la
     * tabla al registrar o editar.
     *
     * @param controller Controlador padre de la vista de destinos.
     */
    public void setGestionDestinosController(GestionDestinosController controller) {
        this.gestionDestinosController = controller;
    }

    /**
     * Activa el modo de edición y actualiza el texto del botón a "Actualizar".
     *
     * @param estado true si se está editando un destino, false si se está
     * creando uno nuevo.
     */
    public void setEdicionActiva(boolean estado) {
        this.edicionActiva = estado;
        botonRegistrar.setText(estado ? "Actualizar" : "Registrar");
    }

    /**
     * Carga los datos de un destino existente en los campos del formulario.
     *
     * @param destino Objeto destino que se va a editar.
     */
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

    /**
     * Abre un selector de archivos para que el usuario seleccione una imagen
     * desde el sistema local.
     *
     * @param event Evento del botón "Seleccionar imagen".
     */
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

    /**
     * Sube la imagen seleccionada al servidor FTP y devuelve el nombre remoto
     * generado.
     *
     * @param localFile Archivo local a subir.
     * @return Nombre de archivo en el servidor o null si hubo error.
     */
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

    /**
     * Maneja el proceso de registro o actualización de un destino. Valida los
     * campos, sube la imagen si es necesario, guarda los datos y registra una
     * notificación del movimiento.
     *
     * @param event Evento del botón "Registrar" o "Actualizar".
     */
    @FXML
    private void RegistrarDestino(ActionEvent event) {
        if (compruebaCampo.compruebaVacio(campoNombre)) {
            Alertas.error("Campo vacío", "El nombre no puede estar vacío.");
        } else if (compruebaCampo.compruebaVacio(campoDescripcion)) {
            Alertas.error("Campo vacío", "La descripción no puede estar vacía.");
        } else if (categoriaSeleccionada == null) {
            Alertas.error("Selección inválida", "Debe seleccionar una categoría.");
        } else if (!edicionActiva && compruebaCampo.compruebaVacio(campoRutaArchivo)) {
            Alertas.error("Campo vacío", "Debe seleccionar una imagen.");
        } else {
            Conexion.conectar();
            if (!edicionActiva && ConsultasDestinos.existeDestino(campoNombre.getText().trim())) {
                Alertas.error("Destino duplicado", "Ya existe un destino con ese nombre.");
                campoNombre.clear();
                Conexion.cerrarConexion();
                return;
            }

            String nombreFicheroBD;
            if (!edicionActiva) {
                String remoto = subirImagenAlFTP(archivoImagenSeleccionado);
                if (remoto == null) {
                    Alertas.error("Error FTP", "No se pudo subir la imagen al servidor.");
                    Conexion.cerrarConexion();
                    return;
                }
                nombreFicheroBD = remoto;
            } else {
                if (archivoImagenSeleccionado != null) {
                    String remoto = subirImagenAlFTP(archivoImagenSeleccionado);
                    if (remoto == null) {
                        Alertas.error("Error FTP", "No se pudo subir la nueva imagen al servidor.");
                        Conexion.cerrarConexion();
                        return;
                    }
                    ConexionFtp.conectar();
                    ConexionFtp.eliminarArchivo(destinoEditando.getImagen());
                    ConexionFtp.desconectar();
                    nombreFicheroBD = remoto;
                } else {
                    nombreFicheroBD = destinoEditando.getImagen();
                }
            }

            boolean exito;
            if (!edicionActiva) {
                exito = ConsultasDestinos.registrarDestino(
                        campoNombre.getText().trim(),
                        campoDescripcion.getText().trim(),
                        nombreFicheroBD,
                        null,
                        categoriaSeleccionada.getIdCategoria()
                );
            } else {
                exito = ConsultasDestinos.actualizarDestino(
                        destinoEditando.getId_destino(),
                        campoNombre.getText().trim(),
                        campoDescripcion.getText().trim(),
                        nombreFicheroBD,
                        categoriaSeleccionada.getIdCategoria()
                );
            }

            if (exito) {
                String mensaje = edicionActiva
                        ? "Ha actualizado el destino \"" + campoNombre.getText().trim() + "\""
                        : "Ha registrado el destino \"" + campoNombre.getText().trim() + "\"";

                int idUsuario = Usuario.getUsuarioActual().getIdUsuario();

                ConsultasNotificaciones.registrarMovimiento(
                        mensaje,
                        new java.sql.Date(System.currentTimeMillis()),
                        idUsuario
                );
            }
            Conexion.cerrarConexion();

            if (exito) {
                Alertas.informacion(edicionActiva
                        ? "Destino actualizado correctamente."
                        : "Destino registrado exitosamente."
                );
                if (gestionDestinosController != null) {
                    gestionDestinosController.recargarTabla();
                }
                if (edicionActiva) {
                    ((Stage) botonRegistrar.getScene().getWindow()).close();
                } else {
                    campoNombre.clear();
                    campoDescripcion.clear();
                    campoRutaArchivo.clear();
                    archivoImagenSeleccionado = null;
                    comboCategoria.getSelectionModel().selectFirst();
                    categoriaSeleccionada = null;
                    imagenTrekNic.setImage(new Image(
                            getClass().getResourceAsStream("/img/Encabezado.png")
                    ));
                }
            } else {
                Alertas.error("Error", edicionActiva
                        ? "No se pudo actualizar el destino."
                        : "No se pudo registrar el destino."
                );
            }
        }
    }

}
