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
 * This JavaFX app lets the user play scales.
 * @author Janet Davis, Ben Limpich, Emma Twersky, Cooper Lazar
 * @author PROJECT 3
 * @since February 20, 2017
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
     * length of time in ticks that notes should play;
     */
    private final int noteLength = 100;
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player;

    /**
     * Pane that all visual music features live within
     */
    @FXML
    private Pane musicPane;
    
    /**
     * The vertical red line that will move as the notes play.
     */
    @FXML
    private PlayBar playBarObj;

    /**
     * creates the grey lines of the music staff and the red line playBar object.
     * adds staff lines to musicPane pane, and sets css id to "staffLine"
     * passes musicPane to playBar constructor
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
     * creates a rectangle object of width 100px and height 10px.
     * sets y to snap b/t staff lines, and
     * sets left side of rectangle to mouse x-cord
     * adds the rectangle to musicNotesArray for midiPlayer
     * and adds to musicPane to visually show note's box.
     * Assumes scren size is 2000px wide.

     * @param event 
     */
    @FXML
    protected void handleOnMouseClickAction(MouseEvent event){
        //create rectangle
        Rectangle r = new Rectangle();
        r.setId("noteBox");
        r.setWidth(100);
        r.setHeight(10);
        //find x and y cords from mouse position
        if (event.getX() > 1900) { //ensure rectangle doesn't go off screen
            r.setX(1900);            
        }
        else {
            r.setX(event.getX());
        }
        //snap Y between staff lines
        r.setY(Math.round(event.getY() / 10) * 10);
        
        musicNotesArray.add(r);
        musicPane.getChildren().add(r);        
    }

    /**
     * stop sound, animation, and clear player when user selects stop
     * @param event the menu selection event
     */
    @FXML
    protected void handleStopMenuItemAction(ActionEvent event) {
        player.stop();
        player.clear();
        playBarObj.stopAnimation();
    }

    /**
     * adds notes to player, plays the player, plays the red bar animation
     * @param event 
     */
    @FXML
    protected void handlePlayMenuItemAction(ActionEvent event) {
        player.stop();
        player.clear();

        addNotesArrayToMidiPlayer();
        
        player.play();
        playBarObj.playAnimation(musicNotesArray);
    }
    
    /**
     * handler for "exit" menuItem to quit program with success status.
     * @param event 
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }    
    
    /**
     * takes array of rectangles representing notes, and adds them to player.
     * assumes pane height is 1280px high.
     * all objects in musicNotesArray must be of type javafx shape.
     */
    private void addNotesArrayToMidiPlayer() {
        for (int i = 0; i < musicNotesArray.size(); i++){            
            Rectangle noteBox = (Rectangle) musicNotesArray.get(i); 
            int startTick = (int)noteBox.getX();
            int pitch = 128 - (int) noteBox.getY() /10;
            player.addNote(pitch, VOLUME, startTick, noteLength, 0, 0);
        }
    }
    
    
    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     * @throws java.io.IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {   
        //read fxml file
        Parent root = FXMLLoader.load(getClass().getResource("TuneComposer.fxml"));
        Scene scene = new Scene(root);
        
        //setup and show primaryStage
        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // quit program when primaryStage window closed
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });        
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
}
