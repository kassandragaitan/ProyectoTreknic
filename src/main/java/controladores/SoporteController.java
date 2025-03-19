/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modelo.FAQItem;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class SoporteController implements Initializable {

    @FXML
    private TabPane mainTabPane;
    @FXML
    private ListView<FAQItem> faqList;  // Cambio aquí para manejar FAQItem
    @FXML
    private TableView<Ticket> ticketsTable;
    @FXML
    private ListView<String> guidesList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        // Load tickets
        ticketsTable.getItems().addAll(
                new Ticket(1, "Error al crear itinerario"),
                new Ticket(2, "Problema con carga de imágenes")
        );

        // Load guides
        guidesList.getItems().addAll("Guía de Inicio Rápido", "Gestión de Itinerarios Avanzada");
    }

    public static class Ticket {

        private SimpleIntegerProperty id;
        private SimpleStringProperty subject;

        public Ticket(int id, String subject) {
            this.id = new SimpleIntegerProperty(id);
            this.subject = new SimpleStringProperty(subject);
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public StringProperty subjectProperty() {
            return subject;
        }
    }
}
