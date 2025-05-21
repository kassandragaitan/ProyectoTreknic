package modelo;

/**
 * Representa una sugerencia enviada por un usuario dentro del sistema. Incluye
 * el título, mensaje, fecha de envío y datos del usuario remitente.
 *
 * Esta clase se utiliza principalmente para mostrar y gestionar sugerencias en
 * el módulo de soporte.
 *
 * @author k0343
 */
public class Sugerencia {

    /**
     * Identificador único de la sugerencia.
     */
    private int id;

    /**
     * Identificador del usuario que envió la sugerencia.
     */
    private int idUsuario;

    /**
     * Título breve de la sugerencia.
     */
    private String titulo;

    /**
     * Mensaje o contenido de la sugerencia.
     */
    private String mensaje;

    /**
     * Fecha en la que se envió la sugerencia (formato YYYY-MM-DD).
     */
    private String fechaEnvio;

    /**
     * Constructor básico de sugerencia, sin información del usuario.
     *
     * @param id ID de la sugerencia
     * @param titulo Título de la sugerencia
     * @param mensaje Contenido de la sugerencia
     * @param fechaEnvio Fecha de envío
     */
    public Sugerencia(int id, String titulo, String mensaje, String fechaEnvio) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Constructor completo con ID de usuario.
     *
     * @param id ID de la sugerencia
     * @param idUsuario ID del usuario remitente
     * @param titulo Título de la sugerencia
     * @param mensaje Contenido de la sugerencia
     * @param fechaEnvio Fecha de envío
     */
    public Sugerencia(int id, int idUsuario, String titulo, String mensaje, String fechaEnvio) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Obtiene el ID de la sugerencia.
     *
     * @return ID entero
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la sugerencia.
     *
     * @param id Nuevo ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID del usuario que envió la sugerencia.
     *
     * @return ID del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario que envió la sugerencia.
     *
     * @param idUsuario ID del usuario
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el título de la sugerencia.
     *
     * @return Título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la sugerencia.
     *
     * @param titulo Nuevo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el mensaje o cuerpo de la sugerencia.
     *
     * @return Texto del mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje o cuerpo de la sugerencia.
     *
     * @param mensaje Texto del mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene la fecha de envío de la sugerencia en formato corto (YYYY-MM-DD).
     *
     * @return Fecha como cadena
     */
    public String getFechaEnvio() {
        return fechaEnvio != null && fechaEnvio.length() >= 10 ? fechaEnvio.substring(0, 10) : fechaEnvio;
    }

    /**
     * Establece la fecha de envío de la sugerencia.
     *
     * @param fechaEnvio Fecha como cadena
     */
    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
