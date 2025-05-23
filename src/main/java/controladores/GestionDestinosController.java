package controladores;

import Utilidades.Alertas;
import bbdd.ConsultasDestinos;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.ConexionFtp;
import modelo.Destino;

/**
 * Controlador JavaFX para la gestión visual de destinos turísticos. Permite
 * registrar, visualizar, editar, eliminar y filtrar destinos cargados desde la
 * base de datos. Incluye búsqueda en tiempo real y filtrado por fecha o
 * categoría.
 *
 * Autor: k0343
 */
public class GestionDestinosController implements Initializable {

    @FXML
    private TextField campoBuscarDestino;
    @FXML
    private Button botonNuevoDestino;
    @FXML
    private TilePane destinationsPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private Button botonQuitarFiltro;

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Inicializa la interfaz cargando los destinos, configurando la lógica de
     * filtrado y búsqueda.
     *
     * @param url URL de inicialización.
     * @param rb ResourceBundle de recursos (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDestinos();

        botonQuitarFiltro.setDisable(true);

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por fecha",
                "Filtrar por categoría"
        );

        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();
        comboValorFiltro.setDisable(true);

        campoBuscarDestino.textProperty().addListener((obs, oldV, newV) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            aplicarBusqueda(newV);
            botonQuitarFiltro.setDisable(newV.trim().isEmpty());
        });

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null || newVal.equals("Selecciona un tipo de filtro...")) {
                botonQuitarFiltro.setDisable(campoBuscarDestino.getText().trim().isEmpty());
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            String placeholderValor = "";
            if ("Filtrar por fecha".equals(newVal)) {
                opciones = ConsultasDestinos.obtenerFechasDestinos();
                placeholderValor = "Selecciona una fecha...";
            } else if ("Filtrar por categoría".equals(newVal)) {
                opciones = ConsultasDestinos.obtenerCategorias();
                placeholderValor = "Selecciona una categoría...";
            }

            if (!opciones.contains(placeholderValor)) {
                opciones.add(0, placeholderValor);
            }
            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);

            botonQuitarFiltro.setDisable(
                    campoBuscarDestino.getText().trim().isEmpty()
                    && (comboValorFiltro.getValue() == null || comboValorFiltro.getValue().startsWith("Selecciona"))
            );
        });

        comboValorFiltro.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.startsWith("Selecciona")) {
                aplicarBusqueda(campoBuscarDestino.getText());
                botonQuitarFiltro.setDisable(campoBuscarDestino.getText().trim().isEmpty());
                return;
            }

            botonQuitarFiltro.setDisable(false);
            List<Destino> filtrados = ConsultasDestinos.filtrarDestinos(
                    comboFiltroPor.getValue(), newVal
            );
            mostrarDestinos(filtrados);
        });

        botonQuitarFiltro.setOnAction(e -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.setDisable(true);
            campoBuscarDestino.clear();
            cargarDestinos();
            botonQuitarFiltro.setDisable(true);
        });
    }

    /**
     * Carga todos los destinos disponibles desde la base de datos.
     */
    private void cargarDestinos() {
        List<Destino> destinos = ConsultasDestinos.obtenerDestinos();
        mostrarDestinos(destinos);
    }

    /**
     * Aplica búsqueda por nombre a la lista de destinos.
     *
     * @param texto Texto a buscar.
     */
    private void aplicarBusqueda(String texto) {
        if (texto == null || texto.isEmpty()) {
            cargarDestinos();
            return;
        }
        List<Destino> destinos = ConsultasDestinos.obtenerDestinos();
        destinos.removeIf(d
                -> !d.getNombre().toLowerCase().contains(texto.toLowerCase())
        );
        mostrarDestinos(destinos);
    }

    /**
     * Muestra gráficamente los destinos en el contenedor `TilePane`.
     *
     * @param destinos Lista de destinos a mostrar.
     */
    public void mostrarDestinos(List<Destino> destinos) {
        destinationsPane.getChildren().clear();

        if (destinos == null || destinos.isEmpty()) {
            Label mensaje = new Label("No hay destinos registrados.");
            mensaje.setStyle("-fx-text-fill: #888888; -fx-font-size: 15px;");
            mensaje.setWrapText(true);
            mensaje.setMaxWidth(400);
            mensaje.setAlignment(Pos.CENTER);

            destinationsPane.setAlignment(Pos.CENTER);
            destinationsPane.setPrefHeight(300);
            destinationsPane.getChildren().add(mensaje);
            return;
        }

        destinationsPane.setAlignment(Pos.TOP_LEFT);
        for (Destino destino : destinos) {
            crearTarjetaDestino(destino);
        }
    }

    /**
     * Crea y agrega una tarjeta visual para un destino, incluyendo imagen,
     * nombre, descripción y botones de edición y eliminación.
     *
     * @param destino El destino que se representará.
     */
    private void crearTarjetaDestino(Destino destino) {
        VBox tarjetaDestino = new VBox(10);
        tarjetaDestino.getStyleClass().add("destination-card");
        tarjetaDestino.setAlignment(Pos.TOP_CENTER);

        ImageView vistaImagen = new ImageView();
        vistaImagen.setFitWidth(240);
        vistaImagen.setFitHeight(135);
        vistaImagen.setPreserveRatio(false);
        vistaImagen.setSmooth(true);
        vistaImagen.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");

        try {
            ConexionFtp.cargarImagen(destino.getImagen(), vistaImagen);
        } catch (Exception ex) {
            vistaImagen.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/no-image.png")));
        }

        Label etiquetaNombre = new Label(destino.getNombre());
        etiquetaNombre.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label etiquetaDescripcion = new Label(destino.getDescripcion());
        etiquetaDescripcion.setWrapText(true);
        etiquetaDescripcion.setMaxWidth(220);
        etiquetaDescripcion.setStyle("-fx-text-fill: #555; -fx-font-size: 13px;");

        Button botonEditar = new Button("Editar");
        botonEditar.setOnAction(e -> abrirVentanaEditar(destino));
        botonEditar.setStyle("-fx-background-color: #3874b4; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 6 16;");

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setOnAction(e -> eliminarDestino(destino));
        botonEliminar.setStyle("-fx-background-color: #e57373; -fx-text-fill: white; -fx-background-radius: 18; -fx-padding: 6 16;");

        HBox botones = new HBox(10, botonEditar, botonEliminar);
        botones.setAlignment(Pos.CENTER);

        tarjetaDestino.getChildren().addAll(vistaImagen, etiquetaNombre, etiquetaDescripcion, botones);
        destinationsPane.getChildren().add(tarjetaDestino);
    }

    /**
     * Abre el formulario de edición con los datos del destino seleccionado.
     *
     * @param destino Destino a editar.
     */
    private void abrirVentanaEditar(Destino destino) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarDestino.fxml"));
            Parent root = loader.load();

            AgregarDestinoController controller = loader.getController();
            controller.setGestionDestinosController(this);
            controller.setEdicionActiva(true);
            controller.cargarDestino(destino);

            Stage stage = new Stage();
            stage.setTitle("Editar Destino");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir la ventana de edición: " + e.getMessage());
        }
    }

    /**
     * Elimina el destino seleccionado y su imagen del servidor FTP, previa
     * confirmación.
     *
     * @param destino Destino a eliminar.
     */
    private void eliminarDestino(Destino destino) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar destino " + destino.getNombre() + "?");

        Optional<ButtonType> resp = confirm.showAndWait();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            boolean ok = ConsultasDestinos.eliminarDestino(destino.getId_destino());
            if (ok) {
                if (destino.getImagen() != null && !destino.getImagen().isBlank()) {
                    ConexionFtp.eliminarArchivo(destino.getImagen());
                }
                Alertas.informacion("Destino eliminado correctamente.");
                recargarTabla();
            } else {
                Alertas.error("Error", "No se pudo eliminar el destino.");
            }
        }
    }

    /**
     * Recarga la tabla/galería de destinos.
     */
    public void recargarTabla() {
        cargarDestinos();
    }

    /**
     * Abre la ventana para registrar un nuevo destino.
     *
     * @param event Evento generado al hacer clic en el botón "Nuevo destino".
     */
    @FXML
    private void irANuevoDestino(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarDestino.fxml"));
            Parent root = loader.load();

            AgregarDestinoController controlador = loader.getController();
            controlador.setGestionDestinosController(this);
            controlador.setEdicionActiva(false);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setTitle("Agregar Destino");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
