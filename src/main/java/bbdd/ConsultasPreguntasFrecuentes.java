/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Clase que gestiona las operaciones sobre la tabla
 * {@code preguntas_frecuentes} en la base de datos. Actualmente permite
 * insertar nuevas preguntas y respuestas frecuentes relacionadas con el uso del
 * sistema.
 *
 * Forma parte del módulo de Soporte
 *
 * @author k0343
 */
public class ConsultasPreguntasFrecuentes {

    /**
     * Inserta una nueva pregunta frecuente junto con su respuesta en la base de
     * datos.
     *
     * @param pregunta Texto de la pregunta frecuente.
     * @param respuesta Respuesta correspondiente a la pregunta.
     * @return {@code true} si se insertó correctamente, {@code false} si
     * ocurrió un error.
     */
    public static boolean insertarPregunta(String pregunta, String respuesta) {
        String sql = "INSERT INTO preguntas_frecuentes (pregunta, respuesta) VALUES (?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pregunta);
            stmt.setString(2, respuesta);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
