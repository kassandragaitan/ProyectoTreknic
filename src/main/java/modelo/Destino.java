package modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Destino {

    private int id_destino;
    private String nombre;
    private String descripcion;
    private String categoria;
    private Date fecha_creacion; // original desde BBDD
    private String imagen;

    private int visitas;
    private double valoracion;

    public Destino() {}

    public Destino(int id_destino, String nombre, String descripcion, String categoria, Date fecha_creacion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fecha_creacion = fecha_creacion;
    }

    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, String imagen) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.imagen = imagen;
    }

    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, String imagen, int visitas, double valoracion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.imagen = imagen;
        this.visitas = visitas;
        this.valoracion = valoracion;
    }

    public Destino(int id_destino, String nombre, String descripcion, Date fecha_creacion, int visitas, double valoracion) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.visitas = visitas;
        this.valoracion = valoracion;
    }

    public Destino(int id_destino, String nombre) {
        this.id_destino = id_destino;
        this.nombre = nombre;
    }

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

    // Conversión segura para LocalDate (para filtros)
    public LocalDate getFecha() {
        if (this.fecha_creacion == null) return null;
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

    @Override
    public String toString() {
        return nombre;
    }
}
