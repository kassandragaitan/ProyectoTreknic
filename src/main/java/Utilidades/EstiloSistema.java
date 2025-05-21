/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

import javafx.scene.paint.Color;

/**
 * Clase Singleton que gestiona el color de fondo del sistema. Permite
 * establecer y recuperar un color de fondo consistente a lo largo de la
 * aplicación. Ideal para aplicar temas o personalización visual.
 *
 * Uso:
 * <pre>
 *     EstiloSistema estilo = EstiloSistema.getInstancia();
 *     estilo.setColorFondo(Color.LIGHTGRAY);
 * </pre>
 *
 * @author k0343
 */
public class EstiloSistema {

    /**
     * Instancia única de la clase.
     */
    private static EstiloSistema instancia;

    /**
     * Color de fondo del sistema (por defecto blanco).
     */
    private Color colorFondo = Color.WHITE;

    /**
     * Constructor privado para aplicar el patrón Singleton.
     */
    private EstiloSistema() {
    }

    /**
     * Devuelve la única instancia de la clase (Singleton).
     *
     * @return instancia única de EstiloSistema.
     */
    public static EstiloSistema getInstancia() {
        if (instancia == null) {
            instancia = new EstiloSistema();
        }
        return instancia;
    }

    /**
     * Obtiene el color de fondo actual del sistema.
     *
     * @return Color de fondo.
     */
    public Color getColorFondo() {
        return colorFondo;
    }

    /**
     * Establece el color de fondo del sistema.
     *
     * @param colorFondo Nuevo color de fondo a aplicar.
     */
    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
    }

    /**
     * Devuelve el color de fondo actual en formato hexadecimal (por ejemplo:
     * #FFFFFF).
     *
     * @return String representando el color en formato HEX.
     */
    public String getColorFondoHex() {
        return String.format("#%02X%02X%02X",
                (int) (colorFondo.getRed() * 255),
                (int) (colorFondo.getGreen() * 255),
                (int) (colorFondo.getBlue() * 255));
    }
}
