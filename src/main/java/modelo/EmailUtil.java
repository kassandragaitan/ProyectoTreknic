/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Clase utilitaria para el envío de correos electrónicos desde el sistema
 * Treknic.
 * <p>
 * Utiliza la librería Apache Commons Email para realizar envíos de correos
 * electrónicos a través de un servidor SMTP. En este caso, está configurado con
 * Hostinger.
 * </p>
 *
 * <p>
 * Actualmente se utiliza para enviar códigos de verificación como parte del
 * sistema de autenticación en dos pasos (2FA) o recuperación de cuenta.
 * </p>
 *
 * @author k0343
 */
public class EmailUtil {

    /**
     * Envía un correo electrónico con un código de verificación al destinatario
     * especificado.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param codigo Código de verificación que se enviará en el cuerpo del
     * mensaje.
     */
    public static void enviarCodigo(String destinatario, String codigo) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.hostinger.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("kassandra@reynaldomd.com", "2025-Kassandra"));
            email.setSSLOnConnect(true);
            email.setCharset("UTF-8");
            email.setFrom("kassandra@reynaldomd.com");
            email.setSubject("Código de verificación - Treknic");
            email.setMsg("Hola, tu código de verificación es: " + codigo + "\n\nGracias por usar Treknic.");
            email.addTo(destinatario);
            email.send();
        } catch (EmailException ex) {
            ex.printStackTrace();
        }
    }
}
