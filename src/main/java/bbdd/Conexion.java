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

    public static boolean registrarUsuario(Usuario usuario) {
        conectar();
        try {
            String consulta = "INSERT INTO usuarios (nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(consulta);
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getContrasena());
            pst.setString(4, usuario.getTipoUsuario());
            pst.setString(5, usuario.getIdioma());
            pst.setString(6, usuario.getTipoViajero());
            pst.setString(7, usuario.getTelefono());
            pst.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Fecha actual
            pst.setBoolean(9, usuario.getActivo()); // ✅ nuevo campo "activo"

            int resultado = pst.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            cerrarConexion();
        }
    }

    public static ObservableList<String> cargarRolesUsuarios() {
        ObservableList<String> roles = FXCollections.observableArrayList();
        roles.add("Todos los roles"); // Agregamos esta opción por defecto

        String consulta = "SELECT DISTINCT tipo_usuario FROM usuarios";

        try {
            PreparedStatement st = conn.prepareStatement(consulta);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                roles.add(rs.getString("tipo_usuario"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return roles;
    }

    public static ObservableList<String> cargarEstadosUsuarios() {
        ObservableList<String> estados = FXCollections.observableArrayList();
        estados.add("Todos los estados"); // Opción por defecto

        String consulta = "SELECT DISTINCT activo FROM usuarios";

        try {
            PreparedStatement st = conn.prepareStatement(consulta);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                boolean estadoBD = rs.getBoolean("activo");
                String estado = estadoBD ? "Activo" : "Inactivo";
                if (!estados.contains(estado)) {
                    estados.add(estado);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return estados;
    }

    public static void cargarUsuariosPorRol(ObservableList<Usuario> listaUsuarios, String rolSeleccionado) {
        String consulta;

        if (rolSeleccionado.equals("Todos los roles")) {
            consulta = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo FROM usuarios";
        } else {
            consulta = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo FROM usuarios WHERE tipo_usuario = ?";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(consulta);

            if (!rolSeleccionado.equals("Todos los roles")) {
                stmt.setString(1, rolSeleccionado);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("tipo_usuario"),
                        rs.getDate("fecha_registro"),
                        rs.getString("tipo_viajero"),
                        rs.getString("idioma_preferido"),
                        rs.getString("telefono")
                );
                usuario.setActivo(rs.getBoolean("activo"));
                listaUsuarios.add(usuario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarUsuariosPorEstado(ObservableList<Usuario> listaUsuarios, String estadoSeleccionado) {
        String consulta;

        if (estadoSeleccionado.equals("Todos los estados")) {
            consulta = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo FROM usuarios";
        } else {
            consulta = "SELECT id_usuario, nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro, activo FROM usuarios WHERE activo = ?";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(consulta);

            if (!estadoSeleccionado.equals("Todos los estados")) {
                boolean valorEstado = estadoSeleccionado.equals("Activo");
                stmt.setBoolean(1, valorEstado);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("tipo_usuario"),
                        rs.getDate("fecha_registro"),
                        rs.getString("tipo_viajero"),
                        rs.getString("idioma_preferido"),
                        rs.getString("telefono")
                );
                usuario.setActivo(rs.getBoolean("activo"));
                listaUsuarios.add(usuario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarDatosUsuariosFiltrados(ObservableList<Usuario> listaUsuarios, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_usuario, nombre, email, tipo_usuario, idioma_preferido, tipo_viajero, telefono, activo "
                + "FROM usuarios "
                + "WHERE nombre LIKE ? OR email LIKE ? OR tipo_usuario LIKE ? OR idioma_preferido LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, wildcard);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                    usuario.setIdioma(rs.getString("idioma_preferido"));
                    usuario.setTipoViajero(rs.getString("tipo_viajero"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setActivo(rs.getBoolean("activo"));

                    listaUsuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar usuarios filtrados:");
            e.printStackTrace();
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
    public static boolean registrarItinerario(Itinerario itinerario) {
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

    public static ObservableList<String> cargarDuracionesItinerarios() {
        ObservableList<String> duraciones = FXCollections.observableArrayList();
        duraciones.add("Todas las duraciones"); // Opción por defecto

        String consulta = "SELECT DISTINCT duracion FROM itinerario ORDER BY duracion";

        try {
            PreparedStatement st = conn.prepareStatement(consulta);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                duraciones.add(rs.getString("duracion"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return duraciones;
    }

    public static void cargarItinerariosPorDuracion(ObservableList<Itinerario> listaItinerarios, String duracionSeleccionada) {
        String consulta;

        if (duracionSeleccionada.equals("Todas las duraciones")) {
            consulta = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion FROM itinerario";
        } else {
            consulta = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion FROM itinerario WHERE duracion = ?";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(consulta);

            if (!duracionSeleccionada.equals("Todas las duraciones")) {
                stmt.setString(1, duracionSeleccionada);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Itinerario itinerario = new Itinerario(
                        rs.getInt("id_itinerario"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_creacion"), // <-- ESTE ES Date
                        rs.getString("descripcion"),
                        rs.getInt("duracion")
                );

                listaItinerarios.add(itinerario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarDatosItinerariosFiltrados(ObservableList<Itinerario> listaItinerarios, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion "
                + "FROM itinerario "
                + "WHERE nombre LIKE ? OR descripcion LIKE ? OR CAST(duracion AS CHAR) LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Itinerario it = new Itinerario();
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

    public static void cargarDatosItinerarios(ObservableList<Itinerario> listado) {
        conectar();
        try {
            String consultaCarga = "SELECT id_itinerario, nombre, fecha_creacion, descripcion, duracion, id_usuario FROM itinerario";
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consultaCarga)) {
                while (rs.next()) {
                    listado.add(new Itinerario(
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

    public static void cargarDatosActividadesFiltradas(ObservableList<Actividad> listaActividades, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_actividad, nombre, descripcion, id_destino "
                + "FROM actividades "
                + "WHERE nombre LIKE ? OR descripcion LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String comodin = "%" + busqueda + "%";
            ps.setString(1, comodin);
            ps.setString(2, comodin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Actividad act = new Actividad();
                    act.setIdActividad(rs.getInt("id_actividad"));
                    act.setNombre(rs.getString("nombre"));
                    act.setDescripcion(rs.getString("descripcion"));
                    act.setIdDestino(rs.getInt("id_destino"));

                    listaActividades.add(act);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar actividades filtradas:");
            e.printStackTrace();
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

    public static void cargarDatosAlojamientosFiltrados(ObservableList<Alojamiento> listaAlojamientos, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_alojamiento, nombre, id_tipo_fk, contacto, imagen, id_destino_fk "
                + "FROM alojamiento "
                + "WHERE nombre LIKE ? OR contacto LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Alojamiento al = new Alojamiento();
                    al.setIdAlojamiento(rs.getInt("id_alojamiento"));
                    al.setNombre(rs.getString("nombre"));
                    al.setIdTipo(rs.getInt("id_tipo_fk"));
                    al.setContacto(rs.getString("contacto"));
                    al.setImagen(rs.getString("imagen"));
                    al.setIdDestino(rs.getInt("id_destino_fk"));

                    listaAlojamientos.add(al);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alojamientos filtrados:");
            e.printStackTrace();
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

    public static void cargarDatosTiposAlojamientoFiltrados(ObservableList<TipoAlojamiento> listaTipos, String busqueda) {
        Connection conn = Conexion.conn;

        String consulta = "SELECT id_tipo, tipo FROM tipoalojamiento WHERE tipo LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoAlojamiento tipoAloj = new TipoAlojamiento();
                    tipoAloj.setIdTipo(rs.getInt("id_tipo"));
                    tipoAloj.setTipo(rs.getString("tipo"));

                    listaTipos.add(tipoAloj);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar tipos de alojamiento filtrados:");
            e.printStackTrace();
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

    public static void cargarDatosCategoriasFiltradas(ObservableList<Categoria> listaCategorias, String busqueda) {
        Connection conn = Conexion.conn;
        String consulta = "SELECT id_categoria, nombre, descripcion FROM categoria WHERE nombre LIKE ? OR descripcion LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("id_categoria"));
                    cat.setNombre(rs.getString("nombre"));
                    cat.setDescripcion(rs.getString("descripcion"));

                    listaCategorias.add(cat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar categorías filtradas:");
            e.printStackTrace();
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
