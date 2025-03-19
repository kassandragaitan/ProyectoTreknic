/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Destino;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaDestino.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaVisitas.setCellValueFactory(new PropertyValueFactory<>("visitas"));
        columnaValoracion.setCellValueFactory(cellData -> {
            double valoracion = cellData.getValue().getValoracion();
            String estrellas = "";
            for (int i = 0; i < 5; i++) {
                estrellas += i < valoracion ? "★" : "☆";
            }
            return new ReadOnlyStringWrapper(estrellas);
        });

        // Añade datos de ejemplo
        tablaDestinos.getItems().addAll(
                new Destino("Barcelona, España", 1245, 4.8),
                new Destino("París, Francia", 1120, 4.7),
                new Destino("Roma, Italia", 980, 4.6),
                new Destino("Nueva York, EEUU", 875, 4.5),
                new Destino("Tokio, Japón", 740, 4.9)
        );
    }
}
