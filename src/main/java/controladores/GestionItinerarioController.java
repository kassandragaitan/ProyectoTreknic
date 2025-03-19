/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import modelo.Itinerario;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class GestionItinerarioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ListView<Itinerario> listaItinerario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Itinerario> itinerarios = FXCollections.observableArrayList(
                new Itinerario("Volcanes y Lagunas", "Explora los famosos volcanes y lagunas de Nicaragua", "/img/volcanes.jpg"),
                new Itinerario("Colonial y Playas", "Descubre la belleza colonial de Granada y las playas del Pacífico", "/img/granada.jpg"),
                new Itinerario("Aventura en Isla de Ometepe", "Aventura y naturaleza en la impresionante Isla de Ometepe", "/img/ometepe.jpg")
        );

        listaItinerario.setItems(itinerarios);
        listaItinerario.setCellFactory(param -> new ListCell<Itinerario>() {
            @Override
            protected void updateItem(Itinerario itinerario, boolean empty) {
                super.updateItem(itinerario, empty);

                if (empty || itinerario == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/Itinerario_Item.fxml"));
                        HBox hbox = loader.load();

                        Label lblNombre = (Label) hbox.lookup("#lblNombre");
                        Label lblDescripcion = (Label) hbox.lookup("#lblRol"); // Renombrado a lblDescripcion para claridad
                        ImageView imgItinerario = (ImageView) hbox.lookup("#imgUsuario"); // Renombrado a imgItinerario

                        lblNombre.setText(itinerario.getNombre());
                        lblDescripcion.setText(itinerario.getDescripcion());
                        imgItinerario.setImage(new Image(itinerario.getFoto()));

                        setGraphic(hbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



    }
}
