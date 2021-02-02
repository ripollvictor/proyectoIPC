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
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Member;

/**
 * FXML Controller class
 *
 * @author ripol
 */
public class RegistroController implements Initializable {

    private Stage primaryStage;
    private Scene escenaAnterior;
    private String tituloAnterior;
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
    @FXML
    private Text mensaje_registro;
    @FXML
    private ImageView ivImagen;
    @FXML
    private Button btnBuscar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
}
      

    void initStage(Stage stage) {
        primaryStage = stage;
        escenaAnterior = stage.getScene();
        tituloAnterior = stage.getTitle();
        primaryStage.setTitle("Registrarse"); 
        
        //Limitar los caracteres que se pueden meter
        limitTextField(svc, 3);
        limitTextField(num_tarjeta, 16);
        limitTextField(num_telf, 9);
        
        // Para que solo se puedan meter NUMEROS
        num_telf.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
                if(!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        );
                // Para que solo se puedan meter NUMEROS

        num_tarjeta.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
                if(!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        );
                // Para que solo se puedan meter NUMEROS

        svc.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {

                if(!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
               
            }
        );
        
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
    // Metodo para limitar los caracteres que puedes meter al text field
    public static void limitTextField(TextField textField, int limit) {
        UnaryOperator<Change> textLimitFilter = change -> {
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
    private void registrarse(ActionEvent event) {
        ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
           
        String nom = nombre.getText();
        String apellido = apellidos.getText();
        String telf = num_telf.getText();
        String tarjeta = num_tarjeta.getText();
        String sv = svc.getText();
        if (club.existsLogin(login.getText())){
        mensaje_registro.setText("El login ya ha sido utilizado");
        } else{
        Member mem = new Member(nom,apellido,telf,login.getText(),pass.getText(),tarjeta,sv,ivImagen.getImage());
        club.getMembers().add(mem);
        club.saveDB();
        System.out.println(mem.toString());
        System.out.println("REGISTRADO CON EXITO!!");
        primaryStage.setTitle(tituloAnterior);
        primaryStage.setScene(escenaAnterior);
        }
    }
    
//    private void irHome(ActionEvent event2) {
//        try {
//            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("HomeINI.fxml"));
//            Parent root = (Parent) miCargador2.load();
//            // acceso al controlador de ventana 1
//            HomeINIController ventana1 = miCargador2.<HomeINIController>getController();
//            ventana1.initStage(primaryStage);
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (IOException e) {e.printStackTrace();
//        }
//    }

    @FXML
    private void atras(ActionEvent event) {
        primaryStage.setTitle(tituloAnterior);
        primaryStage.setScene(escenaAnterior);
    }
}
