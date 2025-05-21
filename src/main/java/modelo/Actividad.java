/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa una actividad turística asociada a un destino.
 *
 * Contiene atributos como nombre, descripción, identificador del destino y el
 * nombre del destino. Esta clase se utiliza en la gestión de actividades dentro
 * del sistema de turismo.
 *
 * Puede ser utilizada para mostrar actividades en tablas, registrar nuevas o
 * modificar existentes.
 *
 * @author k0343
 */
public class Actividad {

    private int idActividad;
    private String nombre;
    private String descripcion;
    private int idDestino;
    private String nombreDestino;

    /**
     * Constructor vacío requerido para instanciación sin parámetros.
     */
    public Actividad() {
    }

    /**
     * Constructor que recibe datos completos para persistencia.
     *
     * @param idActividad Identificador de la actividad.
     * @param nombre Nombre de la actividad.
     * @param descripcion Descripción de la actividad.
     * @param idDestino Identificador del destino asociado.
     */
    public Actividad(int idActividad, String nombre, String descripcion, int idDestino) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idDestino = idDestino;
    }

    /**
     * Constructor para mostrar datos con nombre del destino.
     *
     * @param idActividad Identificador de la actividad.
     * @param nombre Nombre de la actividad.
     * @param descripcion Descripción de la actividad.
     * @param nombreDestino Nombre del destino asociado.
     */
    public Actividad(int idActividad, String nombre, String descripcion, String nombreDestino) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nombreDestino = nombreDestino;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }
}
