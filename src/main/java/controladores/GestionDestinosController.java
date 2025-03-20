/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Destino;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionDestinosController implements Initializable {

    @FXML
    private TextField campoBuscarDestino;
    @FXML
    private Button botonNuevoDestino;
    @FXML
    private TilePane destinationsPane;
    @FXML
    private Label labelNombre;
    @FXML
    private Label labelDescripcion;
    @FXML
    private Label labelFechaCreacion;
    @FXML
    private Button botonEditar;
    @FXML
    private Button botonDesactivar;
    @FXML
    private Label labelNombr;
    @FXML
    private Label labelDescripcio;
    @FXML
    private Label labelFechaCreacio;
    @FXML
    private Label labelNombreDestino;
    @FXML
    private Label labelDescripcionDestino;
    @FXML
    private Label labelFechaCreacionDestino;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void addDestinationCard(Destino destino) {
        VBox destinationCard = new VBox(5);
        destinationCard.getStyleClass().add("destination-card");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(75);
        imageView.setFitWidth(130);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        // Añadir imagen si está disponible

        Label nameLabel = new Label(destino.getNombre());
        Label descriptionLabel = new Label(destino.getDescripcion());
        Label dateLabel = new Label("Fecha: " + destino.getFecha_creacion().toString());

        HBox buttonBox = new HBox(10);
        Button editButton = new Button("Editar");
        Button deactivateButton = new Button("Desactivar");
        buttonBox.getChildren().addAll(editButton, deactivateButton);

        destinationCard.getChildren().addAll(imageView, nameLabel, descriptionLabel, dateLabel, buttonBox);

        destinationsPane.getChildren().add(destinationCard);
    }

    @FXML
    private void irANuevoDestino(ActionEvent event) {
             try {
            // Cargar el FXML de la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarDestino.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Agregar usuario");
            stage.setScene(new Scene(root));

            // Hacer que la ventana sea modal (bloquea la principal hasta que se cierre)
            stage.initModality(Modality.APPLICATION_MODAL);

            // Mostrar la ventana y esperar hasta que se cierre
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}
