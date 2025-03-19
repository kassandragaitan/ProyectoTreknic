/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import java.sql.Time;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;


public class Animacion {
    public static void animacionEscena(BorderPane marco) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), marco.getCenter());
        ft.setFromValue(0.0);
        ft.setToValue(1);
        ft.play();
    }

    public static void linearTiempo(Node node) {
        KeyValue keyValue = new KeyValue(node.translateYProperty(), 40);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.5), keyValue);
        Timeline line = new Timeline(keyFrame);
        line.setCycleCount(1);
        line.play();
    }

    public static void desaparecer2000(Node nombre) {
        nombre.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), nombre);
        ft.setFromValue(1);
        ft.setToValue(0); 
        ft.play();
    }

    public static void aparecer2000(Node nombre) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), nombre);
        ft.setFromValue(0); 
        ft.setToValue(0.9);
        ft.play();
    }
}
