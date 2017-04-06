/*
 * CS 300-A, 2017S
 */
package tunecomposer;

import java.awt.Point;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    
    protected ArrayList<Items> composerItems = new ArrayList<Items>(); 
    
    protected ArrayList<Items> composerItemsToRemove = new ArrayList<Items>(); 

    private ArrayList<NoteBox> selectedNotes = new ArrayList<NoteBox>();

    //private ArrayList<Items> selectedItems = new ArrayList<Items>();
    
    private ArrayList<Gesture> selectedGestures = new ArrayList<Gesture>();
    
    private ArrayList gesturesArray = new ArrayList();
    
    private Rectangle selectionRectangle;
   
    private Gesture gesture;
    
    private boolean validGestureMove = false;
        
    private Point gestureRelativeFocalPoint;
    
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
    
    private boolean noteDrag;
    private boolean noteStretch;
    private boolean gesDrag;
    private boolean gesStretch;

    private double startingPointX;
    private double startingPointY;
    private double dragPointX;
    private double dragPointY;
        
    protected void modifyAllGesturesNotes(NoteBox selected, String switchto){
        
        ArrayList<NoteBox> GestureNotes = new ArrayList<NoteBox>();

        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                GestureNotes = currentGesture.getGestureNotes();
                
                if(GestureNotes.contains(selected)){
                    
                    if(switchto=="selected") {
                            currentGesture.markGes();
                            if(!selectedGestures.contains(currentGesture)) { selectedGestures.add(currentGesture); }
                    }
                    if(switchto=="unselected") {
                            currentGesture.unmarkGes();
                            if(selectedGestures.contains(currentGesture)) { selectedGestures.remove(currentGesture); }
                    }

                    for (NoteBox notefound : GestureNotes) {
                        if(switchto=="selected") {
                            notefound.markNote();
                            if(!selectedNotes.contains(notefound)) { selectedNotes.add(notefound); }
                        }
                        if(switchto=="unselected") {
                            notefound.unmarkNote();
                            if(selectedNotes.contains(notefound)) { selectedNotes.remove(notefound); }
                        }
                    }
                    break;
                }
            }
        }            
    }
    
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
        Point startingPoint = new Point((int)startingPointX,(int)startingPointY);
        
        NoteBox currentNote;
        Gesture currentGes;
        
        noteStretch = false;
        noteDrag = false;
        gesStretch = false;
        gesDrag = false;
        this.updateSelected();
        for (Items arrayItem : selectedNotes){    
            if(arrayItem instanceof Gesture){
                currentGes = (Gesture) arrayItem;
                for (NoteBox currentNoteBox: currentGes.getGestureNotes()){
                    if (pointIsInRectangle(startingPoint,currentNoteBox.getDragZone())){
                        gesDrag = true;
                    }else if (pointIsInRectangle(startingPoint,currentNoteBox.getStretchZone())) {
                        gesStretch=true;
                    }
                }
                /*if (pointIsInRectangle(startingPoint,currentGes.getDragZone())) {
                    gesDrag=true;
                }*/
            }else if(arrayItem instanceof NoteBox){
                currentNote = (NoteBox) arrayItem; 
                if (pointIsInRectangle(startingPoint,currentNote.getDragZone())) {
                    noteDrag=true;
                }
                else if (pointIsInRectangle(startingPoint,currentNote.getStretchZone())) {
                    noteStretch=true;
                }
            }

        }
        if (gesDrag||gesStretch){
            noteDrag=false;
            noteStretch = false;
        }
        System.out.println(noteDrag);
        System.out.println(noteStretch);
        System.out.println(gesDrag);
        System.out.println(gesStretch);
        dragPointX = (int)event.getX();
        dragPointY = (int)event.getY();
        this.updateSelected();
        
        
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
        Gesture currentGes;
        Gesture currentSelectedGes;

