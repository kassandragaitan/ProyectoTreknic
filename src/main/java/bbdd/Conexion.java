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
import modelo.Itinerario;
import modelo.ItinerarioTabla;
import modelo.Notificacion;
import modelo.Resena;
import modelo.TipoAlojamiento;
import modelo.UsuarioRegistro;

/**
 *
 * @author k0343
 */
public class Conexion {

    static Connection conn;
//     public static final String URL = "jdbc:mysql://145.14.151.1/u812167471_alojamientos2";

    public static final String URL = "jdbc:mysql://localhost/treknic";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection conectar() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
//            Class.forName("com.mysql.cj.jdbc.Driver");

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
            // identificador de usuario es 'nombre' o podría ser 'email' si se usa el correo electrónico para iniciar sesión
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

    public static boolean registrarUsuario(UsuarioRegistro usuarioRegistro) {
        conectar();
        try {
            String consulta = "INSERT INTO usuarios (nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, usuarioRegistro.getNombre());
            pst.setString(2, usuarioRegistro.getEmail());
            pst.setString(3, usuarioRegistro.getContrasena());
            pst.setString(4, usuarioRegistro.getTipoUsuario());
            pst.setString(5, usuarioRegistro.getIdioma());
            pst.setString(6, usuarioRegistro.getTipoViajero());
            pst.setString(7, usuarioRegistro.getTelefono());
            pst.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Establecer la fecha y hora actuales como fecha_registro
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarComboTipoUsuario(ComboBox comboTipoUsuario) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios LIKE 'tipo_usuario'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String enumValues = rs.getString("Type");
                enumValues = enumValues.substring(5, enumValues.length() - 1);
                String[] values = enumValues.split("','");
                for (String value : values) {
                    comboTipoUsuario.getItems().add(value.replace("'", ""));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboTipoCompania(ComboBox comboTipoCompania) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios LIKE 'tipo_viajero'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String tipoCompania = rs.getString("Type");
                tipoCompania = tipoCompania.substring(5, tipoCompania.length() - 1).replace("'", "");
                String[] valoresEnum = tipoCompania.split(",");
                for (String valor : valoresEnum) {
                    comboTipoCompania.getItems().add(valor);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarComboIdioma(ComboBox comboIdioma) {
        try {
            String consulta = "SHOW COLUMNS FROM usuarios WHERE Field = 'idioma_preferido'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            if (rs.next()) {
                String idiomaPreferido = rs.getString("Type");
                idiomaPreferido = idiomaPreferido.substring(5, idiomaPreferido.length() - 1).replace("'", "");
                String[] valoresEnum = idiomaPreferido.split(",");
                for (String valor : valoresEnum) {
                    comboIdioma.getItems().add(valor);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////
    public static boolean registrarItinerario(ItinerarioTabla itinerario) {
        conectar();
        try {
            String consulta = "INSERT INTO itinerario (nombre, descripcion, fecha_creacion, duracion, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, itinerario.getNombre());
            pst.setString(2, itinerario.getDescripcion());
            pst.setTimestamp(3, new java.sql.Timestamp(itinerario.getFechaCreacion().getTime()));
            pst.setInt(4, itinerario.getDuracion());
            pst.setInt(5, itinerario.getIdUsuario());

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosItinerariosFiltrados(ObservableList<ItinerarioTabla> listaItinerarios, String busqueda) {
        Connection conn = Conexion.conn;

        if (conn == null) {
            System.err.println("Error: La conexión a la base de datos no está abierta.");
            return;
        }

        String sql = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion "
                + "FROM itinerario "
                + "WHERE nombre LIKE ? OR descripcion LIKE ? OR CAST(duracion AS CHAR) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItinerarioTabla it = new ItinerarioTabla();
                    it.setIdItinerario(rs.getInt("id_itinerario"));
                    it.setNombre(rs.getString("nombre"));
                    it.setFechaCreacion(rs.getDate("fecha_creacion"));
                    it.setDescripcion(rs.getString("descripcion"));
                    it.setDuracion(rs.getInt("duracion"));

                    listaItinerarios.add(it);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar itinerarios filtrados:");
            e.printStackTrace();
        }
    }

    public static void cargarDatosItinerarios(ObservableList<ItinerarioTabla> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion, id_usuario FROM itinerario";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new ItinerarioTabla(
                            rs.getInt("id_itinerario"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_creacion"),
                            rs.getString("descripcion"),
                            mapEnumDurations(rs.getString("duracion")),
                            rs.getInt("id_usuario")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static int mapEnumDurations(String duration) {
        return duration.equals("3") ? 3 : duration.equals("5") ? 5 : duration.equals("7") ? 7 : 0;
    }

    public static void cargarComboDuracionItinerario(ComboBox comboDuracion) {
        try {
            String consulta = "SHOW COLUMNS FROM itinerario WHERE Field = 'duracion'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()) {
                String duracionStr = rs.getString("Type");
                duracionStr = duracionStr.substring(5, duracionStr.length() - 1).replace("'", "");
                String[] valoresEnum = duracionStr.split(",");
                for (String valor : valoresEnum) {
                    comboDuracion.getItems().add(Integer.parseInt(valor.trim())); // Convierte el valor a Integer y lo añade
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////
    public static void cargarDatosNotificaciones(ObservableList<Notificacion> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT n.id_notificacion, n.descripcion, n.fecha, n.notificacion, n.id_usuario, u.nombre as nombreDestinatario "
                    + "FROM notificaciones n "
                    + "JOIN usuarios u ON n.id_usuario = u.id_usuario";
            PreparedStatement pst = conn.prepareStatement(consultaCarga);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                listado.add(new Notificacion(
                        rs.getInt("id_notificacion"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha"),
                        rs.getString("notificacion"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombreDestinatario")
                ));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    ////////////////////////////////////////////
    public static boolean registrarActividad(Actividad actividad) {
        conectar();
        try {
            String consulta = "INSERT INTO actividades (nombre, descripcion, id_destino) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, actividad.getNombre());
            pst.setString(2, actividad.getDescripcion());
            pst.setInt(3, actividad.getIdDestino());

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarComboDestino(ComboBox<Destino> comboDestino) {
        conectar();
        try {
            String consulta = "SELECT id_destino, nombre FROM destinos";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                Destino destino = new Destino(rs.getInt("id_destino"), rs.getString("nombre"));
                comboDestino.getItems().add(destino);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosActividades(ObservableList<Actividad> listado) {
        try {
            String consultaCarga = "SELECT id_actividad, nombre, descripcion, id_destino FROM actividades";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new Actividad(
                            rs.getInt("id_actividad"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getInt("id_destino")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }
////////////////////////

    public static boolean registrarAlojamiento(Alojamiento alojamiento) {
        conectar();
        try {
            String consulta = "INSERT INTO alojamiento (nombre, id_tipo_fk, contacto, imagen, id_destino_fk) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, alojamiento.getNombre());
            pst.setInt(2, alojamiento.getIdTipo());
            pst.setString(3, alojamiento.getContacto());
            pst.setString(4, alojamiento.getImagen());
            pst.setInt(5, alojamiento.getIdDestino());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarComboTipoAlojamiento(ComboBox<TipoAlojamiento> comboTipo) {
        conectar();
        try {
            String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosAlojamientos(ObservableList<Alojamiento> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_alojamiento, nombre, id_tipo_fk as id_tipo, contacto, imagen, id_destino_fk as id_destino FROM alojamiento";

            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    System.out.println("Loading: " + rs.getString("nombre"));
                    listado.add(new Alojamiento(
                            rs.getInt("id_alojamiento"),
                            rs.getString("nombre"),
                            rs.getInt("id_tipo"),
                            rs.getString("contacto"),
                            rs.getString("imagen"),
                            rs.getInt("id_destino")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    //////////////////////////////
    public static boolean registrarTipoAlojamiento(TipoAlojamiento tipo) {
        conectar();
        try {
            String consulta = "INSERT INTO tipoalojamiento (tipo) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, tipo.getTipo());

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosTiposAlojamientoRegistrar(ComboBox<TipoAlojamiento> comboTipo) {
        conectar();
        try {
            String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaCarga);
            while (rs.next()) {
                TipoAlojamiento tipo = new TipoAlojamiento(rs.getInt("id_tipo"), rs.getString("tipo"));
                comboTipo.getItems().add(tipo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosTiposAlojamiento(ObservableList<TipoAlojamiento> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_tipo, tipo FROM tipoalojamiento";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new TipoAlojamiento(
                            rs.getInt("id_tipo"),
                            rs.getString("tipo")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    ///////////////////////////////
    public static boolean registrarCategoria(Categoria categoria) {
        conectar();
        try {
            String consulta = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, categoria.getNombre());
            pst.setString(2, categoria.getDescripcion());
            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static void cargarDatosCategorias(ObservableList<Categoria> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_categoria, nombre, descripcion FROM categoria";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

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

    public static void cargarDatosDestinos(ObservableList<Destino> listado) {
        conectar();
        try {
            // Asumiendo que el número de 'visitas' es el conteo de reseñas para cada destino.
            String consulta = "SELECT d.id_destino, d.nombre, d.descripcion, d.fecha_creacion, d.imagen, "
                    + "COUNT(r.id_resena) as visitas, "
                    + "COALESCE(AVG(r.clasificacion), 0) as valoracion "
                    + "FROM destinos d "
                    + "LEFT JOIN resenas r ON d.id_destino = r.id_destino "
                    + "GROUP BY d.id_destino";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {
                while (rs.next()) {
                    listado.add(new Destino(
                            rs.getInt("id_destino"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_creacion"),
                            rs.getInt("visitas"),
                            rs.getDouble("valoracion")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion();
        }
    }

    public static int contar(String tabla) {
        int total = 0;
        conectar(); // Asegúrate de que la conexión se abre
        String query = "SELECT COUNT(*) FROM " + tabla;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cerrarConexion(); // Asegúrate de cerrar la conexión
        }
        return total;
    }

}
