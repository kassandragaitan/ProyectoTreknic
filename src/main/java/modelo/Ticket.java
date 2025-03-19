/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author k0343
 */
public class Ticket {
     private StringProperty id;
    private StringProperty asunto;
    private StringProperty estado;
    private StringProperty prioridad;
    private StringProperty creado;
    private StringProperty ultimaActualizacion;

    public Ticket(String id, String asunto, String estado, String prioridad, String creado, String ultimaActualizacion) {
        this.id = new SimpleStringProperty(id);
        this.asunto = new SimpleStringProperty(asunto);
        this.estado = new SimpleStringProperty(estado);
        this.prioridad = new SimpleStringProperty(prioridad);
        this.creado = new SimpleStringProperty(creado);
        this.ultimaActualizacion = new SimpleStringProperty(ultimaActualizacion);
    }

    // Getters
    public String getId() {
        return id.get();
    }

    public String getAsunto() {
        return asunto.get();
    }

    public String getEstado() {
        return estado.get();
    }

    public String getPrioridad() {
        return prioridad.get();
    }

    public String getCreado() {
        return creado.get();
    }

    public String getUltimaActualizacion() {
        return ultimaActualizacion.get();
    }

    // Setters
    public void setId(String value) {
        id.set(value);
    }

    public void setAsunto(String value) {
        asunto.set(value);
    }

    public void setEstado(String value) {
        estado.set(value);
    }

    public void setPrioridad(String value) {
        prioridad.set(value);
    }

    public void setCreado(String value) {
        creado.set(value);
    }

    public void setUltimaActualizacion(String value) {
        ultimaActualizacion.set(value);
    }

    // Property accessors
    public StringProperty idProperty() {
        return id;
    }

    public StringProperty asuntoProperty() {
        return asunto;
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public StringProperty prioridadProperty() {
        return prioridad;
    }

    public StringProperty creadoProperty() {
        return creado;
    }

    public StringProperty ultimaActualizacionProperty() {
        return ultimaActualizacion;
    }
}
