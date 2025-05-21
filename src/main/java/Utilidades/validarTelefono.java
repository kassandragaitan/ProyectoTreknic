/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

/**
 * Clase utilitaria para validar números de teléfono. Contiene métodos para
 * verificar si un número es válido en Nicaragua y si una cadena contiene solo
 * dígitos numéricos.
 *
 * Útil para validar formularios de contacto o registro de usuarios.
 *
 * @author k0343
 */
public class validarTelefono {

    /**
     * Verifica si un número de teléfono es válido en Nicaragua. Un número
     * válido debe tener 8 dígitos y comenzar con 2, 5, 7 u 8.
     *
     * @param numero Número de teléfono a validar.
     * @return true si cumple el formato nicaragüense, false en caso contrario.
     */
    public static boolean esTelefonoNicaraguenseValido(String numero) {
        return numero.matches("^[2578]\\d{7}$");
    }

    /**
     * Verifica si una cadena contiene solo caracteres numéricos.
     *
     * @param texto Texto a comprobar.
     * @return true si la cadena contiene solo dígitos, false si contiene otros
     * caracteres o es nula.
     */
    public static boolean esSoloNumeros(String texto) {
        return texto != null && texto.matches("\\d+");
    }
}
