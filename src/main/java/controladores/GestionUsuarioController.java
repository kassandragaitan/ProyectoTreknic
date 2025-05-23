/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import bbdd.ConsultasUsuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import acciones.CeldaAccionesUsuario;
import java.io.IOException;
import modelo.Usuario;
import java.text.SimpleDateFormat;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * Controlador de la vista de gestión de usuarios dentro del panel principal.
 * Permite registrar, filtrar, buscar, editar y eliminar usuarios. Incluye
 * opciones de filtro por rol, idioma y tipo de compañía.
 *
 * Componentes de UI: - Tabla de usuarios con columnas personalizadas (nombre,
 * email, rol, etc.) - Campo de búsqueda con actualización en tiempo real -
 * Combos de filtro por atributo - Botón para añadir un nuevo usuario
 *
 * Estilo visual personalizado según tipo de usuario y avatar textual.
 */
public class GestionUsuarioController implements Initializable {

    @FXML
    private TableColumn<Usuario, String> columnUsuario;
    @FXML
    private TableColumn<Usuario, String> columnCorreo;
    @FXML
    private TableColumn<Usuario, String> columnRol;
    @FXML
    private TableColumn<Usuario, Integer> columnTelefono;
    @FXML
    private TableColumn<Usuario, Date> columnFechaRegistro;
    @FXML
    private TableColumn<Usuario, Void> columnAcciones;
    @FXML
    private Button botonNuevoUsuario;
    @FXML
    private TableColumn<Usuario, String> columnIdioma;
    @FXML
    private TableColumn<Usuario, String> columnViajero;
    @FXML
    private TableColumn<Usuario, String> columnContrasena;
    @FXML
    private TextField campoBuscarUsuario;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private ComboBox<String> comboFiltroPor;
    @FXML
    private ComboBox<String> comboValorFiltro;
    @FXML
    private Button botonQuitarFiltro;

