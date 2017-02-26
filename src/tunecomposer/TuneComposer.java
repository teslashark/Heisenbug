/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This JavaFX app lets the user compose tunes.
 * @author BEC3 (Emma, Cooper and Ben)
 * @since January 26, 2017
 */
public class TuneComposer extends Application {
    
    /**
     * Contains the rectangle objects that represent 
     * the musical notes in the UI.
     */
    private ArrayList musicNotesArray = new ArrayList();    
    
    /**
     * Play notes at maximum volume.
     */
    private static final int VOLUME = 127;
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player;

    /**
     * Central pane which holds the music staff and notes.
     */
    @FXML
    private Pane musicPane;
    
    /**
     * The vertical red line that will move as the notes play.
     */
    @FXML
    private PlayBar playBarObj;

    /**
     * Creates the grey lines of the music staff and the red playLine. 
     * Adds all of these to musicPane pane. Sets the red line to playLine
     */
    public void initialize() {
        for (int i = 1; i <= 127; i++) {
            Line line = new Line(0, i*10, 2000, i*10);
            line.setId("staffLine");
            musicPane.getChildren().add(line);
        }
        playBarObj = new PlayBar(musicPane);
    }
    
    /**
     * Constructs a new ScalePlayer application.
     */
    public TuneComposer() {
        this.player = new MidiPlayer(100,60);
    }
    
    /**
     * Creates a rectangle object of width 100px and height 10px
     * @param event 
     */
    @FXML
    protected void handleOnMouseClickAction(MouseEvent event){
        Rectangle r = new Rectangle();
        r.setId("noteBox");
        r.setWidth(100);
        r.setHeight(10);
        
        r.setX(event.getX());
        
        r.setY(Math.round(event.getY() / 10) * 10);
        
        musicNotesArray.add(r);
        musicPane.getChildren().add(r);        
    }

    /**
     * Stops tune from playing and clears animation.
     * @param event the menu selection event
     */
    @FXML
    protected void handleStopMenuItemAction(ActionEvent event) {
        player.stop();
        player.clear();
        playBarObj.stopAnimation();
    }

    /**
     * Adds all notes to the MidiPlayer, plays them and begins animation.
     * @param event the menu selection event
     */
    @FXML
    protected void handlePlayMenuItemAction(ActionEvent event) {
        player.stop();
        player.clear();

        final int noteLength = 100;
        
        for (int i = 0; i < musicNotesArray.size(); i++){            
            Rectangle noteBox = (Rectangle) musicNotesArray.get(i); 
            int startTick = (int)noteBox.getX();
            int pitch = 128 - (int) noteBox.getY() /10;
            player.addNote(pitch, VOLUME, startTick, noteLength, 0, 0);
        }

        player.play();
        playBarObj.playAnimation(musicNotesArray);
    }
    
    /**
     * Exits tune player.
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
