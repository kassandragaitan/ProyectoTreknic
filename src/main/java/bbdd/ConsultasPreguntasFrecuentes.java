/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author k0343
 */
public class ConsultasPreguntasFrecuentes {
    
    
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
