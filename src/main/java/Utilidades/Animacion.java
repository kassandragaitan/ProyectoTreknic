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
import javafx.scene.control.CheckBox;
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

    public static void transicionGrafico(Node grafico) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), grafico);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void animarDatosGrafico(Node node) {
        node.setScaleX(0);
        node.setScaleY(0);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.scaleXProperty(), 0),
                        new KeyValue(node.scaleYProperty(), 0)
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(node.scaleXProperty(), 1),
                        new KeyValue(node.scaleYProperty(), 1)
                )
        );
        timeline.play();
    }

    public static void animarCheckBox(CheckBox checkBox) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), checkBox);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void aplicarAnimaciones(CheckBox checkBox) {
        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            animarCheckBox(checkBox);
        });
    }
}
