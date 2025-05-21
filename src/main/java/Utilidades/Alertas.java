/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Clase utilitaria para mostrar distintos tipos de alertas gráficas en JavaFX.
 * Centraliza el manejo de mensajes informativos, advertencias, errores y
 * confirmaciones.
 *
 * Proporciona una interfaz sencilla y reutilizable para mostrar ventanas
 * emergentes sin repetir lógica en el resto de la aplicación.
 *
 * @author k0343
 */
public class Alertas {

    /**
     * Muestra una alerta de tipo información con el mensaje especificado.
     *
     * @param mensaje Texto que se mostrará como contenido de la alerta.
     */
    public static void informacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de advertencia con cabecera y mensaje.
     *
     * @param cabezera Título o encabezado del mensaje.
     * @param mensaje Contenido del mensaje.
     */
    public static void aviso(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de error con cabecera y mensaje.
     *
     * @param cabezera Título o encabezado del error.
     * @param mensaje Contenido del error.
     */
    public static void error(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación con cabecera y mensaje, sin devolver
     * el resultado de la acción.
     *
     * @param cabezera Título o encabezado del mensaje.
     * @param mensaje Contenido de la confirmación.
     */
    public static void confirmacion(String cabezera, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Diálogo de confirmación");
        alerta.setHeaderText(cabezera);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Muestra una alerta de confirmación y devuelve true si el usuario confirma
     * (OK).
     *
     * @param titulo Título de la ventana de alerta.
     * @param mensaje Contenido de la alerta de confirmación.
     * @return true si el usuario hace clic en OK, false si cancela o cierra.
     */
    public static boolean confirmacionSoporte(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

}
