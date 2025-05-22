/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controlador para el componente visual de un ítem de reseña.
 * <p>
 * Esta clase administra la representación gráfica de una reseña dentro de una
 * lista o contenedor en la interfaz, incluyendo su icono, título y descripción.
 * </p>
 *
 * Componentes gestionados:
 * <ul>
 * <li><b>iconImageView</b>: imagen asociada a la reseña (por ejemplo, un avatar
 * o ícono representativo).</li>
 * <li><b>titleLabel</b>: título o encabezado de la reseña.</li>
 * <li><b>descriptionLabel</b>: contenido o resumen de la reseña.</li>
 * </ul>
 *
 * @author k0343
 */
public class Resenas_ItemController implements Initializable {

    @FXML
    private ImageView iconImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    /**
     * Inicializa el controlador una vez cargada la vista FXML.
     *
     * @param url no se utiliza.
     * @param rb no se utiliza.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
