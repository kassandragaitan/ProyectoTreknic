/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.paint.Color;

/**
 *
 * @author k0343
 */
public class EstiloSistema {

    private static EstiloSistema instancia;
    private Color colorFondo = Color.WHITE; // Color por defecto

    private EstiloSistema() {
    }

    public static EstiloSistema getInstancia() {
        if (instancia == null) {
            instancia = new EstiloSistema();
        }
        return instancia;
    }

    public Color getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
    }

    public String getColorFondoHex() {
        return String.format("#%02x%02x%02x",
                (int) (colorFondo.getRed() * 255),
                (int) (colorFondo.getGreen() * 255),
                (int) (colorFondo.getBlue() * 255));
    }
}
