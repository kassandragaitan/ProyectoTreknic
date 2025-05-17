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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.InformeActividadDestino;

/**
 *
 * @author k0343
 */
public class Conexion {

//    static Connection conn;
//    public static final String URL = "jdbc:mysql://145.14.151.1/u812167471_kassandra";
//    public static final String USERNAME = "u812167471_kassandra";
//    public static final String PASSWORD = "2025-Kassandra";
    
    public static Connection conn;
    public static final String URL = "jdbc:mysql://localhost:3306/remota_kassandra";
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

    public static int contarPorMes(String tabla, String campoFecha, int mes, int anio) {
        conectar();
        int cantidad = 0;
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE MONTH(" + campoFecha + ") = ? AND YEAR(" + campoFecha + ") = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, mes);
            pst.setInt(2, anio);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt(1);
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            System.err.println("Error al contar por mes: " + e.getMessage());
        }

        cerrarConexion();
        return cantidad;
    }

}
