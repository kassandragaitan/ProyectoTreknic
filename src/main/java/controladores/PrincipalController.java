/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import bbdd.Conexion;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import modelo.Destino;
import modelo.InformeActividadDestino;

/**
 * FXML Controller class
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaVisitas.setCellValueFactory(new PropertyValueFactory<>("visitas"));
        columnaValoracion.setCellValueFactory(cellData
                -> new ReadOnlyStringWrapper(generarEstrellas(cellData.getValue().getValoracion()))
        );

        ejeXActividad.setTickLabelRotation(0); // Horizontal
        ejeXActividad.setTickLabelGap(10);
        ejeXActividad.setTickLength(10);       // Espacio entre texto y eje

        ejeYActividad.setTickUnit(1);
        ejeYActividad.setMinorTickVisible(false);
        ejeYActividad.setForceZeroInRange(true);

        cargarDatosDestinos();
        cargarDatosPanel();
        cargarGraficoActividadReciente();
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
                    tooltip.setStyle(
                            "-fx-background-color: rgba(0, 0, 0, 0.8); "
                            + "-fx-text-fill: white; "
                            + "-fx-font-weight: bold; "
                            + "-fx-font-size: 12px; "
                            + "-fx-padding: 6px;"
                    );
                    Tooltip.install(barra, tooltip);

                    Label etiqueta = new Label(String.valueOf(data.getYValue()));
                    etiqueta.setStyle(
                            "-fx-background-color: white; "
                            + "-fx-text-fill: black; "
                            + "-fx-font-weight: bold; "
                            + "-fx-font-size: 12px; "
                            + "-fx-padding: 2px 6px; "
                            + "-fx-border-radius: 4px; "
                            + "-fx-background-radius: 4px;"
                    );

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
        String estrellas = "";
        for (int i = 0; i < 5; i++) {
            estrellas += i < valoracion ? "★" : "☆";
        }
        return estrellas;
    }

    private void cargarDatosDestinos() {
        ObservableList<Destino> listadoDestinos = FXCollections.observableArrayList();
        Conexion.cargarDatosDestinos(listadoDestinos);
        tablaDestinos.setItems(listadoDestinos);
    }

    private void cargarDatosPanel() {
        int totalUsuarios = Conexion.contar("usuarios");
        int totalItinerarios = Conexion.contar("itinerario");
        int totalDestinosP = Conexion.contar("destinos");
        int totalActividadesMen = Conexion.contar("actividades");

        labelUsuarioReg.setText(String.valueOf(totalUsuarios) + " Usuarios");
        labelItinerarioActivo.setText(String.valueOf(totalItinerarios) + " Itinerarios");
        labelDestinosPopulares.setText(String.valueOf(totalDestinosP) + " Destinos");
        labelActividadesMen.setText(String.valueOf(totalActividadesMen) + " Actividades");
    }

}
