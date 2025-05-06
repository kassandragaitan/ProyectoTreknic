package controladores;

import bbdd.ConsultasDestinos;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Destino;

public class GestionDestinosController implements Initializable {

    @FXML
    private TextField campoBuscarDestino;
    @FXML
    private Button botonNuevoDestino;
    @FXML
    private TilePane destinationsPane;

    @FXML
    private ScrollPane scrollPane;

    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Destino> destinos = ConsultasDestinos.obtenerDestinos();
        for (Destino destino : destinos) {
            addDestinationCard(destino);
        }
        recargarTabla();
    }

    // Hacerlo público para poder acceder desde otro controlador
    public void addDestinationCard(Destino destino) {
        VBox destinationCard = new VBox(5);
        destinationCard.getStyleClass().add("destination-card");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(75);
        imageView.setFitWidth(130);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        if (destino.getImagen() != null && !destino.getImagen().isEmpty()) {
            try {
                String rutaBase = "C:/xampp/htdocs/carpetaimg/";
                File imagenFile = new File(rutaBase + destino.getImagen());
                if (imagenFile.exists()) {
                    Image image = new Image(imagenFile.toURI().toString());
                    imageView.setImage(image);
                } else {
                    System.out.println("Imagen no encontrada: " + imagenFile.getPath());
                }
            } catch (Exception e) {
                System.out.println("Error al cargar imagen: " + e.getMessage());
            }
        }

        Label nameLabel = new Label(destino.getNombre());
        Label descriptionLabel = new Label(destino.getDescripcion());
        Label dateLabel = new Label("Fecha: " + formatoFecha.format(destino.getFecha_creacion()));

        HBox buttonBox = new HBox(10);
        Button editButton = new Button("Editar");
        Button deactivateButton = new Button("Desactivar");
        buttonBox.getChildren().addAll(editButton, deactivateButton);

        destinationCard.getChildren().addAll(imageView, nameLabel, descriptionLabel, dateLabel, buttonBox);
        destinationsPane.getChildren().add(destinationCard);
    }

// Método para recargar la tabla
    public void recargarTabla() {
        // Limpiar el contenedor
        destinationsPane.getChildren().clear();

        // Obtener los destinos de nuevo y agregar las tarjetas
        List<Destino> destinos = ConsultasDestinos.obtenerDestinos();
        for (Destino destino : destinos) {
            addDestinationCard(destino);
        }
    }

    @FXML
    private void irANuevoDestino(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/AgregarDestino.fxml"));
            Parent root = loader.load();

            // Pasar la referencia del controlador principal
            AgregarDestinoController controlador = loader.getController();
            controlador.setGestionDestinosController(this);  // Aquí se pasa la referencia

            Stage stage = new Stage();
            stage.setTitle("Agregar Destino");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
