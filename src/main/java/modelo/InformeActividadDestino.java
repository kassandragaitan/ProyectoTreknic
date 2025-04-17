/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class InformeActividadDestino {
    private String destino;
    private int actividades;

    public InformeActividadDestino(String destino, int actividades) {
        this.destino = destino;
        this.actividades = actividades;
    }

    public String getDestino() {
        return destino;
    }

    public int getActividades() {
        return actividades;
    }
}