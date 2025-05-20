package modelo;
import java.util.Date;

public class Notificacion {

    private int idNotificacion;
    private String descripcion;
    private Date fecha;
    private String nombreUsuario;
    private boolean leido;

    public Notificacion(int idNotificacion, String descripcion, Date fecha, String nombreUsuario, boolean leido) {
        this.idNotificacion = idNotificacion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.nombreUsuario = nombreUsuario;
        this.leido = leido;
    }


    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}
