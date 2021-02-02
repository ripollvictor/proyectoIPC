/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import DBAcess.ClubDBAccess;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Booking;
import java.time.ZoneId;

/**
 * FXML Controller class
 *
 * @author franc
 */
public class ReservarController implements Initializable {

    private Stage primaryStage;
    @FXML
    private ListView<String> listView;
     ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
     private ObservableList<String> datos = null;
     private ArrayList<String> reservas = new ArrayList<>();
     private ArrayList<Booking> misReservas = club.getUserBookings(LOGINController.getUsuario());
    @FXML
    private Button borrarReserva;
    
    
 
      @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        for (int i = 0; i < misReservas.size(); i++) {
            reservas.add(bookString(misReservas.get(i)));
        }
       
        datos = FXCollections.observableArrayList(reservas);
        listView.setItems(datos);
        borrarReserva.disableProperty().bind(
        Bindings.equal(-1,
        listView.getSelectionModel().selectedIndexProperty()));

    }    
    public ArrayList<Booking> bo = new ArrayList<>();
    @FXML
    private void borrarReserva(ActionEvent event) {
        
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Se borrará la reserva");
        alerta.setHeaderText("Los cambios se guardarán permanentemente");
        Optional<ButtonType> result = alerta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int i = listView.getSelectionModel().getSelectedIndex();
            if (i >= 0) {
                //datos.remove(i);

                for (int j = 0; j < club.getBookings().size(); j++) {
                    ArrayList<Booking> misReservas2 = club.getUserBookings(LOGINController.getUsuario());
                    if (club.getBookings().get(j).equals(misReservas2.get(i))) {
                        LocalDate d2 = club.getBookings().get(j).getMadeForDay();

                        Instant instant = d2.atStartOfDay(ZoneId.systemDefault()).toInstant();
                        LocalTime d3 = club.getBookings().get(j).getFromTime();
                        long hours = d3.getHour();
                        hours = hours * 3600000;
                        long minis = d3.getMinute();
                        minis = minis * 60000;
                        long timeInMillis = instant.toEpochMilli();
                        long dayMillis = 86400000;
                        timeInMillis = timeInMillis - dayMillis;
                        timeInMillis = timeInMillis + hours + minis;
                        System.out.println(timeInMillis);
                        long current = System.currentTimeMillis();
                        System.out.println(current);
                        System.out.println(club.getBookings().get(j).getFromTime());
                        LocalDate now = LocalDate.now();
                        System.out.println(now);
                        
                        if (current > timeInMillis) {
                            //if(LocalDate.now().equals(club.getBookings().get(j).getMadeForDay())){
                            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("24 horas");
                            alerta.setHeaderText("No es posible eliminar porque quedan menos de 24 horas");
                            Optional<ButtonType> result2 = alerta.showAndWait();
                            //}
                            
                        } else {
                            datos.remove(i);
                            club.getBookings().remove(j);
                        }

                    }

                }
            }

        }

        }
    
    //Metodo toString para los bookins que vamos a meter en el listView
    public String bookString(Booking book) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");

        return book.getMadeForDay().format(dayFormatter) + " a las " + book.getFromTime().format(timeFormatter) + " en la pista " + book.getCourt().getName() ;
    }
    
    
    public void initStage(Stage stage)
    {
        primaryStage = stage;
    primaryStage.setTitle("Reservar");
    }   

    @FXML
    private void irPistas(ActionEvent event) {
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
   
    @FXML
    private void irReservas(ActionEvent event) {
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
    private void irPerfil(ActionEvent event) {
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

    
    
}
