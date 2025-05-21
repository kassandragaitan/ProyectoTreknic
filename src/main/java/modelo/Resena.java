package modelo;

/**
 * Representa una reseña realizada por un usuario sobre un destino turístico,
 * incluyendo su comentario, puntuación y nombre del autor.
 *
 * Esta clase es utilizada para visualizar y gestionar opiniones dentro del
 * sistema Treknic.
 *
 * @author k0343
 */
public class Resena {

    /**
     * Identificador único de la reseña.
     */
    private int idResena;

    /**
     * Nombre del destino al que pertenece la reseña.
     */
    private String nombreDestino;

    /**
     * Comentario escrito por el usuario.
     */
    private String comentario;

    /**
     * Clasificación numérica dada al destino (por ejemplo, de 1 a 5).
     */
    private int clasificacion;

    /**
     * Nombre del usuario que realizó la reseña.
     */
    private String nombreUsuario;

    /**
     * Constructor principal de la clase Resena.
     *
     * @param idResena ID de la reseña
     * @param nombreDestino Nombre del destino reseñado
     * @param comentario Comentario escrito por el usuario
     * @param clasificacion Clasificación numérica (1 a 5)
     * @param nombreUsuario Nombre del autor de la reseña
     */
    public Resena(int idResena, String nombreDestino, String comentario, int clasificacion, String nombreUsuario) {
        this.idResena = idResena;
        this.nombreDestino = nombreDestino;
        this.comentario = comentario;
        this.clasificacion = clasificacion;
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene el ID de la reseña.
     *
     * @return ID único
     */
    public int getIdResena() {
        return idResena;
    }

    /**
     * Obtiene el nombre del destino reseñado.
     *
     * @return Nombre del destino
     */
    public String getNombreDestino() {
        return nombreDestino;
    }

    /**
     * Obtiene el comentario de la reseña.
     *
     * @return Texto del comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Obtiene la puntuación dada al destino.
     *
     * @return Valor de clasificación entre 1 y 5
     */
    public int getClasificacion() {
        return clasificacion;
    }

    /**
     * Obtiene el nombre del usuario que hizo la reseña.
     *
     * @return Nombre del autor de la reseña
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Devuelve una representación textual de la reseña.
     *
     * @return Cadena representativa de la reseña
     */
    @Override
    public String toString() {
        return "Destino: " + nombreDestino + " - Clasificación: " + clasificacion + "/5 - " + comentario;
    }
}
