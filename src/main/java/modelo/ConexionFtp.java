/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import Utilidades.Alertas;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Clase que gestiona la conexión y operaciones FTP con el servidor donde se
 * almacenan imágenes.
 *
 * Permite conectar al servidor FTP, subir archivos, cargar imágenes desde una
 * URL pública, y eliminar archivos remotos.
 *
 * El servidor está configurado para uso en el dominio reynaldomd.com bajo el
 * proyecto Kassandra.
 *
 * <p>
 * Requiere la librería Apache Commons Net.</p>
 *
 * @author k0343
 */
public class ConexionFtp {

    public static FTPClient clienteFtp;

    /**
     * Establece la conexión con el servidor FTP usando credenciales
     * predefinidas.
     *
     * @return true si la conexión fue exitosa, false si ocurrió un error.
     */
    public static boolean conectar() {
        String servidorFtp = "ftp.reynaldomd.com";
        String user = "u812167471.kassandra";
        String pass = "2025-Kassandra";

        clienteFtp = new FTPClient();
        try {

            clienteFtp.connect(servidorFtp);

            clienteFtp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            clienteFtp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            clienteFtp.enterLocalPassiveMode();
            return clienteFtp.login(user, pass);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Sube un archivo local al servidor FTP.
     *
     * @param localFile Archivo local a subir.
     * @param remoteFileName Nombre del archivo en el servidor FTP.
     * @return true si el archivo fue subido correctamente, false si falló.
     */
    public static boolean subirArchivo(File localFile, String remoteFileName) {
        try (FileInputStream fis = new FileInputStream(localFile)) {
            return clienteFtp.storeFile(remoteFileName, fis);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Carga una imagen desde la URL pública del servidor y la establece en un
     * ImageView.
     *
     * @param imagen Nombre del archivo de imagen en el servidor.
     * @param img ImageView donde se cargará la imagen.
     */
    public static void cargarImagen(String imagen, ImageView img) {

        String urlImagen = "https://reynaldomd.com/kassandra/" + imagen;
        Image image = new Image(urlImagen, true);
        img.setImage(image);
    }

    /**
     * Cierra la sesión actual con el servidor FTP.
     */
    public static void desconectar() {
        try {
            clienteFtp.logout();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Elimina un archivo del servidor FTP.
     *
     * @param nombreImagen Nombre del archivo a eliminar.
     */
    public static void eliminarArchivo(String nombreImagen) {
        conectar();
        try {
            if (!clienteFtp.deleteFile(nombreImagen)) {
            } else {
                System.out.println("Archivo eliminado con éxito: " + nombreImagen);
            }
        } catch (IOException ex) {
            Alertas.error("Error al eliminar", "Error al eliminar el archivo: " + ex.getMessage());
        } finally {
            desconectar();
        }
    }
}