    /**
     * Método que se ejecuta automáticamente al inicializar el controlador de la
     * vista de gestión de usuarios.
     * <p>
     * Realiza las siguientes acciones:
     * <ul>
     * <li>Recarga los datos de la tabla de usuarios desde la base de
     * datos.</li>
     * <li>Asocia cada columna de la tabla con las propiedades correspondientes
     * del modelo {@code Usuario}.</li>
     * <li>Oculta las columnas de correo y contraseña por motivos de
     * privacidad.</li>
     * <li>Establece celdas personalizadas para mostrar el nombre del usuario
     * con formato enriquecido, así como estilos visuales para roles e
     * iconos.</li>
     * <li>Configura el buscador en tiempo real, que filtra usuarios al escribir
     * en el campo de búsqueda.</li>
     * <li>Inicializa los ComboBox de filtros con opciones para filtrar por rol,
     * idioma o tipo de compañía.</li>
     * <li>Gestiona dinámicamente los valores disponibles en los ComboBox de
     * filtro en función del filtro seleccionado.</li>
     * <li>Actualiza la tabla según los valores seleccionados en los filtros o
     * el buscador.</li>
     * <li>Ofrece un botón para quitar los filtros y restaurar la lista completa
     * de usuarios.</li>
     * </ul>
     *
     * @param url No utilizado. Parte del ciclo de vida de JavaFX.
     * @param rb No utilizado. Representa recursos internacionalizados si se
     * usan.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recargarTabla();
        columnCorreo.setVisible(false);
        columnContrasena.setVisible(false);
        columnUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        columnRol.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        columnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));
        columnViajero.setCellValueFactory(new PropertyValueFactory<>("tipoViajero"));
        columnFechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));

        columnFechaRegistro.setStyle("-fx-alignment: CENTER;");
        columnAcciones.setStyle("-fx-alignment: CENTER;");

        tablaUsuarios.setItems(generarDatosUsuario());
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUsuarios.toFront();
        initializeCustomCells();

        botonQuitarFiltro.setDisable(true);

        campoBuscarUsuario.textProperty().addListener((observable, oldValue, newValue) -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            buscarUsuariosEnTiempoReal(newValue);

            botonQuitarFiltro.setDisable(newValue.trim().isEmpty());
        });

        ObservableList<String> filtros = FXCollections.observableArrayList(
                "Selecciona un tipo de filtro...",
                "Filtrar por rol",
                "Filtrar por idioma",
                "Filtrar por tipo de compañía"
        );
        comboFiltroPor.setItems(filtros);

        comboFiltroPor.getSelectionModel().selectFirst();
        comboValorFiltro.setDisable(true);

        comboFiltroPor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);

            if (newVal == null || newVal.equals("Selecciona un tipo de filtro...")) {
                campoBuscarUsuario.clear();
                generarDatosYMostrar();
                botonQuitarFiltro.setDisable(true);
                return;
            }

            ObservableList<String> opciones = FXCollections.observableArrayList();
            String textoPlaceholder = "";

            Conexion.conectar();
            switch (newVal) {
                case "Filtrar por rol":
                    opciones = ConsultasUsuario.cargarRolesUsuarios();
                    textoPlaceholder = "Seleccione un rol...";
                    break;
                case "Filtrar por idioma":
                    opciones = ConsultasUsuario.cargarIdiomasDisponibles();
                    textoPlaceholder = "Seleccione un idioma...";
                    break;
                case "Filtrar por tipo de compañía":
                    opciones = ConsultasUsuario.cargarTiposDeCompania();
                    textoPlaceholder = "Seleccione un tipo de compañía...";
                    break;
            }
            Conexion.cerrarConexion();

            if (!textoPlaceholder.isEmpty() && !opciones.contains(textoPlaceholder)) {
                opciones.add(0, textoPlaceholder);
            }

            comboValorFiltro.setItems(opciones);
            comboValorFiltro.getSelectionModel().selectFirst();
            comboValorFiltro.setDisable(false);
        });

        comboValorFiltro.getSelectionModel()
                .selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null || newVal.equalsIgnoreCase("Seleccione un rol...")
                            || newVal.equalsIgnoreCase("Seleccione un idioma...")
                            || newVal.equalsIgnoreCase("Seleccione un tipo de compañía...")) {
                        generarDatosYMostrar();
                        botonQuitarFiltro.setDisable(true);
                        return;
                    }

                    ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
                    String filtro = comboFiltroPor.getValue();

                    if ("Filtrar por rol".equals(filtro)) {
                        Conexion.conectar();
                        ConsultasUsuario.cargarUsuariosPorRol(listaUsuarios, newVal);
                        Conexion.cerrarConexion();
                    } else {
                        String campo = "";
                        if ("Filtrar por idioma".equals(filtro)) {
                            campo = "idioma_preferido";
                        } else if ("Filtrar por tipo de compañía".equals(filtro)) {
                            campo = "tipo_viajero";
                        }

                        Conexion.conectar();
                        ConsultasUsuario.cargarUsuariosPorCampo(listaUsuarios, campo, newVal);
                        Conexion.cerrarConexion();
                    }

                    tablaUsuarios.setItems(listaUsuarios);
                    botonQuitarFiltro.setDisable(false);
                }
                );

        botonQuitarFiltro.setOnAction(e
                -> {
            comboFiltroPor.getSelectionModel().selectFirst();
            comboValorFiltro.getItems().clear();
            comboValorFiltro.getSelectionModel().clearSelection();
            comboValorFiltro.setDisable(true);
            campoBuscarUsuario.clear();
            tablaUsuarios.setItems(generarDatosUsuario());
            comboFiltroPor.setVisible(false);
            comboFiltroPor.setVisible(true);
            botonQuitarFiltro.setDisable(true);
        }
        );

    }

    /**
     * Carga y asigna a la tabla los datos de los usuarios, ya sea al iniciar o
     * tras quitar filtros.
     */
    public void generarDatosYMostrar() {
        ObservableList<Usuario> listaUsuarios = generarDatosUsuario();
        tablaUsuarios.setItems(listaUsuarios);
    }

