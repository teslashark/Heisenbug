
package tunecomposer;

import java.awt.Point;
import java.util.ArrayList;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLACK;
import javafx.scene.shape.Rectangle;
import tunecomposer.NoteBox;

public class Gesture extends Items {
    
    /**
     * The gesRectangle to visually represent this Gesture
     */
    protected Rectangle gesRectangle = new Rectangle(2000,1280,0,0);
   
    /**
     * the initial width of a gesture; 
     * currently just for representing a click, subject to change
     */
    private final int initialGesWidth = 1;
    
    /**
     * The initial height of a gesture
     */
    private final int gesHeight = 1;
    
    private int gesCount;
    
    private ArrayList<NoteBox> gestureNotes = new ArrayList<NoteBox>();
    
    /**
     * gesture Constructor
     * @param event the mouse event
     */
    public Gesture(ArrayList<NoteBox> notes) {
        for (NoteBox currentNote: notes){
            gestureNotes.add(currentNote);
        }
        this.gesCount = 0;
        this.gesRectangle = new Rectangle(1999,1279,1,1);
        //TODO: bring up to spec is selected default
        this.isSelected = false; 
        gesRectangle.setId("Gesture" + (gesCount + 1));
        gesCount += 1;

        gesRectangle.setStroke(BLACK);
        gesRectangle.setStrokeWidth(1.5);
        gesRectangle.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
        gesRectangle.setFill(Color.TRANSPARENT);
        //Set dimentions and location of the gesRectangle based on the arraylist
        NoteBox currentNote;
        
        for (int i=0;i<notes.size();i++){
            currentNote = (NoteBox) notes.get(i);
            if (currentNote.getX()<gesRectangle.getX()){
                gesRectangle.setX(currentNote.getX());
            }
            if (currentNote.getY()<gesRectangle.getY()){
                gesRectangle.setY(currentNote.getY());
            }
       }
        for (int i=0;i<notes.size();i++){
            currentNote = (NoteBox) notes.get(i);

            if ((currentNote.getX()+currentNote.getWidth())>(gesRectangle.getX()+gesRectangle.getWidth())){
                gesRectangle.setWidth(currentNote.getX()+currentNote.getWidth()-gesRectangle.getX());
            }
            if ((currentNote.getY()+10)>(gesRectangle.getY()+gesRectangle.getHeight())){
                gesRectangle.setHeight(currentNote.getY()+10-gesRectangle.getY());
            }
        }

        //Set up strech and drag zones
        stretchZone = new Rectangle();
        dragZone = new Rectangle();

    }
    
    public ArrayList<NoteBox> getGestureNotes() {
        return gestureNotes;
    }
    
    /**
     * Returns a gesRectangle representative of the area of the gesture which can be clicked on to
     * activate the resizing action.
     * @return The gesRectangle representing the place to click to resize the NoteBox
     */
    public Rectangle getStretchZone() {
        return stretchZone;
    }
    
    /**
     * Returns a gesRectangle representative of the area of the notebox which can be clicked on to
     * activate the drag to move action.
     * @return The gesRectangle representing the place to click to drag and move the NoteBox
     */
    public Rectangle getDragZone() {
        return dragZone;
    }
    
    /**
     * Get method for the gesRectangle
     * @return the gesRectangle representing the NoteBox
     */
    public Rectangle getRectangle() {
        return gesRectangle;
    }
    
    /**
     * Gives the height of the noteBox
     * @return the height of the noteBox
     */
    public int getGesHeight() {
        return gesHeight;
    }
    
    /**
     * Gives the width of the notebox
     * @return the width of the NoteBox
     */
    public int getWidth() {
        return (int) this.gesRectangle.getWidth();
    }
    
    /**
     * Gives the x-coordinate of the NoteBox
     * @return the x-coordinate
     */
    public int getX() {
        return (int) this.gesRectangle.getX();
    }
    
    /**
     * Gives the y-Coordinate of the NoteBox
     * @return the y-coordinate
     */
    public int getY() {
        return (int) this.gesRectangle.getY();
    }
    
    /**
     * Returns whether the gesture is marked as selected
     * @return true if the gesture is selected, else false
     */
    public boolean getIsSelected() {
        return isSelected;
    }
    
