package controladores;

import bbdd.Conexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import modelo.PreguntaFrecuente;
import modelo.PruebaFuncionalidad;
import modelo.Sugerencia;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.util.Callback;

public class SoporteController implements Initializable {

    @FXML
    private TabPane panelPestanas;

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
    private Button botonNuevaSugerencia;
    private ObservableList<Sugerencia> datosSugerencias = FXCollections.observableArrayList();

    @FXML
    private ListView<PruebaFuncionalidad> listaCentroPruebas;
    @FXML
    private TextField campoBuscarPruebas;
    @FXML
    private Button botonAgregarPrueba;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPreguntas();
        cargarSugerencias();
        cargarCentroPruebas();

        columnaIDSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getId())));
        columnaTituloSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitulo()));
        columnaMensajeSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMensaje()));
        columnaFechaSugerencia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaEnvio()));
    }

    private void cargarPreguntas() {
        listaPreguntas.getItems().clear();
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT pregunta, respuesta FROM preguntas_frecuentes")) {

            while (rs.next()) {
                PreguntaFrecuente pregunta = new PreguntaFrecuente(
                        rs.getString("pregunta"),
                        rs.getString("respuesta")
                );
                listaPreguntas.getItems().add(pregunta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaPreguntas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PreguntaFrecuente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
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

    private void cargarSugerencias() {
        datosSugerencias.clear();
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT id_sugerencia, titulo, mensaje, fecha_envio FROM sugerencias")) {

            while (rs.next()) {
                Sugerencia s = new Sugerencia(
                        rs.getInt("id_sugerencia"),
                        rs.getString("titulo"),
                        rs.getString("mensaje"),
                        rs.getString("fecha_envio").substring(0, 10) // solo la fecha
                );
                datosSugerencias.add(s);
            }
            tablaSugerencias.setItems(datosSugerencias);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirFormularioSugerencia(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nueva Sugerencia");
        dialogo.setHeaderText("Escribe tu sugerencia");
        dialogo.setContentText("Mensaje:");

        TextField tituloField = new TextField();
        tituloField.setPromptText("Título (opcional)");
        dialogo.getDialogPane().setContent(new VBox(tituloField, new Label("Mensaje:"), dialogo.getEditor()));

        dialogo.showAndWait().ifPresent(mensaje -> {
            String titulo = tituloField.getText();
            if (mensaje.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "El mensaje no puede estar vacío.").showAndWait();
                return;
            }

            try (Connection conn = Conexion.conectar()) {
                String sql = "INSERT INTO sugerencias (id_usuario, titulo, mensaje, fecha_envio) VALUES (?, ?, ?, NOW())";
                var stmt = conn.prepareStatement(sql);
                stmt.setInt(1, 1); // ← Cambiar por ID real del usuario
                stmt.setString(2, titulo.isEmpty() ? null : titulo);
                stmt.setString(3, mensaje);
                stmt.executeUpdate();
                cargarSugerencias();
                new Alert(Alert.AlertType.INFORMATION, "¡Sugerencia enviada con éxito!").showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al enviar sugerencia.").showAndWait();
            }
        });
    }

    private void cargarCentroPruebas() {
        ObservableList<PruebaFuncionalidad> pruebas = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT id_funcionalidad, titulo, descripcion FROM funcionalidades_prueba")) {

            while (rs.next()) {
                pruebas.add(new PruebaFuncionalidad(
                        rs.getInt("id_funcionalidad"),
                        rs.getString("titulo"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaCentroPruebas.setItems(pruebas);

        listaCentroPruebas.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PruebaFuncionalidad item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
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
    private void abrirFormularioPrueba(ActionEvent event) {
        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Nueva Funcionalidad de Prueba");
        dialogo.setHeaderText("Describe la funcionalidad en prueba");

        TextField campoTitulo = new TextField();
        campoTitulo.setPromptText("Título");

        TextArea campoDescripcion = new TextArea();
        campoDescripcion.setPromptText("Descripción");
        campoDescripcion.setPrefRowCount(4);

        VBox contenido = new VBox(10, new Label("Título:"), campoTitulo,
                new Label("Descripción:"), campoDescripcion);
        contenido.setStyle("-fx-padding: 10;");
        dialogo.getDialogPane().setContent(contenido);

        ButtonType botonGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(botonGuardar, ButtonType.CANCEL);

        dialogo.showAndWait().ifPresent(tipo -> {
            if (tipo == botonGuardar) {
                String titulo = campoTitulo.getText().trim();
                String descripcion = campoDescripcion.getText().trim();

                if (titulo.isEmpty() || descripcion.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Todos los campos son obligatorios.").showAndWait();
                    return;
                }

                try (Connection conn = Conexion.conectar()) {
                    String sql = "INSERT INTO funcionalidades_prueba (titulo, descripcion) VALUES (?, ?)";
                    var stmt = conn.prepareStatement(sql);
                    stmt.setString(1, titulo);
                    stmt.setString(2, descripcion);
                    stmt.executeUpdate();
                    cargarCentroPruebas();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error al guardar la funcionalidad.").showAndWait();
                }
            }
        });
    }

    // Clase utilitaria para botones en tabla si decides usarla en el futuro
    public static class CeldaBotonTabla<S> extends TableCell<S, Button> {

        private final Button botonAccion = new Button();

        public CeldaBotonTabla(String textoBoton, Consumer<S> accion) {
            botonAccion.setText(textoBoton);
            botonAccion.setOnAction(e -> accion.accept(getTableView().getItems().get(getIndex())));
            setGraphic(botonAccion);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> paraTabla(String textoBoton, Consumer<S> accion) {
            return columna -> new CeldaBotonTabla<>(textoBoton, accion);
        }
    }
}
