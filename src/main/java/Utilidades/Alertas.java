/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.control.Alert;

/**
 *
 * @author k0343
 */
public class Alertas {

    public static void informacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void aviso(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void error(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static void confirmacion(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Diálogo de confirmación");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
