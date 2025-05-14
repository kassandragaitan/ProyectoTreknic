package controladores;

import Utilidades.Alertas;
import bbdd.Conexion;
import bbdd.ConsultasAlojamientos;
import bbdd.ConsultasDestinos;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.Alojamiento;
import modelo.ConfiguracionSistema;
import modelo.Destino;
import modelo.InformeActividadDestino;
import modelo.Usuario;

public class PrincipalController implements Initializable {

    @FXML
    private TableColumn<Destino, String> columnaDestino;
    @FXML
    private TableColumn<Destino, Integer> columnaVisitas;
    @FXML
    private TableColumn<Destino, String> columnaValoracion;
    @FXML
    private TableView<Destino> tablaDestinos;
    @FXML
    private Label labelUsuarioReg;
    @FXML
    private Label labelItinerarioActivo;
    @FXML
    private Label labelDestinosPopulares;
    @FXML
    private Label labelActividadesMen;
    @FXML
    private BarChart<String, Integer> barChartActividad;
    @FXML
    private NumberAxis ejeYActividad;
    @FXML
    private CategoryAxis ejeXActividad;
    @FXML
    private AnchorPane contenedor;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ComboBox<String> comboCategoria;
    @FXML
    private FlowPane contenedorCategorias;
    @FXML
    private Label labelNombre;
    @FXML
    private FlowPane contenedorFavoritos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaVisitas.setCellValueFactory(new PropertyValueFactory<>("visitas"));
        columnaValoracion.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(generarEstrellas(cellData.getValue().getValoracion())));

        columnaDestino.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));
        columnaVisitas.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));
        columnaValoracion.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));

        ejeXActividad.setTickLabelRotation(0);
        ejeXActividad.setTickLabelGap(10);
        ejeXActividad.setTickLength(10);
        ejeYActividad.setTickUnit(1);
        ejeYActividad.setMinorTickVisible(false);
        ejeYActividad.setForceZeroInRange(true);

        cargarDatosDestinos();
        cargarDatosPanel();
        cargarGraficoActividadReciente();
        aplicarColorFondo();

        Platform.runLater(() -> {
            String css = ConfiguracionSistema.getInstancia().getCssPersonalizado();
            if (!css.isEmpty() && scrollPane.getScene() != null) {
                scrollPane.getScene().getStylesheets().add(css);
            }
        });

        inicializarCategorias();
        mostrarTodosLosDestinosAgrupados();
        cargarAlojamientosFavoritos();
    }

    private Timer temporizadorSesion;

    public void iniciarTemporizadorSesion(int minutos) {
        if (temporizadorSesion != null) {
            temporizadorSesion.cancel();
        }
        temporizadorSesion = new Timer();
        temporizadorSesion.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    cerrarSesion();
                    Alertas.aviso("Sesión finalizada", "Por seguridad, tu sesión ha expirado.");
                });
            }
        }, minutos * 60 * 1000);
    }

    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) contenedor.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Iniciar Sesión");
            stage.show();

            Usuario.setUsuarioActual(null);

        } catch (IOException e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo cerrar la sesión correctamente.");
        }
    }

    private Usuario usuarioActual;

    public void inicializarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        Usuario.setUsuarioActual(usuario);

        labelNombre.setText(usuario.getNombre());
    }

    private void inicializarCategorias() {
        List<String> categorias = ConsultasDestinos.obtenerNombresCategorias();
        comboCategoria.getItems().addAll(categorias);

        mostrarTodosLosDestinosAgrupados();

        comboCategoria.setOnAction(e -> {
            String seleccion = comboCategoria.getValue();
            if (seleccion != null && !seleccion.isEmpty()) {
                cargarDestinosPorCategoria();
            } else {
                mostrarTodosLosDestinosAgrupados();
            }
        });
    }

    private void mostrarTodosLosDestinosAgrupados() {
        contenedorCategorias.getChildren().clear();
        List<String> categorias = ConsultasDestinos.obtenerNombresCategorias();

        for (String categoria : categorias) {
            List<Destino> destinos = ConsultasDestinos.obtenerDestinosPorNombreCategoria(categoria);

            if (!destinos.isEmpty()) {
                VBox bloqueCategoria = new VBox(10);
                bloqueCategoria.setAlignment(Pos.TOP_CENTER);

                Label tituloCategoria = new Label(categoria);
                tituloCategoria.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2F6EB5;");

                FlowPane flowDestinos = new FlowPane();
                flowDestinos.setHgap(15);
                flowDestinos.setVgap(15);
                flowDestinos.setAlignment(Pos.CENTER);

                // Cálculo dinámico para 2 tarjetas por fila:
                int tarjetasPorFila = 2;
                double anchoTarjeta = 200;
                double espacioEntre = flowDestinos.getHgap();
                double wrapLength = tarjetasPorFila * anchoTarjeta + (tarjetasPorFila - 1) * espacioEntre;
                flowDestinos.setPrefWrapLength(wrapLength); // Fuerza a 2 por fila

                for (Destino destino : destinos) {
                    VBox tarjeta = new VBox(8);
                    tarjeta.setPrefWidth(anchoTarjeta);
                    tarjeta.setPrefHeight(220);
                    tarjeta.setAlignment(Pos.TOP_CENTER);
                    tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);");

                    String urlImagen = "http://localhost/carpetaimg/" + destino.getImagen();
                    ImageView imagen = new ImageView(new Image(urlImagen, true));
                    imagen.setFitWidth(180);
                    imagen.setFitHeight(120);
                    imagen.setPreserveRatio(true);
                    imagen.setSmooth(true);
                    imagen.setCache(true);

                    Label nombre = new Label(destino.getNombre());
                    nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    tarjeta.getChildren().addAll(imagen, nombre);
                    flowDestinos.getChildren().add(tarjeta);
                }

                HBox contenedorFlow = new HBox(flowDestinos);
                contenedorFlow.setAlignment(Pos.CENTER);

                bloqueCategoria.getChildren().addAll(tituloCategoria, contenedorFlow);
                contenedorCategorias.getChildren().add(bloqueCategoria);
            }
        }
    }

    private void cargarAlojamientosFavoritos() {
        contenedorFavoritos.getChildren().clear();
        List<Alojamiento> favoritos = ConsultasAlojamientos.obtenerAlojamientosFavoritosPorUsuario(Usuario.getUsuarioActual().getIdUsuario());

        if (favoritos == null || favoritos.isEmpty()) {
            Label sinFavoritos = new Label("No hay alojamientos favoritos.");
            sinFavoritos.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorFavoritos.getChildren().add(sinFavoritos);
            return;
        }

        for (Alojamiento aloj : favoritos) {
            VBox tarjeta = crearTarjetaAlojamiento(aloj);
            contenedorFavoritos.getChildren().add(tarjeta);

            FadeTransition ft = new FadeTransition(Duration.millis(500), tarjeta);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }

    private VBox crearTarjetaAlojamiento(Alojamiento aloj) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(200);
        tarjeta.setPrefHeight(220);
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);");

        String urlImagen = "http://localhost/carpetaimg/" + aloj.getImagen();
        ImageView imagen = new ImageView(new Image(urlImagen, true));
        imagen.setFitWidth(180);
        imagen.setFitHeight(120);
        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);
        imagen.setCache(true);

        Label nombre = new Label(aloj.getNombre());
        nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label destino = new Label("Destino: " + aloj.getNombreDestino());
        destino.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        tarjeta.getChildren().addAll(imagen, nombre, destino);
        return tarjeta;
    }

    private void cargarDestinosPorCategoria() {
        contenedorCategorias.getChildren().clear();
        String categoria = comboCategoria.getValue();

        if (categoria != null) {
            List<Destino> destinos = ConsultasDestinos.obtenerDestinosPorNombreCategoria(categoria);

            for (Destino destino : destinos) {
                VBox tarjeta = new VBox(8);
                tarjeta.setPrefWidth(200);
                tarjeta.setPrefHeight(220);
                tarjeta.setAlignment(Pos.TOP_CENTER);
                tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                        + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);");

                String urlImagen = "http://localhost/carpetaimg/" + destino.getImagen();
                ImageView imagen = new ImageView(new Image(urlImagen, true));
                imagen.setFitWidth(180);
                imagen.setFitHeight(120);
                imagen.setPreserveRatio(true);
                imagen.setSmooth(true);
                imagen.setCache(true);

                Label nombre = new Label(destino.getNombre());
                nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                tarjeta.getChildren().addAll(imagen, nombre);
                contenedorCategorias.getChildren().add(tarjeta);

                FadeTransition ft = new FadeTransition(Duration.millis(400), tarjeta);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        }
    }

    private void aplicarColorFondo() {
        String colorHex = Utilidades.EstiloSistema.getInstancia().getColorFondoHex();
        scrollPane.setStyle("-fx-background: " + colorHex + "; -fx-background-color: " + colorHex + ";");
    }

    private void cargarGraficoActividadReciente() {
        ObservableList<InformeActividadDestino> actividadesPorDestino;

        Conexion.conectar();
        actividadesPorDestino = Conexion.cargarActividadesPorDestino();
        Conexion.cerrarConexion();

        barChartActividad.getData().clear();

        XYChart.Series<String, Integer> serieDatos = new XYChart.Series<>();
        serieDatos.setName("ACTIVIDADES POR DESTINO");

        for (InformeActividadDestino elemento : actividadesPorDestino) {
            XYChart.Data<String, Integer> dato = new XYChart.Data<>(elemento.getDestino(), elemento.getActividades());
            serieDatos.getData().add(dato);
        }

        barChartActividad.getData().add(serieDatos);

        Platform.runLater(() -> {
            for (XYChart.Data<String, Integer> data : serieDatos.getData()) {
                Node barra = data.getNode();
                if (barra instanceof StackPane) {
                    barra.setStyle("-fx-bar-fill: #daeafe;");

                    Tooltip tooltip = new Tooltip(data.getXValue().toUpperCase() + ": " + data.getYValue());
                    tooltip.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 6px;");
                    Tooltip.install(barra, tooltip);

                    Label etiqueta = new Label(String.valueOf(data.getYValue()));
                    etiqueta.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 2px 6px; -fx-border-radius: 4px; -fx-background-radius: 4px;");

                    StackPane stack = (StackPane) barra;
                    stack.getChildren().add(etiqueta);

                    barra.boundsInParentProperty().addListener((obs, oldVal, newVal) -> {
                        etiqueta.setTranslateY(-newVal.getHeight() - 10);
                    });
                }
            }
        });
    }

    private String generarEstrellas(double valoracion) {
        StringBuilder estrellas = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            estrellas.append(i < valoracion ? "★" : "☆");
        }
        return estrellas.toString();
    }

    private void cargarDatosDestinos() {
        ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
        ConsultasDestinos.cargarDatosDestinos(listadoDestinos);
        tablaDestinos.setItems(listadoDestinos);
    }

    private void cargarDatosPanel() {
        int totalUsuarios = Conexion.contar("usuarios");
        int totalItinerarios = Conexion.contar("itinerarios");
        int totalDestinosP = Conexion.contar("destinos");
        int totalActividadesMen = Conexion.contar("actividades");

        labelUsuarioReg.setText(totalUsuarios + " Usuarios");
        labelItinerarioActivo.setText(totalItinerarios + " Itinerarios");
        labelDestinosPopulares.setText(totalDestinosP + " Destinos");
        labelActividadesMen.setText(totalActividadesMen + " Actividades");
    }

}
