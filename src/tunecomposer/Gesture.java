
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
     * @param notes
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
            if (currentNote.rectangle.getY()<gesRectangle.getY()){
                gesRectangle.setY(currentNote.rectangle.getY());
            }
       }
        for (int i=0;i<notes.size();i++){
            currentNote = (NoteBox) notes.get(i);

            if ((currentNote.getX()+currentNote.getWidth())>(gesRectangle.getX()+gesRectangle.getWidth())){
                gesRectangle.setWidth(currentNote.getX()+currentNote.getWidth()-gesRectangle.getX());
            }
            if ((currentNote.rectangle.getY()+10)>(gesRectangle.getY()+gesRectangle.getHeight())){
                gesRectangle.setHeight(currentNote.rectangle.getY()+10-gesRectangle.getY());
            }
        }

    }
    
    public ArrayList<NoteBox> getGestureNotes() {
        return gestureNotes;
    }
    
    /**
     * Changes the width of a noteBox based of an incrementation of size
     * @param sizeDifference the difference  of width between the new note box and the old one
     */
    public void changeGestureLength(int sizeDifference) {
        this.gesRectangle.setWidth(this.gesRectangle.getWidth() + sizeDifference);
        this.stretchZone.setX(gesRectangle.getX() + gesRectangle.getWidth() - 5);
        this.dragZone.setWidth(this.getWidth()-5);
    }
    
    // TODO: make it so that when the mouse is unclicked on dragging it snaps
    // in rather than making it snap in while dragging
    public void repositionGesture(int newXCoordinate, int newYCoordinate){
        // don't let the user make a note go offscreen
        gesRectangle.setX(newXCoordinate > 1900 ? 1900 : newXCoordinate);
        // snap Y coordinate between horizontal lines in composer
        gesRectangle.setY(Math.round(newYCoordinate / 10) * 10);
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
                (point.x >= gesRectangle.getX() &&
                point.x <= gesRectangle.getX() + this.getWidth());
        
        boolean yValInRange = 
                (point.y >= gesRectangle.getY() &&
                point.y <= gesRectangle.getY() + this.gesHeight);
        
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
        
        int gesXMax = (int)(gesRectangle.getX() + gesRectangle.getWidth());
        int gesXMin = (int)gesRectangle.getX();
        int gesYMax = (int)(gesRectangle.getY() + gesRectangle.getHeight());
        int gesYMin = (int)gesRectangle.getY();
        
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