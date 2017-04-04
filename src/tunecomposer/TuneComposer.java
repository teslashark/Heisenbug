/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.awt.Point;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.sound.midi.ShortMessage;
import tunecomposer.NoteBox;
import tunecomposer.Gesture;

public class TuneComposer extends Application {
    /**
     * Contains the rectangle objects that represent 
     * the musical notes in the UI.
     */
    protected ArrayList musicNotesArray = new ArrayList();
    
    protected ArrayList gesturesArray = new ArrayList();
        
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
    
    private ArrayList<NoteBox> selectedNotes = new ArrayList<NoteBox>();

    private ArrayList<Gesture> selectedGestures = new ArrayList<Gesture>();

    private Rectangle selectionRectangle;
    

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
     * saves x-coordinate of start location of a drag
     */
    private double startingPointX;
    /**
     * saves y-coordinate of start of location of a drag
     */
    private double startingPointY;
    
    /**
     * Handles mouse pressed event, saving point in case the user drags the mouse, and 
     * begins the selection rectangle
     * @param event the mouse event
     */
    @FXML
    protected void handleOnMousePressedAction(MouseEvent event){
        selectionRectangle = new Rectangle();
        musicPane.getChildren().add(selectionRectangle);
        startingPointX = event.getX();
        startingPointY = event.getY();
    }
    
    /**
     * Handles the mouse drag, checking to see if notes fall within the selection box.
     * If so the notes become selected.
     * @param event the mouse event
     */
    @FXML
    protected void handleOnMouseDraggedAction(MouseEvent event){
        NoteBox currentNote;
        NoteBox currentSelectedNote;
        boolean stretchDrag = false;
        Point topLeft = new Point((int)startingPointX,(int)startingPointY);
        Point bottomRight = new Point((int)event.getX(), (int)event.getY());
        Point startingPoint = new Point((int)startingPointX, (int)startingPointY);
        
        for (int i=0; i<selectedNotes.size();i++){    
            currentNote = (NoteBox)selectedNotes.get(i); 
            Rectangle stretchZone = currentNote.getStretchZone();
            if (pointIsInRectangle(startingPoint, stretchZone)) {
                for (int j=0; j<selectedNotes.size();j++){
                    currentSelectedNote = (NoteBox)selectedNotes.get(j);
                    //Not sure why this doesn't change the length of the bars correctly
                    int changeInLength = (int)event.getX() - (int)startingPointX;
                    currentSelectedNote.changeNoteBoxLength(changeInLength);
                    
                }
            } else if (pointIsInRectangle(startingPoint, currentNote.getDragZone())) {
                for (int j=0; j<selectedNotes.size();j++){
                    currentSelectedNote = (NoteBox)selectedNotes.get(j);
                    //I don't know why this doesn't properly change the position,
                    //the coordinate change seems to check out
                    int xpos = currentSelectedNote.getX() + ( (int)event.getX() - startingPoint.x );
                    int ypos = currentSelectedNote.getY() + ( (int)event.getY() - startingPoint.y );
                    currentSelectedNote.repositionNoteBox(xpos,ypos);
                }
                stretchDrag=true;
                break;
            } 
        }
        this.updateSelected();
        
        if (!stretchDrag){
            this.selectionRectangle.setX(startingPointX);
            this.selectionRectangle.setY(startingPointY);
            resizeSelectionRectangle(selectionRectangle,event); 
        
            for (int i = 0; i < musicNotesArray.size(); i++) {
                currentNote = (NoteBox)musicNotesArray.get(i);

                if (currentNote.isInRect(topLeft, bottomRight)) {
                    currentNote.markNote();
                } else if(!event.isControlDown()) {
                    currentNote.unmarkNote();  
                }
            }
        }
    }
    
    /**
     * resizes a rectangle based on a mouse event, used for updating the size of the selection
     * rectangle.
     * @param rect the rectangle to be resized
     * @param e the mouse event
     */
    private void resizeSelectionRectangle(Rectangle rect,MouseEvent e) {
        rect.setWidth(e.getX()-startingPointX);
        rect.setHeight(e.getY()-startingPointY);
        //if the box is dragged up/left the width and height need to bechanged
        if (rect.getWidth()< 0){
            rect.setWidth(-1*rect.getWidth());
            rect.setX(rect.getX()- rect.getWidth());
        }
        if (rect.getHeight()< 0){
            rect.setHeight(-1*rect.getHeight());
            rect.setY(rect.getY()- rect.getHeight()); 
        }
        rect.setStroke(BLACK);
        rect.setStrokeWidth(1);
        rect.setFill(Color.TRANSPARENT);
    }

    /**
     * Handles mouse released, removing the selection rectangle from the screen.
     * @param event the mouse event
     */
    @FXML
    protected void handleOnMouseReleasedAction(MouseEvent event){
        musicPane.getChildren().remove(selectionRectangle);    
    }
    
