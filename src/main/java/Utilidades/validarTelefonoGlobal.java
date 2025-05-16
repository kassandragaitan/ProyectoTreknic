/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

public class validarTelefonoGlobal {

    /**
     * Valida un número de teléfono internacional: - Permite con o sin "+" al
     * inicio - Solo dígitos (sin espacios, guiones ni letras) - Longitud entre
     * 8 y 15 dígitos - Incluye soporte para Nicaragua (números de 8 dígitos que
     * empiezan con 2, 5, 7 u 8)
     */
    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }

        if (!telefono.matches("^\\+?[0-9]{8,15}$")) {
            return false;
        }
        if (!telefono.startsWith("+") && telefono.length() == 8) {
            char primerDigito = telefono.charAt(0);
            return primerDigito == '2' || primerDigito == '5' || primerDigito == '7' || primerDigito == '8';
        }
        return true;
    }

    public static boolean esTelefonoNicaraguenseValido(String numero) {
        return numero.matches("^[2578]\\d{7}$");
    }

}
