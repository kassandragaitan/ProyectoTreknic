/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 *
 * @author k0343
 */
public class compruebaCampo {

    public static boolean compruebaVacio(TextField campo) {
        return campo.getText().isEmpty();
    }

    public static boolean compruebaNull(ComboBox selector) {
        return selector.getSelectionModel().isEmpty();
    }

    public static boolean enteroCorrecto(TextField campo) {
        int minum;
        try {
            minum = Integer.parseInt(campo.getText());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean doubleCorrecto(TextField campo) {
        try {
            double num = Double.parseDouble(campo.getText());
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
