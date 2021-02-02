/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxpaddle;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Booking;
import DBAcess.ClubDBAccess;
import model.Court;


/**
 * FXML Controller class
 *
 * @author franc
 */
public class ReservasController implements Initializable {

    private Stage primaryStage;
    @FXML
    private DatePicker day;
    @FXML
    private GridPane grid;
    @FXML
    private Label slotSelected;
    
    private LOGINController lOGINController;
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(90);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    

    // se puede cambiar por codigo la pseudoclase activa de un nodo    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<List<BookingSlot>> bookingSlots = new ArrayList<>(); //Para varias columnas List<List<Boking>>
    

    
    private ObjectProperty<TimeSlot> timeSlotSelected;
 
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
            } else {
                slotSelected.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter));
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
                if (booksDeBooking.size() != 0) {
                    if (startTime.equals(date.atTime(booksDeBooking.get(0).getFromTime()))) {

                        BookingSlot book2 = new BookingSlot(pistas.get(columnIndex), booksDeBooking.get(0), startTime, slotLength);
                        registerPressHandlers(book2);
                         ObservableList<String> styles = book2.getView().getStyleClass();
                        if (styles.contains("time-slot-libre")) {
                            styles.remove("time-slot-libre");
                            styles.add("time-slot");
                        } else {
                            styles.remove("time-slot");
                            styles.add("time-slot");
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
                    else {
                            styles.remove("time-slot-libre");
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
                    else {
                            styles.remove("time-slot-libre");
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

            for (int i = 0; i < 4; i++) {
                List<BookingSlot> cadaUno = new ArrayList<>();
                cadaUno = bookingSlots.get(i);
                cadaUno.forEach(slot -> slot.setSelected(slot == timeSlot));
            }
            //----------------------------------------------------------------
            //actualizamos el label Dia-Hora-Pista, esto es ad hoc,  para mi diseño
            timeSlotSelected.setValue(timeSlot);

            ClubDBAccess club = ClubDBAccess.getSingletonClubDBAccess();
            ObservableList<String> styles = timeSlot.getView().getStyleClass();
            //----------------------------------------------------------------
            // si es un doubleClik  vamos a mostrar una alerta y cambiar el estilo de la celda
            if (event.getClickCount() > 1 && styles.contains("time-slot-libre")) {
                if (club.hasCreditCard(LOGINController.getUsuario())) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Reserva");
                    alerta.setHeaderText("Confirma la selecció");
                    alerta.setContentText("Has seleccionat: "
                            + timeSlot.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                            + timeSlot.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
                    Optional<ButtonType> result = alerta.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        //LOGINController log = new LOGINController();
                        Boolean pagado = club.hasCreditCard(LOGINController.getUsuario());
                        Booking book = new Booking(LocalDateTime.now(), timeSlot.getDate(), timeSlot.getTime(), pagado, timeSlot.getMyCourt(), LOGINController.getMem());
                        club.getBookings().add(book);
                        if (styles.contains("time-slot")) {
                            styles.remove("time-slot");
                            styles.add("time-slot-libre");
                        } else {
                            styles.remove("time-slot-libre");
                            styles.add("time-slot");
                        }
                    }
                } else {
                    Alert pagar = new Alert(Alert.AlertType.WARNING);
                    pagar.setTitle("Forma de pago");
                    pagar.setHeaderText("Añadir tarjeta");
                    pagar.setContentText("Para hacer una reserva debe añadir su tarjeta de credito."
                            + "Puede hacerlo desde la pestaña superior perfil");
                    Optional<ButtonType> result = pagar.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            FXMLLoader miCargador2 = new FXMLLoader(getClass().getResource("Perfil.fxml"));
                            Parent root = (Parent) miCargador2.load();
                            // acceso al controlador de ventana 1
                            PerfilController ventana1 = miCargador2.<PerfilController>getController();
                            ventana1.initStage(primaryStage);
                            Scene scene = new Scene(root);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            } else if (event.getClickCount() > 1 && styles.contains("time-slot")) {
                Alert ocupado = new Alert(Alert.AlertType.ERROR);
                ocupado.setTitle("Pista Reservada");
                ocupado.setHeaderText("Este horario ya ha sido reservado");
                ocupado.setContentText(slotSelected.getText());
                ocupado.show();
            }
        });
    }
    
    public void initStage(Stage stage)
    {
        primaryStage = stage;
    primaryStage.setTitle("Reservas");
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
    private void irReservar(ActionEvent event) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void infoButton(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText("Aquí podrás reservar la pista.");
        alerta.setContentText("Para hacer una reserva haz doble click sobre la hora y pista deseadas. \n" 
                + "En 'Pistas' podrá ver las reservas de otros jugadores \n"
                + "En 'Mis Reservas' podra verlas y eliminarlas"
        );
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
        }

 

            
            
        public BookingSlot(LocalDateTime start, Duration duration) {
            super(start, duration);
        }
        
        }
     
     
     
    
}
