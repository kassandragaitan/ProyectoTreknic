/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Actividad;
import modelo.Alojamiento;
import modelo.Categoria;
import modelo.Destino;
import modelo.InformeActividadDestino;
import modelo.Itinerario;

import modelo.Notificacion;
import modelo.Resena;
import modelo.TipoAlojamiento;
import modelo.Usuario;

/**
 *
 * @author k0343
 */
public class Conexion {

    static Connection conn;
    public static final String URL = "jdbc:mysql://localhost/treknic";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static void cerrarConexion() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean acceder(String user, String pass) {
        try {
            String consulta = "SELECT * FROM usuarios WHERE nombre = ? AND contrasena = ?";

            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static int contar(String tabla) {
        int total = 0;
        conectar();
        String query = "SELECT COUNT(*) FROM " + tabla;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
        return total;
    }

    /*Grafico de principal*/
    public static ObservableList<InformeActividadDestino> cargarActividadesPorDestino() {
        ObservableList<InformeActividadDestino> listado = FXCollections.observableArrayList();
        String consulta = "SELECT d.nombre AS DESTINO, COUNT(a.id_actividad) AS ACTIVIDADES "
                + "FROM actividades a "
                + "JOIN destinos d ON a.id_destino = d.id_destino "
                + "GROUP BY d.nombre;";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while (rs.next()) {
                listado.add(new InformeActividadDestino(
                        rs.getString("DESTINO"),
                        rs.getInt("ACTIVIDADES")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listado;
    }

}