    /**
     * Handles clicking on the pane to add NoteBoxs, clicking on notes for selection
     * and control clicking.
     * @param event 
     */
    @FXML
    protected void handleOnMouseClickAction(MouseEvent event){
       player.stop();
       player.clear();
       playBarObj.stopAnimation();
       this.updateSelected();
       NoteBox currentNote;
       boolean hasNoConflictWithNote = true;
       int roundedYCoordinate = Math.round((int)event.getY() / 10) * 10;
       Point clickPoint = new Point((int)event.getX(), roundedYCoordinate);
       for(int i = 0; i < musicNotesArray.size(); i++){
           currentNote = (NoteBox) musicNotesArray.get(i);
           if(currentNote.pointIsInNoteBox(clickPoint)){
              hasNoConflictWithNote = false;
              if (event.isControlDown()) {
                  if (currentNote.getIsSelected()) {
                      currentNote.unmarkNote();
                  } else {
                      currentNote.markNote();
                  }
                  break;
              } 
              unselectAll();
              currentNote.markNote();
              break;
           } 
       }       
        
       if (hasNoConflictWithNote) {           
            //this.selector.unselectAll(musicNotesArray);
            NoteBox noteBox = new NoteBox(selectedInstrument, event);
            if (event.isControlDown()) {
                noteBox.markNote();
            }else{
                unselectAll();
            }
            musicNotesArray.add(noteBox);
            musicPane.getChildren().add(noteBox.getRectangle());
       }
       
        
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
            if (button.equals(selectedButton))
                button.setSelected(true);
        }
    }
    
    /**
     * Handles when select all is clicked in the edit menu, selecting all note boxes.
     * @param event the mouse event
     */
    @FXML
    protected void handleSelectAllClicked(ActionEvent event) {
        NoteBox currentNote;
        for (int i = 0; i < musicNotesArray.size(); i++) {
            currentNote = (NoteBox)musicNotesArray.get(i);
            currentNote.markNote();
        }
    }
    
    /**
     * Handles when delete is clicked in the edit menu, removing a specified note box
     * from the musicNotesArray collection.
     * @param event 
     */
    @FXML
    protected void handleDeleteClicked(ActionEvent event) {
        NoteBox currentNote;
        for (int i=0; i < musicNotesArray.size();) {
            currentNote = (NoteBox)musicNotesArray.get(i);
            if (currentNote.getIsSelected()) {
                deleteNote(currentNote);
            } else {
                i++;
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
     * handler for "group" menuItem to create a gesture
     * @param event 
     */
    @FXML
    protected void handleGroupMenuItemAction(ActionEvent event) {
            System.out.println("Group Menu Click");
            System.out.println(selectedNotes);
            Gesture gesture = new Gesture(selectedNotes);
            musicPane.getChildren().add(gesture.rectangle);
            
    }
     /**
     * handler for "Un Group" menuItem to create a gesture
     * @param event 
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
             System.out.println("Ungroup Menu Click");
             System.out.println(selectedNotes);

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
                       
            switch (noteBox.getInstrument()) {
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
            }
            
            //[0,6,12,19,21,24,40,60];
            
            //for (int i = 0; i < 7; i++) {
                
            //}
            
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + channel, instrumentNum, 0, startTick, 0);
            player.addNote(pitch, VOLUME, startTick, noteLength, channel, 0);
        }
    }
    
    /**
     * Deletes a note box from the musicNotesArray, as well as removes the rectangle from the
     * music pane.
     * @param note The NoteBox to be deleted. 
     */
    public void deleteNote(NoteBox note){
        int index = musicNotesArray.indexOf(note);
        musicNotesArray.remove(index);  
        musicPane.getChildren().remove(note.getRectangle());
    }
    
    /**
     * Checks all NoteBoxes in the musicNoteArray to update the ArrayList of selectedNotes
     */
    public void updateSelected() {
        NoteBox currentNote;
        selectedNotes.clear();
        for (int i=0; i < musicNotesArray.size(); i++) {
            currentNote = (NoteBox)musicNotesArray.get(i);
            if (currentNote.getIsSelected()) {
                selectedNotes.add(currentNote);
            }
        }
   
        Gesture currentGesture;
        selectedGestures.clear();
        for (int i=0; i < gesturesArray.size(); i++) {
            currentGesture = (Gesture)gesturesArray.get(i);
            if (currentGesture.getIsSelected()) {
                selectedGestures.add(currentGesture);
            }
        }
        
        System.out.println(selectedGestures);
        
        
    }
    
    /**
     * deselects all NoteBoxes in the musicNotesArray
     */
    public void unselectAll() {
        selectedNotes.clear();
        NoteBox currentNote;
        for (int i=0; i < musicNotesArray.size(); i++) {
            currentNote = (NoteBox)musicNotesArray.get(i);
            currentNote.unmarkNote();
        }
    }
    
    /**
     * Determines whether a given point is inside the bound of a given rectangle
     * @param point the given point
     * @param rect the given rectangle
     * @return returns true if the point is inside of the rectangle, else returns false
     */
    public boolean pointIsInRectangle(Point point, Rectangle rect) {
        boolean xValInRange = 
                (point.x >= rect.getX() &&
                point.x <= rect.getX() + rect.getWidth());
       
        boolean yValInRange = 
                (point.y >= rect.getY() &&
                point.y <= rect.getY() + rect.getHeight());
        
        return (xValInRange && yValInRange);
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