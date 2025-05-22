package controladores;

import Utilidades.Alertas;
import acciones.CeldaAccionesSugerencia;
import bbdd.ConsultasSoporte;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.PreguntaFrecuente;
import modelo.Sugerencia;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de Soporte, que gestiona dos pestañas: preguntas
 * frecuentes y sugerencias enviadas por los usuarios.
 *
 * <p>
 * Incluye funcionalidades para:</p>
 * <ul>
 * <li>Cargar y filtrar preguntas frecuentes en una ListView.</li>
 * <li>Cargar, filtrar y mostrar sugerencias en una TableView con acciones
 * asociadas.</li>
 * <li>Abrir formularios para agregar nuevas preguntas o sugerencias.</li>
 * </ul>
 */
public class SoporteController implements Initializable {

    @FXML
    private TabPane panelPestanas;
    @FXML
    private Tab tabPreguntas;
    @FXML
    private Tab tabSugerencias;
    @FXML
    private ListView<PreguntaFrecuente> listaPreguntas;
    @FXML
    private TextField campoBuscarPreguntas;
    @FXML
    private TableView<Sugerencia> tablaSugerencias;
    @FXML
    private TableColumn<Sugerencia, String> columnaIDSugerencia;
    @FXML
    private TableColumn<Sugerencia, String> columnaTituloSugerencia;
    @FXML
    private TableColumn<Sugerencia, String> columnaMensajeSugerencia;
    @FXML
    private TableColumn<Sugerencia, String> columnaFechaSugerencia;
    @FXML
    private TextField campoBuscarSugerencias;
    @FXML
    private Button botonAgregarPregunta;
    @FXML
    private Button botonNuevaSugerencia;
    @FXML
    private TableColumn<Sugerencia, Void> columnaAccionesSugerencia;
    private final ObservableList<Sugerencia> datosSugerencias = FXCollections.observableArrayList();
    private Runnable recargarPreguntas;

    /**
     * Inicializa la vista, configurando eventos y cargando los datos para
     * preguntas frecuentes y sugerencias.
     *
     * @param url ubicación del recurso FXML.
     * @param rb bundle de recursos, no utilizado en este contexto.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaIDSugerencia.setVisible(false);
        cargarPreguntas("");
        cargarSugerencias("");

        columnaIDSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getId())));
        columnaTituloSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        columnaMensajeSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMensaje()));
        columnaFechaSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaEnvio()));
        columnaAccionesSugerencia.setCellFactory(col -> new CeldaAccionesSugerencia());

        campoBuscarPreguntas.textProperty().addListener((obs, oldVal, newVal) -> cargarPreguntas(newVal));
        campoBuscarSugerencias.textProperty().addListener((obs, oldVal, newVal) -> cargarSugerencias(newVal));

        panelPestanas.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                String id = newTab.getId();
                if (id == null) {
                    return;
                }

                switch (id) {
                    case "tabPreguntas":
                        campoBuscarPreguntas.clear();
                        cargarPreguntas("");
                        break;
                    case "tabSugerencias":
                        campoBuscarSugerencias.clear();
                        cargarSugerencias("");
                        break;
                }

            }
        });
    }

    /**
     * Establece una acción que puede ser ejecutada para recargar la lista de
     * preguntas frecuentes.
     *
     * Este método permite inyectar una función externa (por ejemplo, desde otro
     * controlador) que actualice dinámicamente la lista de preguntas al
     * modificar o agregar una entrada.
     *
     * @param recargarPreguntas acción Runnable que recarga las preguntas
     * frecuentes
     */
    public void setRecargarPreguntas(Runnable recargarPreguntas) {
        this.recargarPreguntas = recargarPreguntas;
    }

    /**
     * Carga preguntas frecuentes desde la base de datos aplicando un filtro de
     * texto.
     *
     * @param filtro texto de búsqueda para filtrar preguntas por contenido.
     */
    private void cargarPreguntas(String filtro) {
        listaPreguntas.getItems().clear();
        ObservableList<PreguntaFrecuente> preguntas = FXCollections.observableArrayList(
                ConsultasSoporte.obtenerPreguntasFiltradas(filtro)
        );
        listaPreguntas.setItems(preguntas);

        if (preguntas.isEmpty()) {
            Label placeholder = new Label("No hay preguntas frecuentes disponibles.");
            placeholder.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");
            listaPreguntas.setPlaceholder(placeholder);
        }

        listaPreguntas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PreguntaFrecuente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Soporte_Item.fxml"));
                        Node node = loader.load();
                        Soporte_ItemController controller = loader.getController();
                        controller.setItem(item);
                        controller.setRecargarPreguntas(() -> cargarPreguntas(campoBuscarPreguntas.getText()));
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Carga sugerencias desde la base de datos y aplica un filtro por título o
     * mensaje.
     *
     * @param filtro texto de búsqueda aplicado al contenido de la sugerencia.
     */
    private void cargarSugerencias(String filtro) {
        datosSugerencias.clear();
        datosSugerencias.addAll(ConsultasSoporte.obtenerSugerenciasFiltradas(filtro));
        tablaSugerencias.setItems(datosSugerencias);

        if (datosSugerencias.isEmpty()) {
            Label placeholder = new Label("No hay sugerencias disponibles.");
            placeholder.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");
            tablaSugerencias.setPlaceholder(placeholder);
        }
    }

    /**
     * Abre el formulario modal para registrar una nueva sugerencia.
     *
     * @param event evento de acción al presionar el botón correspondiente.
     */
    @FXML
    private void abrirFormularioSugerencia(ActionEvent event) {
        abrirFormulario("/vistas/FormularioSugerencia.fxml", "Nueva Sugerencia");
        campoBuscarSugerencias.clear();
        cargarSugerencias("");
    }

    /**
     * Abre el formulario modal para agregar una nueva pregunta frecuente.
     */
    @FXML
    private void abrirFormularioPregunta() {
        abrirFormulario("/vistas/FormularioPregunta.fxml", "Agregar Pregunta Frecuente");
        campoBuscarPreguntas.clear();
        cargarPreguntas("");
    }

    /**
     * Método genérico para abrir formularios en ventanas modales.
     *
     * @param rutaFXML ruta del archivo FXML que define la vista.
     * @param tituloVentana título de la ventana modal que se mostrará.
     */
    private void abrirFormulario(String rutaFXML, String tituloVentana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir el formulario.");
        }
    }
}
