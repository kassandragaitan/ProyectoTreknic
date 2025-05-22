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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.InformeTipoAlojamiento;

/**
 * Controlador de la vista de reportes.
 * <p>
 * Este módulo permite visualizar reportes estadísticos interactivos en la
 * aplicación. Incluye gráficos de pastel y de líneas para analizar datos como
 * usuarios registrados por idioma o distribución de tipos de alojamiento.
 * Proporciona estadísticas resumidas como total, promedio, máximo y
 * crecimiento.
 * </p>
 * <p>
 * <b>Proyecto a futuro:</b> Se planea implementar la funcionalidad de
 * generación de PDF desde esta vista, permitiendo exportar los gráficos y
 * estadísticas con estilo profesional.</p>
 *
 * Funciones principales:
 * <ul>
 * <li>Visualización de gráficos interactivos de usuarios y alojamientos.</li>
 * <li>Filtrado de reportes por idioma o tipo.</li>
 * <li>Animación de datos al cargarse.</li>
 * <li>Resumen estadístico de los datos mostrados.</li>
 * <li>Apertura de ventana para creación de nuevos reportes.</li>
 * <li>(Futuro) Exportación de gráficos y estadísticas a PDF.</li>
 * </ul>
 *
 * Componentes clave:
 * <ul>
 * <li>{@link javafx.scene.chart.PieChart}</li>
 * <li>{@link javafx.scene.chart.LineChart}</li>
 * <li>{@link javafx.scene.control.ComboBox}</li>
 * <li>{@link javafx.scene.control.Label}</li>
 * <li>{@link javafx.stage.FileChooser} (comentado para PDF)</li>
 * </ul>
 *
 * @author k0343
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
    private Label labelCrecimiento;
    @FXML
    private Label labelFiltro;
    @FXML
    private ComboBox<String> comboFiltro;
    @FXML
    private ComboBox<String> comboTipoReporte;
    @FXML
    private Button botonNuevoReporte;
    @FXML
    private Button botonGenerarPdf;

    /**
     * Inicializa la vista de reportes.
     * <p>
     * Configura la animación del panel, inicializa los ComboBox con las
     * opciones de reporte disponibles y oculta los elementos estadísticos hasta
     * que se seleccione un tipo de reporte. Asocia también los eventos de
     * cambio de selección en los combos para reaccionar dinámicamente.
     * </p>
     *
     * @param url URL de inicialización (no utilizado).
     * @param rb Recursos de internacionalización (no utilizado).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animacion.aparecer2000(panel);

        comboTipoReporte.setItems(FXCollections.observableArrayList(
                "Usuarios registrados", "Tipos de alojamiento"
        ));
        comboTipoReporte.setPromptText("Seleccione tipo de reporte");

        comboFiltro.setVisible(false);
        labelFiltro.setVisible(false);
        labelTotal.setVisible(false);
        labelPromedio.setVisible(false);
        labelMaximo.setVisible(false);
        labelCrecimiento.setVisible(false);

        comboTipoReporte.setOnAction(e -> cambiarTipoReporte());
        comboFiltro.setOnAction(e -> cargarGrafico());
    }

    /**
     * Cambia el tipo de reporte seleccionado y actualiza dinámicamente las
     * opciones de filtro, etiquetas y componentes visibles según el contexto
     * (usuarios o tipos de alojamiento).
     */
    private void cambiarTipoReporte() {
        String seleccion = comboTipoReporte.getValue();

        comboFiltro.getItems().clear();
        comboFiltro.setPromptText("Seleccione filtro");

        if (seleccion == null || seleccion.isEmpty()) {
            comboFiltro.setVisible(false);
            labelFiltro.setVisible(false);
            labelTotal.setVisible(false);
            labelPromedio.setVisible(false);
            labelMaximo.setVisible(false);
            labelCrecimiento.setVisible(false);
            return;
        }

        comboFiltro.setVisible(true);
        labelFiltro.setVisible(true);
        labelTotal.setVisible(true);
        labelPromedio.setVisible(true);
        labelMaximo.setVisible(true);
        labelCrecimiento.setVisible(true);

        if (seleccion.equals("Usuarios registrados")) {
            labelFiltro.setText("Idioma");
            comboFiltro.setItems(ConsultasReportes.obtenerIdiomasUsuarios());
        } else if (seleccion.equals("Tipos de alojamiento")) {
            labelFiltro.setText("Tipo de alojamiento");
            comboFiltro.setItems(ConsultasReportes.obtenerTiposAlojamiento());
        }
    }

    /**
     * Carga y muestra el gráfico correspondiente según el tipo de reporte y el
     * filtro seleccionados.
     * <p>
     * Puede mostrar un gráfico de línea para usuarios o un gráfico circular
     * para tipos de alojamiento.
     * </p>
     */
    private void cargarGrafico() {
        String seleccionReporte = comboTipoReporte.getValue();
        String filtroSeleccionado = comboFiltro.getValue();

        if (seleccionReporte == null || filtroSeleccionado == null) {
            return;
        }

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
     * Carga un gráfico de líneas que representa la evolución mensual de
     * usuarios registrados para un idioma específico. Calcula y actualiza
     * métricas como total, promedio, máximo y crecimiento.
     *
     * @param idioma Idioma seleccionado como filtro.
     */
    private void cargarGraficoUsuarios(String idioma) {
        Map<String, Integer> datos = ConsultasReportes.obtenerUsuariosPorMesYIdioma(idioma);
        lineChart.getData().clear();

        if (datos == null || datos.isEmpty()) {
            lineChart.setTitle("No hay datos para mostrar");
            labelTotal.setText("0");
            labelPromedio.setText("0");
            labelMaximo.setText("0");
            labelCrecimiento.setText("0%");
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
            if (valor > maximo) {
                maximo = valor;
            }
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
     * Carga un gráfico de sectores que representa la distribución de tipos de
     * alojamiento. Calcula y actualiza métricas como total, promedio y valor
     * máximo.
     *
     * @param tipo Tipo de alojamiento seleccionado como filtro.
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
            labelTotal.setText("0");
            labelPromedio.setText("0");
            labelMaximo.setText("0");
            labelCrecimiento.setText("0%");
            return;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        int total = 0;
        int maximo = 0;

        for (InformeTipoAlojamiento item : datos) {
            PieChart.Data data = new PieChart.Data(item.getTipo(), item.getCantidad());
            pieData.add(data);
            total += item.getCantidad();
            if (item.getCantidad() > maximo) {
                maximo = item.getCantidad();
            }
        }

        double promedio = (double) total / datos.size();

        graficoTipos.setTitle("");
        graficoTipos.setLegendVisible(true);
        graficoTipos.setLabelsVisible(true);
        graficoTipos.setClockwise(true);
        graficoTipos.setStartAngle(90);
        graficoTipos.setData(pieData);

        for (PieChart.Data data : pieData) {
            double porcentaje = (data.getPieValue() / total) * 100;
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue() + " (" + String.format("%.1f", porcentaje) + "%)");
            Tooltip.install(data.getNode(), tooltip);
            Animacion.animarDatosGrafico(data.getNode());
        }

        Animacion.transicionGrafico(graficoTipos);
        actualizarEstadisticas(total, promedio, maximo, 0);
    }

    /**
     * Actualiza los indicadores estadísticos mostrados en pantalla: total,
     * promedio, valor máximo y crecimiento porcentual respecto al período
     * anterior.
     *
     * @param total Total acumulado de registros.
     * @param promedio Promedio calculado sobre el total.
     * @param maximo Valor máximo registrado en los datos.
     * @param crecimiento Porcentaje de variación respecto al valor anterior
     * (puede ser positivo o negativo).
     */
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

    /**
     * Abre una ventana modal para registrar manualmente un nuevo reporte.
     * <p>
     * La ventana se muestra con estilo decorado, sin maximizar ni permitir
     * redimensionamiento.
     * </p>
     *
     * @param event Evento generado al hacer clic en el botón "Nuevo Reporte".
     */
    @FXML
    private void abrirVentanaNuevoReporte(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarReporte.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nuevo Reporte");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alertas.error("Error", "No se pudo abrir la ventana de nuevo reporte.");
        }
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
