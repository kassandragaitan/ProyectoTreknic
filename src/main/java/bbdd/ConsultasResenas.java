/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import static bbdd.Conexion.cerrarConexion;
import static bbdd.Conexion.conectar;
import static bbdd.Conexion.conn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Resena;

/**
 *
 * @author k0343
 */
public class ConsultasResenas {

    public static List<Resena> obtenerResenas() {
        List<Resena> resenas = new ArrayList<>();
        conectar();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT r.id_resena, d.nombre as destino_nombre, r.comentario, r.clasificacion, u.nombre as usuario_nombre FROM resenas r JOIN destinos d ON r.id_destino = d.id_destino JOIN usuarios u ON r.id_usuario = u.id_usuario")) {
            while (rs.next()) {
                resenas.add(new Resena(
                        rs.getInt("id_resena"),
                        rs.getString("destino_nombre"),
                        rs.getString("comentario"),
                        rs.getInt("clasificacion"),
                        rs.getString("usuario_nombre")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return resenas;
    }
}
