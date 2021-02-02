/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author franc
 */
public class HomeINIController implements Initializable {

 private Stage primaryStage;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void initStage(Stage stage)
    {primaryStage = stage;
    primaryStage.setTitle("Home");
    
    }
    
    @FXML
    private void irPistas(ActionEvent event2) {
        try {
            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Pistas.fxml"));
            Parent root = (Parent) miCargador2.load();
            // acceso al controlador de ventana 1
            PistasController ventana1 = miCargador2.<PistasController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }
    
     @FXML
    private void irReservar(ActionEvent event2) {
        try {
            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Reservar.fxml"));
            Parent root = (Parent) miCargador2.load();
            // acceso al controlador de ventana 1
            ReservarController ventana1 = miCargador2.<ReservarController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }

    @FXML
    private void irReservas(ActionEvent event2) {
        try {
            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Reservas.fxml"));
            Parent root = (Parent) miCargador2.load();
            // acceso al controlador de ventana 1
            ReservasController ventana1 = miCargador2.<ReservasController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }
    
    @FXML
    private void irPerfil(ActionEvent event2) {
        try {
            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Perfil.fxml"));
            Parent root = (Parent) miCargador2.load();
            // acceso al controlador de ventana 1
            PerfilController ventana1 = miCargador2.<PerfilController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }

    @FXML
    private void infoButton(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setContentText("->RESERVAR<- Para hacer una reserva. \n" 
                + "->MIS RESERVAS<- Para ver y eliminar reservas \n" 
                + "->PISTAS<- Para ver los horarios de las pistas");
        alerta.show();
    }
    
 
}
