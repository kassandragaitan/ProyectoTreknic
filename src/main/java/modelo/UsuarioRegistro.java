/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;

/**
 *
 * @author k0343
 */
public class UsuarioRegistro {

    private int idUsuario;
    private String nombre;
    private String email;
    private String contrasena;
    private String tipoUsuario;
    private Date fechaRegistro;
    private String tipoViajero;
    private String idioma;
    private String telefono;

    public UsuarioRegistro() {
    }

    public UsuarioRegistro(int idUsuario, String nombre, String email, String contrasena, String tipoUsuario, Date fechaRegistro, String tipoViajero, String idioma, String telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
        this.fechaRegistro = fechaRegistro;
        this.tipoViajero = tipoViajero;
        this.idioma = idioma;
        this.telefono = telefono;
    }

    public UsuarioRegistro(String nombre, String email, String contrasena, String tipoUsuario, Date fechaRegistro, String tipoViajero, String idioma, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
        this.fechaRegistro = fechaRegistro;
        this.tipoViajero = tipoViajero;
        this.idioma = idioma;
        this.telefono = telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipoViajero() {
        return tipoViajero;
    }

    public void setTipoViajero(String tipoViajero) {
        this.tipoViajero = tipoViajero;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

}
