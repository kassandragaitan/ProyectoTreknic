package modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Representa un destino turístico con información como nombre, descripción,
 * imagen, fecha de creación, categoría, número de visitas y valoración.
 * 
 * <p>Esta clase es utilizada para mostrar y manipular datos relacionados con los destinos turísticos
 * dentro del sistema de gestión.</p>
 * 
 * @author k0343
 */
public class Destino {

    /** Identificador único del destino. */
    private int id_destino;

    /** Nombre del destino turístico. */
    private String nombre;

    /** Descripción general del destino. */
    private String descripcion;

    /** Categoría asociada al destino */
    private String categoria;

    /** Fecha de creación del registro del destino. */
    private Date fecha_creacion;

    /** Ruta o URL de la imagen asociada al destino. */
    private String imagen;

    /** Número de visitas que ha recibido el destino. */
    private int visitas;

    /** Valoración promedio del destino basada en reseñas. */
    private double valoracion;


    /** Constructor vacío. */
    public Destino() {
    }

    /**
     * Constructor con categoría.
     */
    public Destino(int id_destino, String nombre, String descripcion, String categoria, Date fecha_creacion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fecha_creacion = fecha_creacion;
    }

    /**
     * Constructor básico con imagen.
     */
    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, String imagen) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.imagen = imagen;
    }

    /**
     * Constructor completo con categoría e imagen.
     */
    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, String imagen, String categoria) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    /**
     * Constructor con visitas y valoración.
     */
    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, String imagen, int visitas, double valoracion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.imagen = imagen;
        this.visitas = visitas;
        this.valoracion = valoracion;
    }

    /**
     * Constructor con visitas y valoración (sin imagen).
     */
    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, int visitas, double valoracion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.visitas = visitas;
        this.valoracion = valoracion;
    }

    /**
     * Constructor mínimo solo con ID y nombre.
     */
    public Destino(int id_destino, String nombre) {
        this.id_destino = id_destino;
        this.nombre = nombre;
    }

    /**
     * Constructor para registro básico.
     */
    public Destino(String nombre, String descripcion, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public int getId_destino() {
        return id_destino;
    }

    public void setId_destino(int id_destino) {
        this.id_destino = id_destino;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    /**
     * Devuelve la fecha de creación como un {@link LocalDate}.
     * 
     * @return fecha de creación convertida a {@code LocalDate}, o {@code null} si no existe.
     */
    public LocalDate getFecha() {
        if (this.fecha_creacion == null) {
            return null;
        }
        return this.fecha_creacion.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    /**
     * Representación en texto del destino, usualmente usado para desplegar en ComboBox.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