//        Point startingPoint = new Point((int)startingPointX, (int)startingPointY);
        this.updateSelected();

        if (gesStretch){
            for (int j=0; j<selectedGestures.size();j++){
                currentSelectedGes = (Gesture)selectedGestures.get(j);
                int changeInLength = (int)event.getX() - (int)dragPointX;
                for (NoteBox currentGestureNote: currentSelectedGes.getGestureNotes()){
                    currentGestureNote.changeNoteBoxLength(changeInLength);
                }
                currentSelectedGes.changeGestureLength(changeInLength);
            }
            dragPointX = (int)event.getX();
            dragPointY = (int)event.getY();
        }else if (gesDrag) {        
            for (int j=0; j<selectedGestures.size();j++){
                currentSelectedGes = (Gesture)selectedGestures.get(j);

                int xpos = currentSelectedGes.getX() + ( (int)event.getX() - (int)dragPointX );
                int ypos = currentSelectedGes.getY() + ( (int)event.getY() - (int)dragPointY );
                for (NoteBox currentGestureNote: currentSelectedGes.getGestureNotes()){
                    currentGestureNote.repositionNoteBox(xpos,ypos);
                }
                System.out.println("repos");
                currentSelectedGes.repositionGesture(xpos,ypos);
            }
            dragPointX = (int)event.getX();
            dragPointY = (int)event.getY();
        } else if (noteStretch) {
            for (int j=0; j<selectedNotes.size();j++){
                currentSelectedNote = (NoteBox)selectedNotes.get(j);

                int changeInLength = (int)event.getX() - (int)dragPointX;
                currentSelectedNote.changeNoteBoxLength(changeInLength);

            }
            dragPointX = (int)event.getX();
            dragPointY = (int)event.getY();
        } else if (noteDrag) {
            for (int j=0; j<selectedNotes.size();j++){
                currentSelectedNote = (NoteBox)selectedNotes.get(j);

                int xpos = currentSelectedNote.getX() + ( (int)event.getX() - (int)dragPointX );
                int ypos = currentSelectedNote.getY() + ( (int)event.getY() - (int)dragPointY );
                currentSelectedNote.repositionNoteBox(xpos,ypos);


            }
            dragPointX = (int)event.getX();
            dragPointY = (int)event.getY();
        }else{
            Point topLeft = new Point((int)startingPointX,(int)startingPointY);
            Point bottomRight = new Point((int)event.getX(), (int)event.getY());
            this.selectionRectangle.setX(startingPointX);
            this.selectionRectangle.setY(startingPointY);
            resizeSelectionRectangle(selectionRectangle,event); 

            for (Items arrayItem : composerItems) {
                if (arrayItem instanceof NoteBox)
                {
                    currentNote = (NoteBox) arrayItem;
                    if (currentNote.isInRect(topLeft, bottomRight)) {
                        currentNote.markNote();
                        modifyAllGesturesNotes(currentNote, "selected");
                    } else if(!event.isControlDown()) {
                        currentNote.unmarkNote();  
                        modifyAllGesturesNotes(currentNote, "unselected");
                    }
                }
            }
        }
        updateSelected();
    }
    
    private void updateYPos(){        
        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                NoteBox currentNote;
                currentNote = (NoteBox) arrayItem;
                currentNote.getRectangle().setY((currentNote.getY()/10)*10);
                currentNote.getStretchZone().setY((currentNote.getY()/10)*10);
                currentNote.getDragZone().setY((currentNote.getY()/10)*10);
                    
            }
            
            /*if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                if (currentGesture.getIsSelected()) {
                    selectedNotes.add(currentGesture);
                }
                
                currentNote = (NoteBox)composerItems.get(i);
                currentNote.getRectangle().setY((currentNote.getY()/10)*10);
                currentNote.getStretchZone().setY((currentNote.getY()/10)*10);
                currentNote.getDragZone().setY((currentNote.getY()/10)*10);
                    
            }*/
           
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
        this.updateSelected();
        this.updateYPos();
        event.consume();
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
       Gesture currentGesture;

       boolean hasNoConflictWithNote = true;
       int roundedYCoordinate = Math.round((int)event.getY() / 10) * 10;
       Point clickPoint = new Point((int)event.getX(), roundedYCoordinate);
       
       
       for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                currentNote = (NoteBox) arrayItem;
                if(currentNote.pointIsInNoteBox(clickPoint)){
                    hasNoConflictWithNote = false;
                    if (event.isControlDown()) {
                        if (currentNote.getIsSelected()) {
                            currentNote.unmarkNote();
                            modifyAllGesturesNotes(currentNote, "unselected");
                        } else {
                            currentNote.markNote();
                            modifyAllGesturesNotes(currentNote, "selected");
                        }
                    break;
                } 
                    
                unselectAll();
                currentNote.markNote();
                modifyAllGesturesNotes(currentNote, "selected");
                break;
                } 
                    
            }
    
            /*
            ArrayList<NoteBox> GestureNotes = new ArrayList<NoteBox>();

            if (arrayItem instanceof Gesture)
            {
                currentGesture = (Gesture) arrayItem;
                if(currentGesture.pointIsInGesture(clickPoint)){
                    hasNoConflictWithNote = false;
                    if (event.isControlDown()) {
                        if (currentGesture.getIsSelected()) {
                            currentGesture.unmarkGes();
                            GestureNotes = currentGesture.getGestureNotes();
                            for (NoteBox gestureItem : GestureNotes) {
                                selectedNotes.remove(gestureItem);
                                gestureItem.unmarkNote();
                            }                            
                        } else {
                            currentGesture.markGes();
                            GestureNotes = currentGesture.getGestureNotes();
                            for (NoteBox gestureItem : GestureNotes) {
                                //selectedNotes.add(gestureItem);
                                gestureItem.markNote();
                            } 
                            
                        }
                    break;
                } 
                    
                unselectAll();
                currentGesture.markGes();
                break;
                } 
                    
            } 
            */
            
        }    
                 
       if (hasNoConflictWithNote) {           
            //this.selector.unselectAll(musicNotesArray);
            NoteBox noteBox = new NoteBox(selectedInstrument, event);
            if (event.isControlDown()) {
                noteBox.markNote();
            }else{
                unselectAll();
            }
            composerItems.add(noteBox);
            musicPane.getChildren().add(noteBox.getRectangle());
       }
       
       updateSelected();
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
        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                NoteBox currentNote;
                currentNote = (NoteBox) arrayItem;
                currentNote.markNote();
            }
            
            if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                currentGesture.markGes();
            }
        }   
    }
    
    /**
     * Handles when delete is clicked in the edit menu, removing a specified note box
     * from the musicNotesArray collection.
     * @param event 
     */
    @FXML
    protected void handleDeleteClicked(ActionEvent event) {      
                
        System.out.println("Delete triggered");
        System.out.println("Composer Items" + composerItems);

        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                NoteBox currentNote;
                currentNote = (NoteBox) arrayItem;
                if (currentNote.getIsSelected()) {
                    deleteNote(currentNote);
                    composerItemsToRemove.add(currentNote);
                }
            }
            
            if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                if (currentGesture.getIsSelected()) {
                    // Delete a gesture detected
                }
            }
        }    
        composerItems.removeAll(composerItemsToRemove);
        System.out.println("Composer Items" + composerItems);
        
        System.out.println("Selected Notes" + selectedNotes);
        System.out.println("Selected Gestur" + selectedGestures);


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
        playBarObj.playAnimation(composerItems); // composerItems musicNotesArray
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
            Gesture gesture = new Gesture(selectedNotes);
            musicPane.getChildren().add(gesture.gesRectangle);
            System.out.println(gesture);
            composerItems.add(gesture);
            //System.out.println(composerItems);
            unselectAll();
    }
     /**
     * handler for "Un Group" menuItem to create a gesture
     * @param event 
     */
    @FXML
    protected void handleUngroupMenuItemAction(ActionEvent event) {
            System.out.println("Selected For Ungroup" + selectedGestures);
            for (Gesture arrayItem : selectedGestures) {
                 musicPane.getChildren().remove(arrayItem.gesRectangle); 
                 composerItems.remove(arrayItem);
                 arrayItem = null;
            }
    }    
    
    /**
     * takes array of rectangles representing notes, and adds them to player.
     * assumes pane height is 1280px high.
     * all objects in musicNotesArray must be of type javafx shape.
     */
    
    private void addNotesArrayToMidiPlayer() {
        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
            NoteBox noteBox;
            noteBox = (NoteBox) arrayItem;
            
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
            
            player.addMidiEvent(ShortMessage.PROGRAM_CHANGE + channel, instrumentNum, 0, startTick, 0);
            player.addNote(pitch, VOLUME, startTick, noteLength, channel, 0);
            }
        }
    }
    
    /**
     * Deletes a note box from the musicNotesArray, as well as removes the rectangle from the
     * music pane.
     * @param note The NoteBox to be deleted. 
     */
    public void deleteNote(NoteBox note){
        //int index = composerItems.indexOf(note);
        musicPane.getChildren().remove(note.getRectangle());
    }

    public void deleteGesture(Gesture gesture){
        //int index = composerItems.indexOf(note);
        musicPane.getChildren().remove(gesture.getRectangle());
    }
    
    /**
     * Checks all NoteBoxes in the musicNoteArray to update the ArrayList of selectedNotes
     */
    public void updateSelected() {
        selectedNotes.clear();
        selectedGestures.clear();
        
        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                NoteBox currentNote;
                currentNote = (NoteBox) arrayItem;
                if (currentNote.getIsSelected()) {
                    selectedNotes.add(currentNote);
                }
            }
            
            ArrayList<NoteBox> GestureNotes = new ArrayList<NoteBox>();
            
            if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                GestureNotes = currentGesture.getGestureNotes();
                
                if (currentGesture.getIsSelected()) {
                    for (NoteBox gestureItem : GestureNotes) {
                        selectedNotes.add(gestureItem);
                    }
                    selectedGestures.add(currentGesture);
                }
            }
        }    
                
//        System.out.println("Selected Gestures" + selectedGestures);                
        
    }
    
    /**
     * deselects all NoteBoxes in the musicNotesArray
     */
    public void unselectAll() {
        
        selectedNotes.clear();
        selectedGestures.clear();

        for (Items arrayItem : composerItems) {
            if (arrayItem instanceof NoteBox)
            {
                NoteBox currentNote;
                currentNote = (NoteBox) arrayItem;
                currentNote.unmarkNote();
            }
            
            if (arrayItem instanceof Gesture)
            {
                Gesture currentGesture;
                currentGesture = (Gesture) arrayItem;
                currentGesture.unmarkGes();
            }
        }
//        System.out.println("Selected Gestures" + selectedGestures);

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
