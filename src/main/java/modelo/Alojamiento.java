/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class Alojamiento {

    private int idAlojamiento;
    private String nombre;
    private int idTipo;
    private String contacto;
    private String imagen;
    private int idDestino;

    public Alojamiento() {
    }

    public Alojamiento(int idAlojamiento, String nombre, int idTipo, String contacto, String imagen, int idDestino) {
        this.idAlojamiento = idAlojamiento;
        this.nombre = nombre;
        this.idTipo = idTipo;
        this.contacto = contacto;
        this.imagen = imagen;
        this.idDestino = idDestino;
    }

    public int getIdAlojamiento() {
        return idAlojamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public String getContacto() {
        return contacto;
    }

    public String getImagen() {
        return imagen;
    }

    public int getIdDestino() {
        return idDestino;
    }

    public void setIdAlojamiento(int id) {
        this.idAlojamiento = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setIdDestino(int idDestino) {
        this.idDestino = idDestino;
    }
}
