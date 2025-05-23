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
import javafx.geometry.Insets;
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
import modelo.ConexionFtp;
import modelo.ConfiguracionSistema;
import modelo.Destino;
import modelo.InformeActividadDestino;
import modelo.Usuario;

/**
 * Controlador principal de la interfaz de usuario para el panel de bienvenida
 * del sistema.
 * <p>
 * Esta clase administra la carga de datos iniciales como destinos populares,
 * actividades recientes y alojamientos favoritos. También gestiona el inicio de
 * sesión, temporizador de expiración de sesión, categorización de destinos y
 * personalización de la apariencia.
 * </p>
 *
 * Componentes principales gestionados:
 * <ul>
 * <li>Tabla de destinos populares con visitas y valoración</li>
 * <li>Gráfico de barras para actividades recientes</li>
 * <li>Tarjetas de destinos agrupados por categoría</li>
 * <li>Alojamientos marcados como favoritos</li>
 * </ul>
 *
 * Funcionalidades clave:
 * <ul>
 * <li>Carga inicial de datos</li>
 * <li>Actualización de interfaz visual basada en preferencias de usuario</li>
 * <li>Expiración de sesión por inactividad</li>
 * <li>Adaptación al color de fondo personalizado</li>
 * </ul>
 *
 * @author k0343
 */
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

    /**
     * Inicializa los componentes visuales y carga datos al iniciar la vista
     * principal.
     *
     * @param url no se utiliza.
     * @param rb no se utiliza.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaVisitas.setCellValueFactory(new PropertyValueFactory<>("visitas"));
        columnaValoracion.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(generarEstrellas(cellData.getValue().getValoracion())));

        columnaDestino.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));
        columnaVisitas.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));
        columnaValoracion.prefWidthProperty().bind(tablaDestinos.widthProperty().divide(3));
        tablaDestinos.setPlaceholder(new Label("No hay destinos registrados."));
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

    /**
     * Inicia un temporizador de sesión que cerrará automáticamente la sesión
     * tras el tiempo indicado.
     *
     * @param minutos cantidad de minutos antes de expirar la sesión.
     */
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

    /**
     * Cierra la sesión del usuario actual, redirigiendo al formulario de login.
     */
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

    /**
     * Inicializa el panel con el nombre del usuario y lo guarda como usuario
     * actual.
     *
     * @param usuario instancia del usuario autenticado.
     */
    public void inicializarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        Usuario.setUsuarioActual(usuario);
        labelNombre.setText(usuario.getNombre());
    }

    /**
     * Carga la lista de categorías en el ComboBox y configura la acción de
     * filtrado por categoría.
     */
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

    /**
     * Muestra todos los destinos agrupados por categoría en la interfaz visual.
     */
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

                flowDestinos.setPrefWrapLength(500);

                for (Destino destino : destinos) {
                    VBox tarjeta = crearTarjetaDestino(destino);
                    flowDestinos.getChildren().add(tarjeta);
                }

                bloqueCategoria.getChildren().addAll(tituloCategoria, flowDestinos);
                contenedorCategorias.getChildren().add(bloqueCategoria);
            }
        }
    }

    /**
     * Carga los alojamientos favoritos del usuario y los representa con
     * tarjetas visuales.
     */
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

    /**
     * Crea una tarjeta visual estilizada para un alojamiento.
     *
     * @param aloj objeto Alojamiento con los datos a mostrar.
     * @return VBox que contiene el diseño del alojamiento.
     */
    private VBox crearTarjetaAlojamiento(Alojamiento aloj) {
        VBox tarjeta = new VBox(6);
        tarjeta.setPrefWidth(200);
        tarjeta.setPrefHeight(200);
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);");

        ImageView imagen = new ImageView();
        imagen.setFitWidth(180);
        imagen.setFitHeight(100);
        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);
        imagen.setCache(true);

        if (aloj.getImagen() != null && !aloj.getImagen().isBlank()) {
            try {
                ConexionFtp.cargarImagen(aloj.getImagen(), imagen);
            } catch (Exception ex) {
                System.err.println("Error cargando imagen desde FTP: " + ex.getMessage());
                imagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
            }
        } else {
            imagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
        }

        StackPane imagenContenedor = new StackPane(imagen);
        imagenContenedor.setPrefSize(180, 100);
        imagenContenedor.setMaxSize(180, 100);
        imagenContenedor.setMinSize(180, 100);

        Label nombre = new Label(aloj.getNombre());
        nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label destino = new Label("Destino: " + aloj.getNombreDestino());
        destino.setWrapText(true);
        destino.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        destino.setMaxWidth(180);
        destino.setAlignment(Pos.CENTER);
        destino.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        tarjeta.getChildren().addAll(imagenContenedor, nombre, destino);

        FlowPane.setMargin(tarjeta, new Insets(40, 0, 0, 0));

        return tarjeta;
    }

    /**
     * Carga y muestra los destinos correspondientes a la categoría
     * seleccionada.
     */
    private void cargarDestinosPorCategoria() {
        contenedorCategorias.getChildren().clear();
        String categoria = comboCategoria.getValue();

        if (categoria != null) {
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

                int tarjetasPorFila = 2;
                double anchoTarjeta = 200;
                double espacioEntre = flowDestinos.getHgap();
                double wrapLength = tarjetasPorFila * anchoTarjeta + (tarjetasPorFila - 1) * espacioEntre;
                flowDestinos.setPrefWrapLength(wrapLength);

                for (Destino destino : destinos) {
                    VBox tarjeta = crearTarjetaDestino(destino);
                    flowDestinos.getChildren().add(tarjeta);
                }

                HBox contenedorFlow = new HBox(flowDestinos);
                contenedorFlow.setAlignment(Pos.CENTER);

                bloqueCategoria.getChildren().addAll(tituloCategoria, contenedorFlow);
                contenedorCategorias.getChildren().add(bloqueCategoria);
            }
        }
    }

    /**
     * Aplica el color de fondo personalizado definido por el sistema.
     */
    private void aplicarColorFondo() {
        String colorHex = Utilidades.EstiloSistema.getInstancia().getColorFondoHex();
        scrollPane.setStyle("-fx-background: " + colorHex + "; -fx-background-color: " + colorHex + ";");
    }

    /**
     * Carga y muestra un gráfico de barras con los 5 destinos que tienen más
     * actividades registradas. Si hay menos de 5 destinos, se oculta el gráfico
     * y se muestra un mensaje informativo. Se aplica animación y tooltips a
     * cada barra del gráfico.
     */
    private void cargarGraficoActividadReciente() {
        ObservableList<InformeActividadDestino> actividadesPorDestino;

        Conexion.conectar();
        actividadesPorDestino = Conexion.cargarActividadesPorDestino();
        Conexion.cerrarConexion();

        if (actividadesPorDestino.size() < 5) {
            Label sinDatos = new Label("Aún no hay suficientes datos para mostrar el ranking.");
            sinDatos.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
            sinDatos.setAlignment(Pos.CENTER);

            VBox contenedorLabel = new VBox(sinDatos);
            contenedorLabel.setAlignment(Pos.CENTER);
            contenedorLabel.setPrefHeight(390);

            barChartActividad.setVisible(false);

            if (barChartActividad.getParent() instanceof VBox) {
                VBox contenedorPadre = (VBox) barChartActividad.getParent();
                contenedorPadre.getChildren().removeIf(n -> n instanceof Label);
                contenedorPadre.getChildren().add(contenedorLabel);
            }

            return;
        }

        barChartActividad.setVisible(true);
        barChartActividad.getData().clear();

        XYChart.Series<String, Integer> serieDatos = new XYChart.Series<>();
        serieDatos.setName("Top 5 destinos con más actividades");

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

    /**
     * Genera una representación visual en forma de estrellas basada en una
     * puntuación numérica.
     *
     * @param valoracion Puntuación numérica (de 0 a 5).
     * @return Una cadena de estrellas llenas (★) y vacías (☆) que representa la
     * valoración.
     */
    private String generarEstrellas(double valoracion) {
        StringBuilder estrellas = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            estrellas.append(i < valoracion ? "★" : "☆");
        }
        return estrellas.toString();
    }

    /**
     * Carga todos los destinos disponibles desde la base de datos y los muestra
     * en la tabla principal.
     */
    private void cargarDatosDestinos() {
        ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
        ConsultasDestinos.cargarDatosDestinos(listadoDestinos);
        tablaDestinos.setItems(listadoDestinos);
    }

    /**
     * Recupera y muestra las estadísticas globales del sistema como: usuarios
     * registrados, itinerarios activos, destinos disponibles y actividades
     * registradas. Los resultados se muestran en etiquetas correspondientes en
     * el panel principal.
     */
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

    /**
     * Crea una tarjeta visual (VBox) que representa un destino, incluyendo su
     * imagen, nombre y descripción. Si la imagen no está disponible, se usa una
     * imagen por defecto.
     *
     * @param destino Objeto {@link Destino} que contiene la información a
     * mostrar.
     * @return Un contenedor VBox estilizado que representa visualmente el
     * destino.
     */
    private VBox crearTarjetaDestino(Destino destino) {
        VBox tarjeta = new VBox(6);
        tarjeta.setPrefWidth(200);
        tarjeta.setPrefHeight(200);
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-background-radius: 10px; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 1);");

        ImageView imagen = new ImageView();
        imagen.setFitWidth(180);
        imagen.setFitHeight(100);
        imagen.setPreserveRatio(true);
        imagen.setSmooth(true);
        imagen.setCache(true);

        if (destino.getImagen() != null && !destino.getImagen().isBlank()) {
            try {
                ConexionFtp.cargarImagen(destino.getImagen(), imagen);
            } catch (Exception ex) {
                System.err.println("Error cargando imagen desde FTP: " + ex.getMessage());
                imagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
            }
        } else {
            imagen.setImage(new Image(getClass().getResourceAsStream("/img/default-image.png")));
        }

        StackPane imagenContenedor = new StackPane(imagen);
        imagenContenedor.setPrefSize(180, 100);
        imagenContenedor.setMaxSize(180, 100);
        imagenContenedor.setMinSize(180, 100);

        Label nombre = new Label(destino.getNombre());
        nombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label descripcion = new Label(destino.getDescripcion());
        descripcion.setWrapText(true);
        descripcion.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");
        descripcion.setMaxWidth(180);
        descripcion.setAlignment(Pos.CENTER);
        descripcion.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        tarjeta.getChildren().addAll(imagenContenedor, nombre, descripcion);

        return tarjeta;
    }

}
