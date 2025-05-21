package bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import modelo.Usuario;

/**
 * Clase que gestiona todas las operaciones relacionadas con los usuarios en la
 * base de datos. Incluye funciones para registrar, actualizar, buscar y filtrar
 * usuarios, así como cargar valores de enums para ComboBox.
 *
 * Utiliza la conexión proporcionada por {@link Conexion}.
 *
 * @author k0343
 */
public class ConsultasUsuario {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto {@link Usuario} con los datos del nuevo usuario.
     * @return {@code true} si el registro fue exitoso, {@code false} en caso de
     * error.
     */
    public static boolean registrarUsuario(Usuario usuario) {
        String consulta = "INSERT INTO usuarios (nombre, email, contrasena, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getContrasena());
            pst.setString(4, usuario.getTipoUsuario());
            pst.setString(5, usuario.getIdioma());
            pst.setString(6, usuario.getTipoViajero());
            pst.setString(7, usuario.getTelefono());
            pst.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param usuario Objeto {@link Usuario} con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} en
     * caso de error.
     */
    public static boolean actualizarUsuario(Usuario usuario) {
        String consulta = "UPDATE usuarios SET nombre = ?, email = ?, contrasena = ?, tipo_usuario = ?, idioma_preferido = ?, tipo_viajero = ?, telefono = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getContrasena());
            pst.setString(4, usuario.getTipoUsuario());
            pst.setString(5, usuario.getIdioma());
            pst.setString(6, usuario.getTipoViajero());
            pst.setString(7, usuario.getTelefono());
            pst.setInt(8, usuario.getIdUsuario());

            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Verifica si ya existe un usuario con el email especificado.
     *
     * @param email Email a verificar.
     * @return {@code true} si existe, {@code false} si no.
     */
    public static boolean existeEmail(String email) {
        String consulta = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(consulta)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            Logger.getLogger(ConsultasUsuario.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Carga los diferentes roles de usuario disponibles.
     *
     * @return Lista observable con los valores distintos del campo
     * {@code tipo_usuario}.
     */
    public static ObservableList<String> cargarRolesUsuarios() {
        ObservableList<String> roles = FXCollections.observableArrayList();
        String consulta = "SELECT DISTINCT tipo_usuario FROM usuarios";

        try (Connection conn = Conexion.conectar(); PreparedStatement st = conn.prepareStatement(consulta); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                roles.add(rs.getString("tipo_usuario"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return roles;
    }

    /**
     * Carga usuarios que pertenecen a un rol específico.
     *
     * @param listaUsuarios Lista donde se almacenarán los usuarios encontrados.
     * @param rolSeleccionado Rol seleccionado para el filtro.
     */
    public static void cargarUsuariosPorRol(ObservableList<Usuario> listaUsuarios, String rolSeleccionado) {
        String consulta = rolSeleccionado.equals("Todos los roles")
                ? "SELECT * FROM usuarios"
                : "SELECT * FROM usuarios WHERE tipo_usuario = ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement stmt = conn.prepareStatement(consulta)) {
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
                listaUsuarios.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga usuarios por un campo específico (ej. tipo_usuario,
     * idioma_preferido).
     *
     * @param listaUsuarios Lista donde se almacenarán los resultados.
     * @param campo Nombre del campo a filtrar.
     * @param valor Valor exacto del campo.
     */
    public static void cargarUsuariosPorCampo(ObservableList<Usuario> listaUsuarios, String campo, String valor) {
        String consulta = "SELECT * FROM usuarios WHERE " + campo + " = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            ps.setString(1, valor);
            ResultSet rs = ps.executeQuery();
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
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga usuarios cuyos datos coinciden parcialmente con una cadena de
     * búsqueda.
     *
     * @param listaUsuarios Lista donde se añadirán los resultados.
     * @param busqueda Texto parcial a buscar.
     */
    public static void cargarDatosUsuariosFiltrados(ObservableList<Usuario> listaUsuarios, String busqueda) {
        String consulta = "SELECT id_usuario, nombre, email, tipo_usuario, idioma_preferido, tipo_viajero, telefono, fecha_registro "
                + "FROM usuarios "
                + "WHERE nombre LIKE ? OR email LIKE ? OR tipo_usuario LIKE ? OR idioma_preferido LIKE ? "
                + "OR tipo_viajero LIKE ? OR telefono LIKE ? OR DATE_FORMAT(fecha_registro, '%Y-%m-%d') LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(consulta)) {
            String wildcard = "%" + busqueda + "%";
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, wildcard);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                usuario.setIdioma(rs.getString("idioma_preferido"));
                usuario.setTipoViajero(rs.getString("tipo_viajero"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setFechaRegistro(rs.getDate("fecha_registro"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar usuarios filtrados:");
            e.printStackTrace();
        }
    }

    /**
     * Carga los idiomas definidos en el enum {@code idioma_preferido} de la
     * tabla {@code usuarios}.
     *
     * @return Lista de idiomas.
     */
    public static ObservableList<String> cargarIdiomasDisponibles() {
        ObservableList<String> idiomas = FXCollections.observableArrayList();
        String consulta = "SHOW COLUMNS FROM usuarios WHERE Field = 'idioma_preferido'";

        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {

            if (rs.next()) {
                String values = rs.getString("Type").substring(5, rs.getString("Type").length() - 1).replace("'", "");
                for (String valor : values.split(",")) {
                    idiomas.add(valor.trim());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idiomas;
    }

    /**
     * Carga los tipos de viajero definidos en el enum {@code tipo_viajero} de
     * la tabla {@code usuarios}.
     *
     * @return Lista de tipos de compañía.
     */
    public static ObservableList<String> cargarTiposDeCompania() {
        ObservableList<String> tipos = FXCollections.observableArrayList();
        String consulta = "SHOW COLUMNS FROM usuarios WHERE Field = 'tipo_viajero'";

        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(consulta)) {

            if (rs.next()) {
                String values = rs.getString("Type").substring(5, rs.getString("Type").length() - 1).replace("'", "");
                for (String valor : values.split(",")) {
                    tipos.add(valor.trim());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tipos;
    }

    /**
     * Llena un {@link ComboBox} con los tipos de usuario disponibles.
     *
     * @param comboTipoUsuario ComboBox a llenar.
     */
    public static void cargarComboTipoUsuario(ComboBox<String> comboTipoUsuario) {
        comboTipoUsuario.getItems().clear();
        comboTipoUsuario.getItems().add("Seleccione");
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SHOW COLUMNS FROM usuarios LIKE 'tipo_usuario'")) {
            if (rs.next()) {
                String enumValues = rs.getString("Type").substring(5, rs.getString("Type").length() - 1).replace("'", "");
                for (String value : enumValues.split(",")) {
                    comboTipoUsuario.getItems().add(value.trim());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboTipoUsuario.getSelectionModel().selectFirst();
    }

    /**
     * Llena un {@link ComboBox} con los tipos de viajero disponibles.
     *
     * @param comboTipoCompania ComboBox a llenar.
     */
    public static void cargarComboTipoCompania(ComboBox<String> comboTipoCompania) {
        comboTipoCompania.getItems().clear();
        comboTipoCompania.getItems().add("Seleccione");
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SHOW COLUMNS FROM usuarios LIKE 'tipo_viajero'")) {
            if (rs.next()) {
                String values = rs.getString("Type").substring(5, rs.getString("Type").length() - 1).replace("'", "");
                for (String valor : values.split(",")) {
                    comboTipoCompania.getItems().add(valor.trim());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboTipoCompania.getSelectionModel().selectFirst();
    }

    /**
     * Llena un {@link ComboBox} con los idiomas preferidos disponibles.
     *
     * @param comboIdioma ComboBox a llenar.
     */
    public static void cargarComboIdioma(ComboBox<String> comboIdioma) {
        comboIdioma.getItems().clear();
        comboIdioma.getItems().add("Seleccione");
        try (Connection conn = Conexion.conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SHOW COLUMNS FROM usuarios WHERE Field = 'idioma_preferido'")) {
            if (rs.next()) {
                String values = rs.getString("Type").substring(5, rs.getString("Type").length() - 1).replace("'", "");
                for (String valor : values.split(",")) {
                    comboIdioma.getItems().add(valor.trim());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        comboIdioma.getSelectionModel().selectFirst();
    }

}