    /**
     * Changes the width of a noteBox based of an incrementation of size
     * @param sizeDifference the difference  of width between the new note box and the old one
     */
    public void changeGestureLength(int sizeDifference) {
        this.gesRectangle.setWidth(this.gesRectangle.getWidth() + sizeDifference);
        this.stretchZone.setX(gesRectangle.getX() + gesRectangle.getWidth() - 5);
        this.dragZone.setWidth(this.getWidth()-5);
        int furthestNoteEdge=0;
        for(NoteBox noteBox: this.getGestureNotes()){
            int noteEdge = (int)noteBox.getX()+(int)noteBox.getWidth();
            if (noteEdge>furthestNoteEdge){
                furthestNoteEdge = not  eEdge;
            }
        }
        int gesEdge = (int)this.gesRectangle.getX()+(int)this.gesRectangle.getWidth();
        if (gesEdge<furthestNoteEdge){
                this.gesRectangle.setWidth(furthestNoteEdge-this.gesRectangle.getX());
            }
    }
    
    // TODO: make it so that when the mouse is unclicked on dragging it snaps
    // in rather than making it snap in while dragging
    public void repositionGesture(int newXCoordinate, int newYCoordinate){
        // don't let the user make a note go offscreen

        gesRectangle.setX(newXCoordinate > 1900 ? 1900 : newXCoordinate);
     
        // snap Y coordinate between horizontal lines in composer
        gesRectangle.setY(newYCoordinate);
        stretchZone.setX(gesRectangle.getX() + gesRectangle.getWidth() -5);
        stretchZone.setY(gesRectangle.getY());
        dragZone.setX(gesRectangle.getX());
        dragZone.setY(gesRectangle.getY());
        
    }
    
    /**
     * marks the note as selected
     */
    public void markGes(){
        this.isSelected = true;
        gesRectangle.setStyle("-fx-stroke: red; -fx-stroke-width: 3;");
    }
    
    /**
     * unmarks the note as selected
     */
    public void unmarkGes(){
        this.isSelected = false;
        gesRectangle.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
    }
    
    /**
     * Checks to see whether a given point is inside the notebox
     * @param point the point to be tested
     * @return returns true if the point is within the box, else false
     */
    public boolean pointIsInGesture(Point point) {
        
        boolean xValInRange = 
                (point.x >= this.getX() &&
                point.x <= this.getX() + this.getWidth());
        
        boolean yValInRange = 
                (point.y >= this.getY() &&
                point.y <= this.getY() + this.gesHeight);
        
        return (xValInRange && yValInRange);
    }
    

    public static boolean isOverlapping(int max1, int min1, int max2, int min2) {
        return (max1 >= min2 && max2 >= min1);
    }
    
    /**
     * Checks to see if the gesture is within a given gesRectangle (used for the selection gesRectangle)
     * @param topLeft Top left corner of the other gesRectangle
     * @param bottomRight Bottom right corner of the other gesRectangle
     * @return returns true is the note box is within the other gesRectangle, else false
     */
    public boolean isInRect(Point topLeft, Point bottomRight) {
        
        int gesXMax = this.getX() + this.getWidth();
        int gesXMin = this.getX();
        int gesYMax = this.getY() + this.gesHeight;
        int gesYMin = this.getY();
        
        int selectGesXMax = (bottomRight.x > topLeft.x) ? bottomRight.x : topLeft.x;
        int selectGesXMin = (bottomRight.x < topLeft.x) ? bottomRight.x : topLeft.x;
        int selectGesYMax = (topLeft.y < bottomRight.y) ? bottomRight.y : topLeft.y;
        int selectGesYMin = (topLeft.y > bottomRight.y) ? bottomRight.y : topLeft.y;
        
        boolean isYOverlapping = isOverlapping(gesYMax, gesYMin, selectGesYMax, selectGesYMin);
        boolean isXOverlapping = isOverlapping(gesXMax, gesXMin, selectGesXMax, selectGesXMin);
        
        return isYOverlapping && isXOverlapping;
        
    }
    
    public void unGesture(){
        gestureNotes.clear();
    }
    
}