    /**
     * Realiza una búsqueda en tiempo real sobre los usuarios en función del
     * texto proporcionado.
     *
     * @param texto Texto ingresado por el usuario para filtrar resultados.
     */
    private void buscarUsuariosEnTiempoReal(String texto) {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        Conexion.conectar();
        ConsultasUsuario.cargarDatosUsuariosFiltrados(listaUsuarios, texto);
        Conexion.cerrarConexion();
        tablaUsuarios.setItems(listaUsuarios);
    }

    /**
     * Consulta la base de datos y obtiene una lista completa de usuarios
     * registrados.
     *
     * @return Una lista observable de objetos Usuario con los datos extraídos
     * de la base de datos.
     */
    public ObservableList<Usuario> generarDatosUsuario() {
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
        try (Connection conn = Conexion.conectar()) {
            String query = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro FROM usuarios";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("tipo_usuario"),
                        rs.getDate("fecha_registro"),
                        rs.getString("tipo_viajero"),
                        rs.getString("idioma_preferido"),
                        rs.getString("telefono")
                );
                listaUsuarios.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaUsuarios;
    }

    /**
     * Recarga por completo la tabla de usuarios desde la base de datos. Se
     * utiliza tras agregar, editar o eliminar registros.
     */
    public void recargarTabla() {
        ObservableList<Usuario> listaUsuarios = generarDatosUsuario();
        tablaUsuarios.setItems(listaUsuarios);
        tablaUsuarios.setPlaceholder(new Label("No hay usuarios registrados."));
    }

    /**
     * Personaliza las celdas de la tabla para mostrar columnas con formato
     * enriquecido: - Columna usuario con avatar y correo. - Columna rol con
     * estilo visual distintivo. - Columna fecha con formato de fecha simple.
     */
    private void initializeCustomCells() {
        columnAcciones.setCellFactory(col -> new CeldaAccionesUsuario(this));

        columnUsuario.setCellFactory(col -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String nombre, boolean empty) {
                super.updateItem(nombre, empty);
                if (empty || nombre == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox contenedor = new HBox(10);
                    contenedor.setAlignment(Pos.CENTER_LEFT);
                    Label avatar = new Label("\uD83D\uDC64");
                    avatar.getStyleClass().add("user-avatar");

                    VBox info = new VBox(2);
                    Label nombreLabel = new Label(nombre);
                    nombreLabel.getStyleClass().add("user-name");

                    Label correoLabel = new Label(getTableRow().getItem().getEmail());
                    correoLabel.getStyleClass().add("user-email");

                    info.getChildren().addAll(nombreLabel, correoLabel);
                    contenedor.getChildren().addAll(avatar, info);
                    setGraphic(contenedor);
                }
            }
        });

        columnRol.setCellFactory(col -> new TableCell<Usuario, String>() {
            @Override
            protected void updateItem(String rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(rol);
                    badge.getStyleClass().add(
                            rol.equalsIgnoreCase("admin") ? "rol-admin" : "rol-visualizador"
                    );
                    HBox container = new HBox(badge);
                    container.setAlignment(Pos.CENTER);
                    setGraphic(container);
                }
            }
        });

        columnFechaRegistro.setCellFactory(col -> new TableCell<Usuario, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            protected void updateItem(Date fecha, boolean empty) {
                super.updateItem(fecha, empty);
                setText(empty || fecha == null ? null : formatter.format(fecha));
            }
        });
    }

    /**
     * Abre una ventana modal para registrar un nuevo usuario. Si se realiza un
     * registro exitoso, se recarga la tabla principal.
     *
     * @param event Evento que dispara la apertura del formulario.
     */
    @FXML
    private void irAnuevoUsuario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarUsuario.fxml"));
            Parent root = loader.load();

            AgregarUsuarioController controlador = loader.getController();
            controlador.setTitulo("Agregar Usuario");
            controlador.setAdministracionUsuarioController(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setUserData(this);
            stage.setScene(scene);
            stage.setTitle("Agregar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/img/montanita.png"));
            stage.showAndWait();

            if (controlador.getModificado()) {
                recargarTabla();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
