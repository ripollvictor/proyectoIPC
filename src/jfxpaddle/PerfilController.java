/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import DBAcess.ClubDBAccess;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static jfxpaddle.RegistroController.limitTextField;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Fran
 */
public class PerfilController implements Initializable {

    @FXML
    private Text mensaje_registro;
    private LOGINController lOGINController;
    private final Member miembro = LOGINController.getMem();
    private Member member2 = LOGINController.getMem();
    private Stage primaryStage;
    @FXML
    private TextField nombre;
    @FXML
    private TextField login;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField num_telf;
    @FXML
    private TextField apellidos;
    @FXML
    private TextField num_tarjeta;
    @FXML
    private TextField svc;
    ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
    @FXML
    private ImageView ivImagen;
    @FXML
    private Button btnBuscar;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void initStage(Stage stage)  {
       
        primaryStage = stage;
        primaryStage.setTitle("Perfil");

        limitTextField(svc, 3);
        limitTextField(num_tarjeta, 16);
        limitTextField(num_telf, 9);
        
        ArrayList llista2 = club.getMembers();
        for (int i = 0; i < club.getMembers().size(); i++) {

                if (llista2.get(i).equals(club.getMemberByCredentials(miembro.getLogin(), miembro.getPassword()))) {
                        member2 = club.getMembers().get(i);
                        
                }

        }
        nombre.setText(member2.getName());
        login.setText(member2.getLogin());
        pass.setText(member2.getPassword());
        num_telf.setText(member2.getTelephone());
        apellidos.setText(member2.getSurname());
        svc.setText(member2.getSvc());
        num_tarjeta.setText(member2.getCreditCard());
        ivImagen.setImage(member2.getImage());
        
        btnBuscar.setOnAction(event -> {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");

        // Agregar filtros para facilitar la busqueda
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        
        // Obtener la imagen seleccionada
        File imgFile = fileChooser.showOpenDialog(stage);

        // Mostar la imagen
        if (imgFile != null) {
            Image image = new Image("file:" + imgFile.getAbsolutePath());
            ivImagen.setImage(image);
        }
    });
   
    }
    
    @FXML
    private void guardar(ActionEvent event) {
        

        String nom = nombre.getText();
        String apellido = apellidos.getText();
        String telf = num_telf.getText();
        String tarjeta = num_tarjeta.getText();
        String sv = svc.getText();
        
        
        

        
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Guardar Cambios");
        alerta.setHeaderText("Los cambios se guardarán permanentemente");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ArrayList llista = club.getMembers();

            Member member = new Member(nom, apellido, telf, login.getText(), pass.getText(), tarjeta, sv, ivImagen.getImage());
            for (int i = 0; i < club.getMembers().size(); i++) {

                if (llista.get(i).equals(club.getMemberByCredentials(miembro.getLogin(), miembro.getPassword()))) {
                    club.getMembers().remove(i);
                    club.getMembers().add(i, member);

                }

            }
            club.saveDB();
            nombre.setText(member.getName());
            login.setText(member.getLogin());
            pass.setText(member.getPassword());
            num_telf.setText(member.getTelephone());
            apellidos.setText(member.getSurname());
            num_tarjeta.setText(member.getCreditCard());
            svc.setText(member.getSvc());
            
        }
        
        
        
        

    }
    
     public static void limitTextField(TextField textField, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
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
    private void irHome(ActionEvent event) {
        try {
            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("HomeINI.fxml"));
            Parent root = (Parent) miCargador2.load();
            // acceso al controlador de ventana 1
            HomeINIController ventana1 = miCargador2.<HomeINIController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }
    
}
