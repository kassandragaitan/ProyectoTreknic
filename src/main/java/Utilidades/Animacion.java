/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 * Clase utilitaria que proporciona animaciones básicas reutilizables para nodos
 * JavaFX. Las animaciones incluyen transiciones de opacidad, escalado y
 * movimiento. Ideal para mejorar la experiencia visual del usuario en la
 * interfaz gráfica.
 */
public class Animacion {

    /**
     * Aplica una animación de desvanecimiento (fade in) al centro de un
     * BorderPane.
     *
     * @param marco BorderPane que contiene el nodo a animar.
     */
    public static void animacionEscena(BorderPane marco) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), marco.getCenter());
        ft.setFromValue(0.0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Aplica una animación de movimiento lineal vertical (translateY) al nodo
     * dado.
     *
     * @param node Nodo al que se aplicará la animación.
     */
    public static void linearTiempo(Node node) {
        KeyValue keyValue = new KeyValue(node.translateYProperty(), 40);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.5), keyValue);
        Timeline line = new Timeline(keyFrame);
        line.setCycleCount(1);
        line.play();
    }

    /**
     * Aplica una transición para desaparecer un nodo (fade out) en 2000
     * milisegundos.
     *
     * @param nombre Nodo que se desea hacer desaparecer.
     */
    public static void desaparecer2000(Node nombre) {
        nombre.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(2000), nombre);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    /**
     * Aplica una transición para hacer aparecer un nodo (fade in) con opacidad
     * de 0.9 en 2000 milisegundos.
     *
     * @param nombre Nodo que se desea hacer aparecer.
     */
    public static void aparecer2000(Node nombre) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), nombre);
        ft.setFromValue(0);
        ft.setToValue(0.9);
        ft.play();
    }

    /**
     * Aplica una animación de desvanecimiento (fade in) a un gráfico o
     * componente visual.
     *
     * @param grafico Nodo gráfico que se desea animar.
     */
    public static void transicionGrafico(Node grafico) {
        FadeTransition ft = new FadeTransition(Duration.millis(800), grafico);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Aplica una animación de escala (zoom in) a un nodo, útil para datos en
     * gráficos.
     *
     * @param node Nodo al que se aplicará la animación de escala.
     */
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
}
