/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import Utilidades.Alertas;
import bbdd.ConsultasConfiguracion;
import bbdd.ConsultasLogin;
import java.util.Arrays;

/**
 *
 * @author k0343
 */
public class SeguridadLogin {
     public static boolean validarAcceso(String email, String password, String ipUsuario) {
        ConfiguracionSistema config = ConsultasConfiguracion.obtenerConfiguracion();

        // Validar IP
        if (!estaIpPermitida(config.getListaIPs(), ipUsuario)) {
            Alertas.error("Acceso Denegado", "Tu IP no está permitida para acceder.");
            return false;
        }

        // Consultar intentos
        int intentosFallidos = ConsultasLogin.obtenerIntentosFallidos(email);
        if (intentosFallidos >= config.getIntentosFallidosMax()) {
            Alertas.error("Cuenta Bloqueada", "Has excedido los intentos permitidos.");
            return false;
        }

        // Verificar usuario y contraseña
        Usuario usuario = ConsultasLogin.obtenerUsuarioPorEmail(email);
        if (usuario == null || !usuario.getContrasena().equals(password)) {
            ConsultasLogin.registrarIntentoFallido(email);
            Alertas.error("Error", "Usuario o contraseña incorrectos.");
            return false;
        }

        // Si tiene 2FA
        if (config.isAutenticacionDosFactores()) {
            String codigo = generarCodigo();
            ConsultasLogin.registrarCodigoVerificacion(email, codigo);
            EmailUtil.enviarCodigo(email, codigo);
            return SolicitarVerificacionCodigo.mostrar(email); // ventana emergente que pide el código
        }

        ConsultasLogin.reiniciarIntentos(email);
        return true;
    }

    private static boolean estaIpPermitida(String listaBlanca, String ip) {
        if (listaBlanca == null || listaBlanca.trim().isEmpty()) return true;
        String[] permitidas = listaBlanca.split(",");
        return Arrays.stream(permitidas).map(String::trim).anyMatch(ip::equals);
    }

    private static String generarCodigo() {
        return String.valueOf((int)(Math.random() * 900000) + 100000); // código de 6 cifras
    }
}
