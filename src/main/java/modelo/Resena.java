/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
public class Resena {

    private int idResena;
    private String nombreDestino;
    private String comentario;
    private int clasificacion;
    private String nombreUsuario;

    public Resena(int idResena, String nombreDestino, String comentario, int clasificacion, String nombreUsuario) {
        this.idResena = idResena;
        this.nombreDestino = nombreDestino;
        this.comentario = comentario;
        this.clasificacion = clasificacion;
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdResena() {
        return idResena;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public String getComentario() {
        return comentario;
    }

    public int getClasificacion() {
        return clasificacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    @Override
    public String toString() {
        return "Destino: " + nombreDestino + " - Clasificación: " + clasificacion + "/5 - " + comentario;
    }

}
