package modelo;

import java.util.Date;

/**
 * Representa a un usuario del sistema, incluyendo sus datos personales, de
 * acceso, preferencias y rol dentro de la plataforma.
 *
 * También gestiona una instancia estática del "usuario actual" para control de
 * sesión o contexto activo.
 *
 * @author k0343
 */
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    private int idUsuario;

    /**
     * Nombre completo del usuario.
     */
    private String nombre;

    /**
     * Correo electrónico asociado al usuario (también usado como login).
     */
    private String email;

    /**
     * Contraseña del usuario.
     */
    private String contrasena;

    /**
     * Rol del usuario (ej. Administrador, Editor, Visitante).
     */
    private String tipoUsuario;

    /**
     * Fecha en la que el usuario se registró.
     */
    private Date fechaRegistro;

    /**
     * Preferencia del tipo de viajero (Ej: Aventura, Familiar, Cultural).
     */
    private String tipoViajero;

    /**
     * Idioma preferido del usuario.
     */
    private String idioma;

    /**
     * Número de teléfono del usuario.
     */
    private String telefono;

    /**
     * Instancia estática del usuario actual activo en sesión.
     */
    private static Usuario usuarioActual;

    /**
     * Constructor vacío requerido para instanciación dinámica.
     */
    public Usuario() {
    }

    /**
     * Constructor simplificado solo con ID y nombre.
     *
     * @param id_usuario ID del usuario
     * @param nombre Nombre del usuario
     */
    public Usuario(int id_usuario, String nombre) {
        this.idUsuario = id_usuario;
        this.nombre = nombre;
    }

    /**
     * Constructor sin ID (para registro).
     */
    public Usuario(String nombre, String email, String contrasena, String tipoUsuario,
            Date fechaRegistro, String tipoViajero, String idioma, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
        this.fechaRegistro = fechaRegistro;
        this.tipoViajero = tipoViajero;
        this.idioma = idioma;
        this.telefono = telefono;
    }

    /**
     * Constructor completo con todos los campos.
     */
    public Usuario(int idUsuario, String nombre, String email, String contrasena,
            String tipoUsuario, Date fechaRegistro, String tipoViajero,
            String idioma, String telefono) {
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

    // Getters y Setters
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

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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

    /**
     * Obtiene la instancia del usuario actualmente logueado en el sistema.
     *
     * @return Usuario activo
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Establece el usuario como activo en sesión.
     *
     * @param usuario Usuario a establecer como actual
     */
    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    /**
     * Devuelve el nombre del usuario como representación por defecto.
     *
     * @return Nombre del usuario
     */
    @Override
    public String toString() {
        return nombre;
    }
}
