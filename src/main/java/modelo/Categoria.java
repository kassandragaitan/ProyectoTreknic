/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa una categoría dentro del sistema.
 * 
 * Una categoría se utiliza para agrupar destinos turísticos según su temática,
 * tipo o característica común, como "Cultural", "Aventura", "Naturaleza", etc.
 * 
 * Contiene un identificador, un nombre descriptivo y una descripción adicional.
 * 
 * Esta clase se utiliza principalmente en la gestión y visualización de destinos.
 * 
 * @author k0343
 */
public class Categoria {

    private int idCategoria;
    private String nombre;
    private String descripcion;

    /**
     * Constructor vacío.
     */
    public Categoria() {
    }

    /**
     * Constructor para una categoría con ID y nombre (sin descripción).
     * 
     * @param idCategoria ID de la categoría.
     * @param nombre Nombre de la categoría.
     */
    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    /**
     * Constructor para crear una categoría sin ID (por ejemplo, al registrar una nueva).
     * 
     * @param nombre Nombre de la categoría.
     * @param descripcion Descripción de la categoría.
     */
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Constructor completo con ID, nombre y descripción.
     * 
     * @param idCategoria ID de la categoría.
     * @param nombre Nombre de la categoría.
     * @param descripcion Descripción de la categoría.
     */
    public Categoria(int idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
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

    @Override
    public String toString() {
        return nombre;
    }

}
