/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class PreferenciasNotificacion {
    private boolean correo;
    private boolean sistema;
    private boolean resumen;
    private String horaInicio;
    private String horaFin;
    private String dias;

    public boolean isCorreo() { return correo; }
    public void setCorreo(boolean correo) { this.correo = correo; }

    public boolean isSistema() { return sistema; }
    public void setSistema(boolean sistema) { this.sistema = sistema; }

    public boolean isResumen() { return resumen; }
    public void setResumen(boolean resumen) { this.resumen = resumen; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public String getDias() { return dias; }
    public void setDias(String dias) { this.dias = dias; }
}