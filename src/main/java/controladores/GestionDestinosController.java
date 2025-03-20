/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
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
}
