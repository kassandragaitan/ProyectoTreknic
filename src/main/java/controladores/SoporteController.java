package controladores;

import Utilidades.Alertas;
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
import modelo.PruebaFuncionalidad;
import modelo.Sugerencia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SoporteController implements Initializable {

    @FXML
    private TabPane panelPestanas;
    @FXML
    private Tab tabPreguntas;
    @FXML
    private Tab tabSugerencias;
    @FXML
    private Tab tabPruebas;
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
    private ListView<PruebaFuncionalidad> listaCentroPruebas;
    @FXML
    private TextField campoBuscarPruebas;
    @FXML
    private Button botonAgregarPregunta;
    @FXML
    private Button botonNuevaSugerencia;
    @FXML
    private Button botonAgregarPrueba;

    private final ObservableList<Sugerencia> datosSugerencias = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPreguntas("");
        cargarSugerencias("");
        cargarCentroPruebas("");

        columnaIDSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getId())));
        columnaTituloSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        columnaMensajeSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMensaje()));
        columnaFechaSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaEnvio()));

        campoBuscarPreguntas.textProperty().addListener((obs, oldVal, newVal) -> cargarPreguntas(newVal));
        campoBuscarSugerencias.textProperty().addListener((obs, oldVal, newVal) -> cargarSugerencias(newVal));
        campoBuscarPruebas.textProperty().addListener((obs, oldVal, newVal) -> cargarCentroPruebas(newVal));

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
                    case "tabPruebas":
                        campoBuscarPruebas.clear();
                        cargarCentroPruebas("");
                        break;
                }

            }
        });
    }

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
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

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

    private void cargarCentroPruebas(String filtro) {
        ObservableList<PruebaFuncionalidad> pruebas = FXCollections.observableArrayList(
                ConsultasSoporte.obtenerPruebasFiltradas(filtro)
        );
        listaCentroPruebas.setItems(pruebas);

        if (pruebas.isEmpty()) {
            Label placeholder = new Label("No hay funcionalidades de prueba disponibles.");
            placeholder.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");
            listaCentroPruebas.setPlaceholder(placeholder);
        }

        listaCentroPruebas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PruebaFuncionalidad item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Soporte_Item.fxml"));
                        Node node = loader.load();
                        Soporte_ItemController controller = loader.getController();
                        controller.setItem(item);
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void abrirFormularioSugerencia(ActionEvent event) {
        abrirFormulario("/vistas/FormularioSugerencia.fxml", "Nueva Sugerencia");
        campoBuscarSugerencias.clear();
        cargarSugerencias("");
    }

    @FXML
    private void abrirFormularioPrueba(ActionEvent event) {
        abrirFormulario("/vistas/FormularioPrueba.fxml", "Nueva Funcionalidad de Prueba");
        campoBuscarPruebas.clear();
        cargarCentroPruebas("");
    }

    @FXML
    private void abrirFormularioPregunta() {
        abrirFormulario("/vistas/FormularioPregunta.fxml", "Agregar Pregunta Frecuente");
        campoBuscarPreguntas.clear();
        cargarPreguntas("");
    }

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
