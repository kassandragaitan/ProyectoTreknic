/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilidades;

public class validarTelefono {

    public static boolean esTelefonoNicaraguenseValido(String numero) {
        return numero.matches("^[2578]\\d{7}$");
    }

    public static boolean esSoloNumeros(String texto) {
        return texto != null && texto.matches("\\d+");
    }
}
