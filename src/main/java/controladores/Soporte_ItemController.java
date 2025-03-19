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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.FAQItem;

/**
 * FXML Controller class
 *
 * @author k0343
 */
public class Soporte_ItemController implements Initializable {

    @FXML
    private ImageView iconImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }    
       public void setItem(FAQItem item) {
        titleLabel.setText(item.getTitle());
        descriptionLabel.setText(item.getDescription());
        iconImageView.setImage(new Image(getClass().getResourceAsStream("/img/documento.png")));
    }
}
