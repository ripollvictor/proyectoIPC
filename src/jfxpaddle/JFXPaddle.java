/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DBAcess.ClubDBAccess;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.WindowEvent;

/**
 *
 * @author Fran
 */
public class JFXPaddle extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        InicioController controladorPrincipal = loader.<InicioController>getController();
        controladorPrincipal.initStage(stage);
        stage.setScene(scene);
        stage.setTitle("PadelWord");
        stage.getIcons().add(new Image("/Imatges/logofondo.png"));

        stage.show();
        // Para guardar los cambios cuando se cierra la app
        stage.setOnCloseRequest((WindowEvent event) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(club.getClubName());
            alert.setHeaderText("Saving data in DB");
            alert.setContentText("The application is saving the changes into the database. This action can expend some minutes.");
            alert.show();
            club.saveDB();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
        
    }
    
}
