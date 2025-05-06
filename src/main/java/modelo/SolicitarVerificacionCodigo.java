/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import bbdd.ConsultasLogin;
import controladores.VerificacionCodigoController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author k0343
 */
public class SolicitarVerificacionCodigo {

    public static boolean resultado = false;

    public static void setResultado(boolean valor) {
        resultado = valor;
    }

    public static boolean mostrar(String email) {
        try {
            FXMLLoader loader = new FXMLLoader(SolicitarVerificacionCodigo.class.getResource("/vistas/VerificacionCodigo.fxml"));
            Parent root = loader.load();

            VerificacionCodigoController controlador = loader.getController();
            controlador.setEmail(email); // <--- esto sí está bien

            Stage stage = new Stage();
            stage.setTitle("Verificación");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            return resultado;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
