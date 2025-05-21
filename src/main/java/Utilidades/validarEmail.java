/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

/**
 * Clase utilitaria para validar direcciones de correo electrónico. Contiene un
 * único método estático que verifica si un string tiene un formato válido de
 * email.
 *
 * Ideal para validar datos antes de almacenarlos o enviarlos.
 *
 * @author k0343
 */
public class validarEmail {

    /**
     * Verifica si el correo electrónico proporcionado es válido. Se considera
     * válido si tiene el formato estándar: texto@dominio.ext
     *
     * @param email Cadena de texto que representa el correo electrónico.
     * @return true si el formato del email es válido, false si es nulo, vacío o
     * no cumple el patrón.
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}
