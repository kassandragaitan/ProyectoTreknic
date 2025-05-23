package controladores;

import Utilidades.Alertas;
import Utilidades.Animacion;
import bbdd.ConsultasReportes;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.InformeTipoAlojamiento;

/**
 * Controlador de la vista de reportes estadísticos en la aplicación Treknic.
 * <p>
 * Permite seleccionar entre diferentes tipos de reporte, aplicar filtros
 * dinámicos (como idioma o tipo de alojamiento) y visualizar resultados en
 * gráficos interactivos. También proporciona métricas estadísticas como total,
 * promedio y máximo.
 * </p>
 *
 * <p>
 * Características principales:
 * <ul>
 * <li>Gráficos actualizados dinámicamente según selección.</li>
 * <li>Ocultamiento de componentes si no hay selección válida.</li>
 * <li>Texto explicativo asociado a cada tipo de reporte para mayor comprensión
 * del usuario.</li>
 * </ul>
 */

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
    private Label labelFiltro;
    @FXML
    private ComboBox<String> comboFiltro;
    @FXML
    private ComboBox<String> comboTipoReporte;
    @FXML
    private Label labelDescripcionGrafico;
    @FXML
    private Button botonGenerarPdf;
    @FXML
    private VBox cardTotal;
    @FXML
    private VBox cardPromedio;
    @FXML
    private VBox cardMaximo;

    /**
     * Inicializa la interfaz de reportes y configura los ComboBox y elementos
     * visibles.
     *
     * @param url ruta del recurso.
     * @param rb recursos internacionalizados.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animacion.aparecer2000(panel);

        comboTipoReporte.setItems(FXCollections.observableArrayList(
                "Usuarios registrados", "Tipos de alojamiento"
        ));
        comboTipoReporte.setPromptText("Seleccione tipo de reporte");

        cardTotal.setVisible(false);
        cardPromedio.setVisible(false);
        cardMaximo.setVisible(false);
        comboFiltro.setVisible(false);
        labelFiltro.setVisible(false);
        labelTotal.setVisible(false);
        labelPromedio.setVisible(false);
        labelMaximo.setVisible(false);
        graficoTipos.setVisible(false);
        lineChart.setVisible(false);
        labelDescripcionGrafico.setText("Seleccione un tipo de reporte y un filtro para visualizar los datos disponibles.");

        comboTipoReporte.setOnAction(e -> cambiarTipoReporte());
        comboFiltro.setOnAction(e -> cargarGrafico());
    }

    /**
     * Cambia el contenido del filtro y actualiza el texto explicativo
     * dependiendo del tipo de reporte seleccionado.
     */
    private void cambiarTipoReporte() {
        String seleccion = comboTipoReporte.getValue();

        comboFiltro.getItems().clear();
        comboFiltro.setVisible(false);
        labelFiltro.setVisible(false);
        labelTotal.setVisible(false);
        labelPromedio.setVisible(false);
        labelMaximo.setVisible(false);
        cardTotal.setVisible(false);
        cardPromedio.setVisible(false);
        cardMaximo.setVisible(false);
        graficoTipos.setVisible(false);
        lineChart.setVisible(false);

        if (seleccion == null || seleccion.isEmpty()) {
            labelDescripcionGrafico.setText("Seleccione un tipo de reporte y un filtro para visualizar los datos disponibles.");
            return;
        }

        comboFiltro.setVisible(true);
        labelFiltro.setVisible(true);
        comboFiltro.getItems().add("Seleccione " + (seleccion.equals("Usuarios registrados") ? "un idioma" : "un tipo"));

        if (seleccion.equals("Usuarios registrados")) {
            labelFiltro.setText("Idioma");
            comboFiltro.getItems().addAll(ConsultasReportes.obtenerIdiomasUsuarios());
            labelDescripcionGrafico.setText("Este gráfico muestra la evolución mensual de usuarios registrados según el idioma seleccionado.");
        } else if (seleccion.equals("Tipos de alojamiento")) {
            labelFiltro.setText("Tipo de alojamiento");
            comboFiltro.getItems().addAll(ConsultasReportes.obtenerTiposAlojamiento());
            labelDescripcionGrafico.setText("Este gráfico representa la distribución de los tipos de alojamiento registrados.");
        }

        comboFiltro.getSelectionModel().select(0);
    }

    /**
     * Carga el gráfico correspondiente según el reporte y filtro seleccionados.
     */
    private void cargarGrafico() {
        String seleccionReporte = comboTipoReporte.getValue();
        String filtroSeleccionado = comboFiltro.getValue();

        if (seleccionReporte == null || filtroSeleccionado == null || filtroSeleccionado.startsWith("Seleccione")) {
            graficoTipos.setVisible(false);
            lineChart.setVisible(false);

            labelTotal.setVisible(false);
            labelPromedio.setVisible(false);
            labelMaximo.setVisible(false);

            cardTotal.setVisible(false);
            cardPromedio.setVisible(false);
            cardMaximo.setVisible(false);
            return;
        }

        labelTotal.setVisible(true);
        labelPromedio.setVisible(true);
        labelMaximo.setVisible(true);

        cardTotal.setVisible(true);
        cardPromedio.setVisible(true);
        cardMaximo.setVisible(true);

        lineChart.setVisible(false);
        graficoTipos.setVisible(false);

        if (seleccionReporte.equals("Usuarios registrados")) {
            cargarGraficoUsuarios(filtroSeleccionado);
            lineChart.setVisible(true);
        } else if (seleccionReporte.equals("Tipos de alojamiento")) {
            cargarGraficoTiposAlojamiento(filtroSeleccionado);
            graficoTipos.setVisible(true);
        }
    }

    /**
     * Genera un gráfico de línea con usuarios por mes para el idioma dado.
     *
     * @param idioma Idioma seleccionado por el usuario.
     */
    private void cargarGraficoUsuarios(String idioma) {
        Map<String, Integer> datos = ConsultasReportes.obtenerUsuariosPorMesYIdioma(idioma);
        lineChart.getData().clear();

        if (datos == null || datos.isEmpty()) {
            lineChart.setTitle("No hay datos para mostrar");
            actualizarEstadisticas(0, 0, 0, 0);
            return;
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Usuarios por mes: " + idioma);

        int total = 0;
        int maximo = 0;
        int anterior = -1;
        int crecimiento = 0;

        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            int valor = entry.getValue();
            serie.getData().add(new XYChart.Data<>(entry.getKey(), valor));
            total += valor;
            maximo = Math.max(maximo, valor);
            if (anterior != -1) {
                crecimiento = valor - anterior;
            }
            anterior = valor;
        }

        double promedio = (double) total / datos.size();

        lineChart.setTitle("");
        lineChart.getData().add(serie);
        Animacion.transicionGrafico(lineChart);

        for (XYChart.Data<String, Number> data : serie.getData()) {
            Animacion.animarDatosGrafico(data.getNode());
        }

        actualizarEstadisticas(total, promedio, maximo, crecimiento);
    }

    /**
     * Carga gráfico circular con datos de tipos de alojamiento.
     *
     * @param tipo Tipo de alojamiento seleccionado por el usuario.
     */
    private void cargarGraficoTiposAlojamiento(String tipo) {
        ObservableList<InformeTipoAlojamiento> datos;

        if (tipo.equals("Todos los tipos")) {
            datos = ConsultasReportes.obtenerDatosTiposAlojamiento();
        } else {
            datos = ConsultasReportes.obtenerDatosTiposAlojamientoPorTipo(tipo);
        }

        graficoTipos.getData().clear();

        if (datos == null || datos.isEmpty()) {
            graficoTipos.setTitle("No hay datos para mostrar");
            actualizarEstadisticas(0, 0, 0, 0);
            return;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        int total = 0;
        int maximo = 0;

        for (InformeTipoAlojamiento item : datos) {
            PieChart.Data data = new PieChart.Data(item.getTipo(), item.getCantidad());
            pieData.add(data);
            total += item.getCantidad();
            maximo = Math.max(maximo, item.getCantidad());
        }

        double promedio = (double) total / datos.size();

        graficoTipos.setTitle("");
        graficoTipos.setLegendVisible(true);
        graficoTipos.setLabelsVisible(true);
        graficoTipos.setClockwise(true);
        graficoTipos.setStartAngle(90);
        graficoTipos.setData(pieData);

        graficoTipos.setLabelsVisible(false);
        for (PieChart.Data data : pieData) {
            double porcentaje = (data.getPieValue() / total) * 100;
            Tooltip tooltip = new Tooltip(
                    data.getName() + ": " + (int) data.getPieValue() + " (" + String.format("%.1f", porcentaje) + "%)");
            Tooltip.install(data.getNode(), tooltip);
        }

        Animacion.transicionGrafico(graficoTipos);
        actualizarEstadisticas(total, promedio, maximo, 0);
    }

    /**
     * Actualiza las etiquetas de resumen estadístico.
     *
     * @param total Total general.
     * @param promedio Promedio calculado.
     * @param maximo Valor máximo.
     * @param crecimiento Crecimiento respecto al mes anterior (solo usuarios).
     */
    private void actualizarEstadisticas(int total, double promedio, int maximo, int crecimiento) {
        labelTotal.setText(String.valueOf(total));
        labelPromedio.setText(String.format("%.1f", promedio));
        labelMaximo.setText(String.valueOf(maximo));
    }

    /**
     * Genera un archivo PDF con el resumen estadístico del reporte actual.
     * <p>
     * Este método está comentado actualmente, y su funcionalidad se considera
     * como parte de un proyecto futuro para exportar los datos generados.
     * </p>
     *
     * @param event Evento generado al hacer clic en el botón "Generar PDF".
     */
    @FXML
    private void generarPdf(ActionEvent event) {
//        try {
//            FileChooser fc = new FileChooser();
//            fc.setTitle("Guardar reporte como PDF");
//            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
//            File destino = fc.showSaveDialog(panel.getScene().getWindow());
//            if (destino == null) {
//                return;
//            }
//
//            PdfWriter writer = new PdfWriter(destino.getAbsolutePath());
//            PdfDocument pdf = new PdfDocument(writer);
//            Document document = new Document(pdf);
//
//            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//
//            Paragraph titulo = new Paragraph("Reporte de " + comboTipoReporte.getValue())
//                    .setFont(fontBold)
//                    .setFontSize(18)
//                    .setMarginBottom(10);
//            document.add(titulo);
//
//            document.add(new Paragraph("Filtro: " + comboFiltro.getValue()));
//            document.add(new Paragraph("Total: " + labelTotal.getText()));
//            document.add(new Paragraph("Promedio: " + labelPromedio.getText()));
//            document.add(new Paragraph("Máximo: " + labelMaximo.getText()));
//            document.add(new Paragraph("Crecimiento: " + labelCrecimiento.getText()));
//
//            document.close();
//
//            Alertas.informacion("PDF generado en:\n" + destino.getAbsolutePath());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Alertas.error("Error generando PDF", ex.getMessage());
//        }
    }
}
