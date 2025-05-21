/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un alojamiento turístico dentro del sistema.
 *
 * Contiene información relevante como nombre, tipo de alojamiento, contacto,
 * imagen, destino asociado, y si es marcado como favorito por el usuario.
 *
 * Se utiliza para la gestión y visualización de alojamientos dentro del sistema
 * Treknic.
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
    private String nombreDestino;
    private String nombreTipo;
    private boolean favorito;

    /**
     * Constructor por defecto.
     */
    public Alojamiento() {
    }

    /**
     * Constructor para crear un alojamiento con identificadores referenciales.
     *
     * @param idAlojamiento ID del alojamiento.
     * @param nombre Nombre del alojamiento.
     * @param idTipo ID del tipo de alojamiento.
     * @param contacto Información de contacto.
     * @param imagen Ruta o nombre del archivo de imagen.
     * @param idDestino ID del destino asociado.
     */
    public Alojamiento(int idAlojamiento, String nombre, int idTipo, String contacto, String imagen, int idDestino) {
        this.idAlojamiento = idAlojamiento;
        this.nombre = nombre;
        this.idTipo = idTipo;
        this.contacto = contacto;
        this.imagen = imagen;
        this.idDestino = idDestino;
    }

    /**
     * Constructor para mostrar datos descriptivos (nombre de tipo y destino).
     *
     * @param idAlojamiento ID del alojamiento.
     * @param nombre Nombre del alojamiento.
     * @param contacto Información de contacto.
     * @param imagen Imagen asociada.
     * @param nombreTipo Nombre del tipo de alojamiento.
     * @param nombreDestino Nombre del destino.
     */
    public Alojamiento(int idAlojamiento, String nombre, String contacto, String imagen, String nombreTipo, String nombreDestino) {
        this.idAlojamiento = idAlojamiento;
        this.nombre = nombre;
        this.contacto = contacto;
        this.imagen = imagen;
        this.nombreTipo = nombreTipo;
        this.nombreDestino = nombreDestino;
    }

    public int getIdAlojamiento() {
        return idAlojamiento;
    }

    public void setIdAlojamiento(int id) {
        this.idAlojamiento = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
