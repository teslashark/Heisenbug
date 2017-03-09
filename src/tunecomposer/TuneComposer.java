/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import tunecomposer.Selection;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;
import tunecomposer.NoteBox;

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
    protected ArrayList musicNotesArray = new ArrayList();
    
    //must be first letter capatilized
    private String selectedInstrument = "Piano";
    
    /**
     * Play notes at maximum volume.
     */
    private static final int VOLUME = 127;
    
    /**
     * length of time in ticks that notes should play;
     */
    
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
     * The group of 8 instrument RadioButtons in the side panel
     */
    @FXML
    private Group instrumentButtonsGroup;
    
    /**
     * An arrayList to hold all instrument radio buttons for easy access.
     */
    private ArrayList<RadioButton> instrumentButtons = new ArrayList<RadioButton>();
    
    private Selection selector;
    

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
        
        //convert group of radiobuttons to ArrayList of buttons
        for (int i = 0; i < 8; i++) {
            Node currentNode = instrumentButtonsGroup.getChildren().get(i);
            instrumentButtons.add((RadioButton)currentNode);
        }
        instrumentButtons.get(0).fire();
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
     * and adds to musicPane to visually show notes' box.
     * Assumes screen size is 2000px wide.

     * @param event 
     */
    
    
    protected void handleClickInPanel(MouseEvent mouse){
        
        boolean ctrl = mouse.isControlDown();
        
        if (mouse.getEventType() == mouse.MOUSE_CLICKED){
            NoteBox newNote = new NoteBox(selectedInstrument, mouse);
            
            if (ctrl){
                selector.select(newNote);
            }
            else{
                selector.unselectReplace(newNote);
            }    
        }
        
        else if (mouse.getEventType() == mouse.DRAG_DETECTED){
            SelectionRectangle rect = new SelectionRectangle();
            
        }
        
        
    }
    
    
    @FXML
    protected void handleOnMouseClickAction(MouseEvent event){
        NoteBox noteBox = new NoteBox(selectedInstrument, event);
        musicNotesArray.add(noteBox);
        musicPane.getChildren().add(noteBox.rectangle);   
    }
    
    /**
     * Handles when an instrument RadioButton is clicked.
     * Changes the global currentInstrument and currentNoteColor values to reflect
     * the newly selected instrument. Deselects the previous RadioButton.
     * @param event 
     */
    @FXML
    protected void handleInstrumentSelection(ActionEvent event) {
        RadioButton selectedButton = (RadioButton)event.getSource();
        selectedInstrument = selectedButton.getText().replaceAll("\\s+","");
        //currentNoteColor = selectedButton.getTextFill().toString().substring(2,8);
        for (RadioButton button : instrumentButtons) {
            if (button.selectedProperty().get() && !button.equals(selectedButton)) {
                button.setSelected(false);
            }
        }
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
            NoteBox noteBox = (NoteBox) musicNotesArray.get(i);
            int noteLength = (int) noteBox.getWidth();
            int startTick = (int)noteBox.getX();
            int pitch = 128 - (int) noteBox.getY() / 10;
            
            //default is piano
            int channel = 0;
            int instrumentNum = 0;
            
            
            // TODO: get this code
            // player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + c, i, 0, s, t);
            // to be use here in accordance to her advice
            
            System.out.println("curr instrument: " + noteBox.instrument);
            
            switch (noteBox.instrument) {
                case "Piano": 
                    channel = 0;
                    instrumentNum = 0;
                    break;
                case "Harpsicord": 
                    channel = 1;
                    instrumentNum = 6;
                    break;
                case "Marimba": 
                    channel = 2;
                    instrumentNum = 12;
                    break;
                case "ChurchOrgan": 
                    channel = 3;
                    instrumentNum = 19;
                    break;
                case "Accordion": 
                    channel = 4;
                    instrumentNum = 21;
                    break;
                case "Guitar": 
                    channel = 5;
                    instrumentNum = 24;
                    break;
                case "Violin": 
                    channel = 6;
                    instrumentNum = 40;
                    break;
                case "FrenchHorn": 
                    channel = 7;
                    instrumentNum = 60;
                    break;
                default:
                    System.out.println("No cases matched");                        
            }
            
            //[0,6,12,19,21,24,40,60];
            
            //for (int i = 0; i < 7; i++) {
                
            //}
            
            System.out.println("channel: " + channel);  
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + channel, instrumentNum, 0, startTick, 0);
            player.addNote(pitch, VOLUME, startTick, noteLength, channel, 0);
        }
    }
    
    public void deleteNote(NoteBox note){
        int index = musicNotesArray.indexOf(note);
        musicNotesArray.remove(index);        
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
