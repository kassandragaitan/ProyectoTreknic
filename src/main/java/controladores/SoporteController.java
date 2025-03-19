/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import modelo.FAQItem;
import modelo.Ticket;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class SoporteController implements Initializable {

    @FXML
    private ListView<FAQItem> faqList;  // Cambio aquí para manejar FAQItem
    @FXML
    private TableView<Ticket> ticketsTable;
    @FXML
    private ListView<String> guidesList;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Ticket, String> columnID;
    @FXML
    private TableColumn<Ticket, String> columnAsunto;
    @FXML
    private TableColumn<Ticket, String> columnEstado;
    @FXML
    private TableColumn<Ticket, String> columnPrioridad;
    @FXML
    private TableColumn<Ticket, String> columnCreado;
    @FXML
    private TableColumn<Ticket, String> columnUltimaActualizacion;
    @FXML
    private TableColumn<Ticket, Button> columnAcciones;
    @FXML
    private TextField searchField1;

    private ObservableList<Ticket> ticketData = FXCollections.observableArrayList();

    public static class ButtonTableCell<S> extends TableCell<S, Button> {

        private final Button actionButton = new Button();

        public ButtonTableCell(String buttonText, Consumer<S> action) {
            actionButton.setText(buttonText);
            actionButton.setOnAction(e -> action.accept(getTableView().getItems().get(getIndex())));
            setGraphic(actionButton);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String buttonText, Consumer<S> action) {
            return column -> new ButtonTableCell<>(buttonText, action);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ticketData.addAll(
                new Ticket("TK-001", "Error al crear itinerario", "Abierto", "Alta", "1/10/2023", "2/10/2023"),
                new Ticket("TK-002", "Problema con carga de imágenes", "En Proceso", "Media", "28/9/2023", "1/10/2023"),
                new Ticket("TK-003", "Solicitud de nueva funcionalidad", "Abierto", "Baja", "25/9/2023", "25/9/2023")
        );

        // Configura la TableView
        initializeTicketsTable();
        ticketsTable.setItems(ticketData);
        // Carga de FAQ y guías
        loadFAQs();
        loadGuides();

    }

    private void initializeTicketsTable() {

        columnID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        columnAsunto.setCellValueFactory(cellData -> cellData.getValue().asuntoProperty());
        columnEstado.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        columnPrioridad.setCellValueFactory(cellData -> cellData.getValue().prioridadProperty());
        columnCreado.setCellValueFactory(cellData -> cellData.getValue().creadoProperty());
        columnUltimaActualizacion.setCellValueFactory(cellData -> cellData.getValue().ultimaActualizacionProperty());

        columnAcciones.setCellFactory(param -> new ButtonTableCell<>("Ver", this::handleViewTicket));
        ticketsTable.setItems(ticketData);
    }

    private void handleViewTicket(Ticket ticket) {
        // Aquí manejas la acción del botón Ver, por ejemplo, mostrar detalles del ticket.
        System.out.println("Ticket ID: " + ticket.getId() + " viewed.");
    }

    private void loadFAQs() {
        // Load FAQs
        faqList.getItems().addAll(
                new FAQItem("¿Cómo crear un nuevo itinerario?", "Para crear un nuevo itinerario, vaya a la sección 'Gestión de Itinerarios' y haga clic en el botón 'Nuevo Itinerario'. Complete el formulario con la información requerida y guarde los cambios."),
                new FAQItem("¿Cómo añadir un nuevo destino?", "Para añadir un nuevo destino, acceda a la sección 'Gestión de Destinos' y haga clic en 'Nuevo Destino'. Complete los campos necesarios incluyendo nombre, país, descripción e imágenes representativas."),
                new FAQItem("¿Cómo gestionar los permisos de usuario?", "Los permisos de usuario se gestionan desde la sección 'Gestión de Usuarios'. Seleccione el usuario deseado y haga clic en 'Editar' para modificar su rol y permisos en el sistema."),
                new FAQItem("¿Cómo generar un reporte personalizado?", "Para generar un reporte personalizado, vaya a la sección 'Reportes y Estadísticas', seleccione el tipo de reporte y el período de tiempo deseado. Luego puede exportar los datos en varios formatos."),
                new FAQItem("¿Cómo configurar notificaciones automáticas?", "Las notificaciones automáticas se configuran en la sección 'Configuración de Notificaciones'. Puede crear nuevas reglas de notificación y definir los destinatarios y condiciones para cada una.")
        );
        faqList.setCellFactory(param -> new ListCell<FAQItem>() {
            @Override
            protected void updateItem(FAQItem item, boolean empty) {
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
                        setText("Error loading FXML");
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void loadGuides() {
        guidesList.getItems().addAll(
                "Guía de Inicio Rápido",
                "Documentación de Referencia",
                "Manual de Usuario Final",
                "Guía de Configuración Avanzada",
                "Preguntas Frecuentes"
        );

        guidesList.getItems().addAll("Guía 1", "Guía 2", "Guía 3");

        guidesList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Soporte_Item.fxml"));
                        Node node = loader.load();
                        Soporte_ItemController controller = loader.getController();
                        // Asumiendo que Soporte_ItemController puede manejar un String
                        controller.setItem(item);
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setText("Error loading FXML");
                        setGraphic(null);
                    }
                }
            }
        });
    }

}
