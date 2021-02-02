/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import DBAcess.ClubDBAccess;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Booking;
import model.Court;

/**
 * FXML Controller class
 *
 * @author franc
 */
public class InicioController implements Initializable{

    private Stage primaryStage;
    @FXML
    private GridPane grid;
    @FXML
    private DatePicker day;
    @FXML
    private Label slotSelected;
    @FXML
    private TextField user;

    

    public void initStage(Stage stage) {
        primaryStage = stage;
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
    private void login(ActionEvent event) {
        try {
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("LOGIN.fxml"));
            Parent root = (Parent) miCargador.load();
            // acceso al controlador de ventana 1
            LOGINController ventana1 = miCargador.<LOGINController>getController();
            ventana1.initStage(primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {e.printStackTrace();
        }
    }

    
     private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(90);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    

    // se puede cambiar por codigo la pseudoclase activa de un nodo    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<List<BookingSlot>> bookingSlots = new ArrayList<>(); //Para varias columnas List<List<Boking>>
    

    
    private ObjectProperty<BookingSlot> timeSlotSelected;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         // TODO
         
      
        timeSlotSelected = new SimpleObjectProperty<>();

        //---------------------------------------------------------------------
        //inicializa el DatePicker al dia actual
        day.setValue(LocalDate.now());

        //---------------------------------------------------------------------
        // pinta los SlotTime en el grid
        setTimeSlotsGrid(day.getValue());

        //---------------------------------------------------------------------
        //cambia los SlotTime al cambiar de dia
        day.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c);
        });
        
        // enlazamos timeSlotSelected con el label para mostrar la seleccion
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
        timeSlotSelected.addListener((a, b, c) -> {
            if (c == null) {
                slotSelected.setText("");
            } else if (c.getMyBooking() != null) {
                slotSelected.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter)
                        + " Reservado por: "
                        + c.getMyBooking().getMember().getName()
                );

            } else {
                slotSelected.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter)
                );
            }
        });

    }    
    private void setTimeSlotsGrid(LocalDate date) {
        //actualizamos la seleccion
        timeSlotSelected.setValue(null);
        ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
        //--------------------------------------------        
        //borramos los SlotTime del grid
        ObservableList<Node> children = grid.getChildren();

        for (int i = 0; i < bookingSlots.size(); i++) {
            for(int j = 0; j < bookingSlots.get(i).size(); j++){
                children.remove(bookingSlots.get(i).get(j).getView());
                
                
            }
            }

        //---------------------------------------------------------------------------
        // recorremos para cada Columna y creamos para cada slot
        // solo hay una columna
       
        ArrayList<Court> pistas = club.getCourts();
        ArrayList<Booking> booksDeBooking = new ArrayList<>(); 
        
        
                      

    
        int columnGrid = 0;
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {
            int slotIndex = 1;
            
            List<BookingSlot> bookings = new ArrayList<>();
            bookingSlots.add(bookings);
            booksDeBooking = club.getCourtBookings(pistas.get(columnIndex).getName(), day.getValue());

//----------------------------------------------------------------------------------
            // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
            for (LocalDateTime startTime = date.atTime(firstSlotStart);
                    !startTime.isAfter(date.atTime(lastSlotStart));
                    startTime = startTime.plus(slotLength)) {

                //---------------------------------------------------------------------------------------
                // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
                if (!booksDeBooking.isEmpty()) {
                    if (startTime.equals(date.atTime(booksDeBooking.get(0).getFromTime()))) {

                       BookingSlot book2;
                        book2 = new BookingSlot(pistas.get(columnIndex), booksDeBooking.get(0), startTime, slotLength);
                        registerPressHandlers(book2);
                        ObservableList<String> styles = book2.getView().getStyleClass();
                        if(!book2.getMyBooking().getMember().getLogin().equals(user.getText())){
                            if (
                                styles.contains("time-slot-libre")) {
                                styles.remove("time-slot-libre");
                                styles.add("time-slot");
                            }
                        }else{
                                styles.remove("time-slot");
                                styles.add("time-slot-user");
                                        
                            }
                        grid.add(book2.getView(), columnGrid, slotIndex);
                        booksDeBooking.remove(0);

                    } else {
                        BookingSlot book = new BookingSlot(pistas.get(columnIndex), null, startTime, slotLength);

                        bookingSlots.get(columnIndex).add(book);
                        registerPressHandlers(book);
                        ObservableList<String> styles = book.getView().getStyleClass();
                        if (styles.contains("time-slot")) {
                            styles.remove("time-slot");
                            styles.add("time-slot-libre");
                        } 
                        
                        grid.add(book.getView(), columnGrid, slotIndex);

                    }

                } else {

                    BookingSlot book = new BookingSlot(pistas.get(columnIndex), null, startTime, slotLength);

                    bookingSlots.get(columnIndex).add(book);
                    registerPressHandlers(book);
                    ObservableList<String> styles = book.getView().getStyleClass();
                    if (styles.contains("time-slot")) {
                            styles.remove("time-slot");
                            styles.add("time-slot-libre");
                        } 
                  
                    
                    grid.add(book.getView(), columnGrid, slotIndex);

                }

                //-----------------------------------------------------------
                // lo anyadimos al grid en la posicion x= pista+1, y= slotIndex
                slotIndex++;
            }
            columnGrid++;
        }
    }

    private void registerPressHandlers(BookingSlot timeSlot) {
        timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            //-------------------------------------------------------------------------
            //solamente puede estar seleccionado un slot dentro de la lista de slot
            //sin el bucle exterior se podria seleccionar un SlotTime por cada columna
            
           for(int i = 0; i < 4; i++) {
               List<BookingSlot> cadaUno = new ArrayList<>();
               cadaUno = bookingSlots.get(i);
               cadaUno.forEach(slot -> slot.setSelected(slot == timeSlot));
           }
            //----------------------------------------------------------------
            //actualizamos el label Dia-Hora-Pista, esto es ad hoc,  para mi diseño
            timeSlotSelected.setValue(timeSlot);
            //----------------------------------------------------------------
            // si es un Clik  vamos a mostrar una quien ha hecho la reserva y cambiar el estilo de la celda
            if (event.getClickCount() > 1) {
                
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("ERROR");
                    alerta.setHeaderText("Para reservar Inicie Sesión");
                    alerta.show();
                }
           
        });

    }

    @FXML
    private void buscarUser(ActionEvent event) {
        ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
        
        if(club.existsLogin(user.getText())){
            setTimeSlotsGrid(day.getValue());
        }
    }

    @FXML
    private void infoButton(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText("Aquí podrá ver los horarios de las pistas.");
        alerta.setContentText("Para hacer una reserva Inicie sesión o Registrese. \n" 
                + "Pulse sobre la fecha reservada para ver el usuario que la reservó. \n" 
                + "También puede seleccionar la fecha y el usuario para que se muestren sus reservas en ROJO.");
        alerta.show();
    }
    
    
    public class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;
        protected final Pane view;

        private final BooleanProperty selected = new SimpleBooleanProperty();
       
       
       
        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start;
            this.duration = duration;
            
            
            view = new Pane();
            view.getStyleClass().add("time-slot");
            // ---------------------------------------------------------------
            // de esta manera cambiamos la apariencia del TimeSlot cuando los seleccionamos
            selectedProperty().addListener((obs, wasSelected, isSelected)
                    -> view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));

        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public LocalDate getDate() {
            return start.toLocalDate();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }

        public Node getView() {
            return view;
        }

    }
     
     public class BookingSlot extends TimeSlot {
        protected final Pane view;
            private Court myCourt;
            
 

            /**
             * Get the value of myCourt
             *
             * @return the value of myCourt
             */
            public Court getMyCourt() {
                return myCourt;
            }

 

            /**
             * Set the value of myCourt
             *
             * @param myCourt new value of myCourt
             */
            public void setMyCourt(Court myCourt) {
                this.myCourt = myCourt;
            }

 

            
            
            private Booking myBooking;

 

            /**
             * Get the value of myBooking
             *
             * @return the value of myBooking
             */
            public Booking getMyBooking() {
                return myBooking;
            }

 

            /**
             * Set the value of myBooking
             *
             * @param myBooking new value of myBooking
             */
            public void setMyBooking(Booking myBooking) {
                this.myBooking = myBooking;
            }

 

        public BookingSlot(Court myCourt, Booking myBooking, LocalDateTime start, Duration duration) {
            super(start, duration);
            this.myCourt = myCourt;
            this.myBooking = myBooking;
            
            view = new Pane();
            view.getStyleClass().add("time-slot");
        }

 

            
            

        
        }
     
   
    
}
