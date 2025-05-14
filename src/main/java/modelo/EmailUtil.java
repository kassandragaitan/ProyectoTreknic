/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author k0343
 */
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailUtil {
    
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
