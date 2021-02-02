/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import DBAcess.ClubDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Member;

/**
 * FXML Controller class
 *
 * @author franc
 */
public class LOGINController implements Initializable {
    private Stage primaryStage;
    @FXML
    private TextField text_usuario;
    @FXML
    private PasswordField text_password;
    @FXML
    private Text mensaje_user;
    private static Member mem;
    private static String user = "";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void initStage(Stage stage)
    {primaryStage = stage;}

    public static String getUsuario() {
        return user;
    }
    
    public static Member getMem() {
        return mem;
    }

    @FXML
    private void registrar(ActionEvent event) {
        try {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("Registro.fxml"));
            Parent root = (Parent) miCargador.load();
            // acceso al controlador de ventana 1
            RegistroController ventana1 = miCargador.<RegistroController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }

    @FXML
    private void iniciar(ActionEvent event) {
        ClubDBAccess acc= ClubDBAccess.getSingletonClubDBAccess();
        String login = text_usuario.getText();
        String pass = text_password.getText();
        if (acc.existsLogin(login)) {
            mem = acc.getMemberByCredentials(login, pass);
            if(mem != null) {
                try {
                    user =  login;
                    FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Reservas.fxml"));
                    Parent root = (Parent) miCargador2.load();
                    // acceso al controlador de ventana 1
                    ReservasController ventana1 = miCargador2.<ReservasController>getController();
                    ventana1.initStage(primaryStage);
                    Scene scene = new Scene(root);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException e) {e.printStackTrace();}
        
            }
            else{mensaje_user.setText("Contraseña incorrecta");}
                
        } else{mensaje_user.setText("Usuario incorrecto");}
    }
    
     @FXML
    private void inicio(ActionEvent event) {
        try {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("Inicio.fxml"));
            Parent root = (Parent) miCargador.load();
            // acceso al controlador de ventana 1
            InicioController ventana1 = miCargador.<InicioController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }
    
    
}
