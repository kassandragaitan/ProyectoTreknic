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
 * Clase encargada de gestionar la conexión con la base de datos MySQL. Contiene
 * métodos para conectar, cerrar conexión, realizar conteos y cargar informes
 * específicos usados en estadísticas y reportes.
 *
 * Uso típico en toda la aplicación para operaciones simples y centralizadas.
 *
 * @author k0343
 */
public class Conexion {
//
//    static Connection conn;
//    public static final String URL = "jdbc:mysql://145.14.151.1/u812167471_kassandra";
//    public static final String USERNAME = "u812167471_kassandra";
//    public static final String PASSWORD = "2025-Kassandra";

    /**
     * Objeto de conexión a la base de datos.
     */
    static Connection conn;

    /**
     * URL de conexión a la base de datos local o remota.
     */
    public static final String URL = "jdbc:mysql://localhost:3306/Treknic";

    /**
     * Usuario de la base de datos.
     */
    public static final String USERNAME = "root";

    /**
     * Contraseña del usuario de la base de datos.
     */
    public static final String PASSWORD = "";

    /**
     * Establece la conexión con la base de datos utilizando los parámetros
     * definidos.
     *
     * @return Objeto {@link Connection} si la conexión es exitosa, o null si
     * falla.
     */
    public static Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    /**
     * Cierra la conexión actual a la base de datos.
     */
    public static void cerrarConexion() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Realiza un conteo total de registros en una tabla específica.
     *
     * @param tabla Nombre de la tabla a contar.
     * @return Número total de registros encontrados.
     */
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

    /**
     * Carga los cinco destinos con menor cantidad de actividades registradas.
     * Usado normalmente para gráficas de análisis o reportes.
     *
     * @return Lista observable con objetos {@link InformeActividadDestino}.
     */
    public static ObservableList<InformeActividadDestino> cargarActividadesPorDestino() {
        ObservableList<InformeActividadDestino> listado = FXCollections.observableArrayList();
        String consulta = "SELECT d.nombre AS DESTINO, COUNT(a.id_actividad) AS ACTIVIDADES "
                + "FROM actividades a "
                + "JOIN destinos d ON a.id_destino = d.id_destino "
                + "GROUP BY d.nombre "
                + "ORDER BY ACTIVIDADES ASC "
                + "LIMIT 5";
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
