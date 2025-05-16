package controladores;

import Utilidades.Alertas;
import bbdd.ConsultasDestinos;
import java.io.File;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDestinos();

        botonQuitarFiltro.setDisable(true);

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por nombre",
                "Filtrar por fecha",
                "Filtrar por categoría"
        );
        comboFiltroPor.setItems(filtros);
        comboFiltroPor.getSelectionModel().selectFirst();
        comboValorFiltro.setDisable(true);

        campoBuscarDestino.textProperty().addListener((obs, oldV, newV) -> {
            aplicarBusqueda(newV);
            botonQuitarFiltro.setDisable(
                    newV.trim().isEmpty()
                    && (comboValorFiltro.getValue() == null || comboValorFiltro.getValue().startsWith("Selecciona"))
            );
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
            if ("Filtrar por nombre".equals(newVal)) {
                opciones = ConsultasDestinos.obtenerNombresDestinos();
                placeholderValor = "Selecciona un nombre...";
            } else if ("Filtrar por fecha".equals(newVal)) {
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

    private void cargarDestinos() {
        List<Destino> destinos = ConsultasDestinos.obtenerDestinos();
        mostrarDestinos(destinos);
    }

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

    public void mostrarDestinos(List<Destino> destinos) {
        destinationsPane.getChildren().clear();
        for (Destino destino : destinos) {
            crearTarjetaDestino(destino);
        }
    }

    private void crearTarjetaDestino(Destino destino) {
        VBox tarjetaDestino = new VBox(8);
        tarjetaDestino.getStyleClass().add("destination-card");
        tarjetaDestino.setAlignment(Pos.TOP_CENTER);

        ImageView vistaImagen = new ImageView();
        vistaImagen.setFitWidth(130);
        vistaImagen.setFitHeight(75);
        vistaImagen.setPreserveRatio(true);

        if (destino.getImagen() != null && !destino.getImagen().isBlank()) {
            try {
                ConexionFtp.cargarImagen(destino.getImagen(), vistaImagen);
            } catch (Exception ex) {
                System.err.println("⚠️ Error cargando imagen: " + ex.getMessage());
                vistaImagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
            }
        } else {
            vistaImagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
        }

        Label etiquetaNombre = new Label(destino.getNombre());
        etiquetaNombre.getStyleClass().add("destination-name");

        Label etiquetaDescripcion = new Label(destino.getDescripcion());
        etiquetaDescripcion.getStyleClass().add("destination-desc");
        etiquetaDescripcion.setWrapText(true);

        String fechaFormateada = formatoFecha.format(destino.getFecha_creacion());
        Label etiquetaFecha = new Label("Fecha: " + fechaFormateada);
        etiquetaFecha.getStyleClass().add("destination-date");

        Button botonEditar = new Button("Editar");
        botonEditar.getStyleClass().add("button-edit");
        botonEditar.setOnAction(e -> abrirVentanaEditar(destino));

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.getStyleClass().add("button-delete");
        botonEliminar.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText("¿Estás seguro que quieres borrar este destino?");
            confirm.setContentText("Destino: " + destino.getNombre());

            Optional<ButtonType> resp = confirm.showAndWait();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                boolean ok = ConsultasDestinos.eliminarDestino(destino.getId_destino());
                if (ok) {
                    String nombreImagen = destino.getImagen();
                    if (nombreImagen != null && !nombreImagen.isBlank()) {
                        ConexionFtp.eliminarArchivo(nombreImagen);
                    }
                    Alertas.informacion("Destino eliminados correctamente.");
                    recargarTabla();
                } else {
                    Alertas.error("Error", "No se pudo eliminar el destino.");
                }
            }
        });

        HBox cajaBotones = new HBox(10, botonEditar, botonEliminar);
        cajaBotones.setAlignment(Pos.CENTER);

        tarjetaDestino.getChildren().addAll(
                vistaImagen,
                etiquetaNombre,
                etiquetaDescripcion,
                etiquetaFecha,
                cajaBotones
        );

        destinationsPane.getChildren().add(tarjetaDestino);
    }

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
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir la ventana de edición: " + e.getMessage());
        }
    }

    public void recargarTabla() {
        cargarDestinos();
    }

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
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
