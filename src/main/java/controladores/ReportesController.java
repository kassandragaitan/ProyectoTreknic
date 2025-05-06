package controladores;

import Utilidades.Animacion;
import bbdd.ConsultasReportes;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import modelo.InformeTipoAlojamiento;

public class ReportesController implements Initializable {

    @FXML
    private AnchorPane panel;
    @FXML
    private PieChart graficoTipos;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private Label labelTotal;
    @FXML
    private Label labelPromedio;
    @FXML
    private Label labelMaximo;
    @FXML
    private Label labelCrecimiento;
    @FXML
    private Label labelFiltro;
    @FXML
    private ComboBox<String> comboFiltro;
    @FXML
    private ComboBox<String> comboTipoReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animacion.aparecer2000(panel);

        comboTipoReporte.setItems(FXCollections.observableArrayList(
                "Usuarios registrados", "Tipos de alojamiento"
        ));
        comboTipoReporte.setPromptText("Seleccione tipo de reporte");

        // Initially hide the filter and statistics labels
        comboFiltro.setVisible(false);
        labelFiltro.setVisible(false);
        labelTotal.setVisible(false);
        labelPromedio.setVisible(false);
        labelMaximo.setVisible(false);
        labelCrecimiento.setVisible(false);

        comboTipoReporte.setOnAction(e -> cambiarTipoReporte());
        comboFiltro.setOnAction(e -> cargarGrafico());
    }

    private void cambiarTipoReporte() {
        String seleccion = comboTipoReporte.getValue();

        // Clear comboFiltro items and prompt
        comboFiltro.getItems().clear();
        comboFiltro.setPromptText("Seleccione filtro");

        // Hide all the panels initially if no report is selected
        if (seleccion == null || seleccion.isEmpty()) {
            comboFiltro.setVisible(false);
            labelFiltro.setVisible(false);
            labelTotal.setVisible(false);
            labelPromedio.setVisible(false);
            labelMaximo.setVisible(false);
            labelCrecimiento.setVisible(false);
            return;
        }

        // Show the filter and statistics panels for valid selections
        comboFiltro.setVisible(true);
        labelFiltro.setVisible(true);
        labelTotal.setVisible(true);
        labelPromedio.setVisible(true);
        labelMaximo.setVisible(true);
        labelCrecimiento.setVisible(true);

        // Set appropriate filter options based on report type
        if (seleccion.equals("Usuarios registrados")) {
            labelFiltro.setText("Idioma");
            comboFiltro.setItems(ConsultasReportes.obtenerIdiomasUsuarios());
        } else if (seleccion.equals("Tipos de alojamiento")) {
            labelFiltro.setText("Tipo de alojamiento");
            comboFiltro.setItems(ConsultasReportes.obtenerTiposAlojamiento());
        }
    }

    private void cargarGrafico() {
        String seleccionReporte = comboTipoReporte.getValue();
        String filtroSeleccionado = comboFiltro.getValue();

        if (seleccionReporte == null || filtroSeleccionado == null) {
            return;
        }

        // Hide all charts initially
        lineChart.setVisible(false);
        graficoTipos.setVisible(false);

        // Display appropriate chart based on selected report
        if (seleccionReporte.equals("Usuarios registrados")) {
            cargarGraficoUsuarios(filtroSeleccionado);
            lineChart.setVisible(true);
        } else if (seleccionReporte.equals("Tipos de alojamiento")) {
            cargarGraficoTiposAlojamiento(filtroSeleccionado);
            graficoTipos.setVisible(true);
        }
    }

    private void cargarGraficoUsuarios(String idioma) {
        Map<String, Integer> datos = ConsultasReportes.obtenerUsuariosPorMesYIdioma(idioma);
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Usuarios por mes: " + idioma);

        int total = 0;
        int maximo = 0;
        int anterior = -1;
        int crecimiento = 0;

        // Process the data
        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            int valor = entry.getValue();
            serie.getData().add(new XYChart.Data<>(entry.getKey(), valor));
            total += valor;
            if (valor > maximo) {
                maximo = valor;
            }
            if (anterior != -1) {
                crecimiento = valor - anterior;
            }
            anterior = valor;
        }

        double promedio = datos.isEmpty() ? 0 : (double) total / datos.size();

        // Update the chart
        lineChart.getData().clear();
        lineChart.getData().add(serie);

        // Animations
        Animacion.transicionGrafico(lineChart);
        for (XYChart.Data<String, Number> data : serie.getData()) {
            Animacion.animarDatosGrafico(data.getNode());
        }

        actualizarEstadisticas(total, promedio, maximo, crecimiento);
    }

    private void cargarGraficoTiposAlojamiento(String tipo) {
        ObservableList<InformeTipoAlojamiento> datos;

        if (tipo.equals("Todos los tipos")) {
            datos = ConsultasReportes.obtenerDatosTiposAlojamiento();
        } else {
            datos = ConsultasReportes.obtenerDatosTiposAlojamientoPorTipo(tipo);
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        int total = 0;
        int maximo = 0;

        // Process the data
        for (InformeTipoAlojamiento item : datos) {
            PieChart.Data data = new PieChart.Data(item.getTipo(), item.getCantidad());
            pieData.add(data);
            total += item.getCantidad();
            if (item.getCantidad() > maximo) {
                maximo = item.getCantidad();
            }
        }

        double promedio = datos.isEmpty() ? 0 : (double) total / datos.size();

        // Update the pie chart
        graficoTipos.setLegendVisible(true);
        graficoTipos.setLabelsVisible(true);
        graficoTipos.setClockwise(true);
        graficoTipos.setStartAngle(90);
        graficoTipos.setData(pieData);

        for (PieChart.Data data : pieData) {
            double porcentaje = (data.getPieValue() / total) * 100;
            Tooltip tooltip = new Tooltip(
                    data.getName() + ": " + (int) data.getPieValue() + " (" + String.format("%.1f", porcentaje) + "%)"
            );
            Tooltip.install(data.getNode(), tooltip);

            // Animations
            Animacion.animarDatosGrafico(data.getNode());
        }

        // General transition animation
        Animacion.transicionGrafico(graficoTipos);

        actualizarEstadisticas(total, promedio, maximo, 0);
    }

    private void actualizarEstadisticas(int total, double promedio, int maximo, int crecimiento) {
        labelTotal.setText(String.valueOf(total));
        labelPromedio.setText(String.format("%.1f", promedio));
        labelMaximo.setText(String.valueOf(maximo));

        if (crecimiento >= 0) {
            labelCrecimiento.setText("+" + crecimiento + "%");
            labelCrecimiento.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            labelCrecimiento.setText(crecimiento + "%");
            labelCrecimiento.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }
}
