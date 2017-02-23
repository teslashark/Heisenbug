/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This JavaFX app lets the user play scales.
 * @author Janet Davis 
 * @author SOLUTION - PROJECT 1
 * @since January 26, 2017
 */
public class TuneComposer extends Application {
    
    /**
     * Contains the rectangle objects that represent
     * the musical notes in the UI
     */
    private ArrayList musicNotesArray = new ArrayList();    
    
    /**
     * Represents the number of pitch steps for 
     * do, re, mi, fa, so, la, ti, do.
     * Source: https://en.wikipedia.org/wiki/Solfège
     */
    private static final int[] SCALE = {0, 2, 4, 5, 7, 9, 11, 12};
    
    /**
     * Play notes at maximum volume.
     */
    private static final int VOLUME = 127;
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player;

    @FXML
    private Pane musicLines;

    /**
     * Creates the grey lines of the music staff.
     */
    public void initialize() {
        for (int i = 1; i <= 127; i++) {
            Line line = new Line(0, i*10, 2000, i*10);
            //line.setStrokeWidth(1);
            line.setStroke(Color.LIGHTGRAY);
            musicLines.getChildren().add(line);
        }
    }
    
    /**
     * Constructs a new ScalePlayer application.
     */
    public TuneComposer() {
        this.player = new MidiPlayer(1,60);
    }
    
    /**
     * Play a new scale, after stopping and clearing any previous scale.
     * @param startingPitch an integer between 0 and 115
     */
    protected void playScale(int startingPitch) {
        player.stop();
        player.clear();
        for (int i=0; i < 8; i++) {
            player.addNote(startingPitch+SCALE[i], VOLUME, i,    1, 0, 0);
            player.addNote(startingPitch+SCALE[i], VOLUME, 16-i, 1, 0, 0);
        }
        player.play();
    }
    
    /**
     * 
     * @param pitch
     * @param startTick
     */
    protected void addNote(int pitch, int startTick) {
        final int noteLength = 10000;
        player.addNote(pitch, VOLUME, startTick, noteLength, 0, 0);
        //player.addNote(pitch, VOLUME, 1, noteLength, 0, 0);
    }
    
    /**
     * creates a rectangle object of width 100px and height 10px
     * 
     * @param event 
     */
    @FXML
    protected void handleOnMouseClickAction(MouseEvent event){
        Rectangle noteBox = new Rectangle(100, 10);
        noteBox.x = (int) event.getSceneX();
        noteBox.y = (int) event.getSceneY();
        musicNotesArray.add(noteBox);        
    }
    
    /**
     * When the user clicks the "Stop playing" button, stop playing the scale.
     * @param event the button click event
     */
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        player.stop();
    }    
    
    /**
     * 
     * @param event the menu selection event
     */
    @FXML
    protected void handleStopMenuItemAction(ActionEvent event) {
        player.stop();
        player.clear();
    }

    /**
     * 
     * @param event 
     */
    @FXML
    protected void handlePlayMenuItemAction(ActionEvent event) {
        
        player.stop();
        player.clear();

        for (int i = 0; i < musicNotesArray.size(); i++){            
            Rectangle noteBox = (Rectangle) musicNotesArray.get(i); 
            int tickDelay = noteBox.x / 200;
            int pitch = noteBox.y / 10;
            this.addNote(pitch, tickDelay);            
        }
        
        player.play();
        
    }
    
    /**
     * 
     * @param event 
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }    
    
    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {                
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });        
        primaryStage.show();
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
