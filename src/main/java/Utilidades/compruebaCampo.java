/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

/**
 * Clase utilitaria que proporciona métodos estáticos para validar campos de
 * entrada. Incluye comprobaciones de campos vacíos, formatos numéricos y
 * correos electrónicos.
 *
 * Útil para centralizar la lógica de validación de formularios y evitar
 * duplicación de código.
 *
 * @author k0343
 */
public class compruebaCampo {

    /**
     * Comprueba si un campo de texto está vacío (ignorando espacios).
     *
     * @param campo Campo de entrada de texto.
     * @return true si el campo está vacío, false en caso contrario.
     */
    public static boolean compruebaVacio(TextInputControl campo) {
        return campo.getText().trim().isEmpty();
    }

    /**
     * Comprueba si no se ha seleccionado ningún valor en un ComboBox.
     *
     * @param selector ComboBox a comprobar.
     * @return true si no hay ningún valor seleccionado, false si hay selección.
     */
    public static boolean compruebaNull(ComboBox selector) {
        return selector.getSelectionModel().isEmpty();
    }

    /**
     * Verifica si el texto del campo tiene el formato de correo electrónico
     * válido.
     *
     * @param campo Campo de texto que contiene el correo a validar.
     * @return true si el correo es válido, false si no cumple el formato.
     */
    public static boolean emailValido(TextField campo) {
        String email = campo.getText().trim();
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

}
