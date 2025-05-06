/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author k0343
 */
public class PreguntaFrecuente {

    private final StringProperty pregunta;
    private final StringProperty respuesta;

    public PreguntaFrecuente(String pregunta, String respuesta) {
        this.pregunta = new SimpleStringProperty(pregunta);
        this.respuesta = new SimpleStringProperty(respuesta);
    }

    public String getPregunta() {
        return pregunta.get();
    }

    public void setPregunta(String pregunta) {
        this.pregunta.set(pregunta);
    }

    public StringProperty preguntaProperty() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta.get();
    }

    public void setRespuesta(String respuesta) {
        this.respuesta.set(respuesta);
    }

    public StringProperty respuestaProperty() {
        return respuesta;
    }
}