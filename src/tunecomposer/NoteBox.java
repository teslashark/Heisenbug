
package tunecomposer;

import java.awt.Point;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class NoteBox extends Items  {
    
    /**
     * The instrument to be represented by the NoteBox
     */
    protected final String instrument;
    
    /**
     * the default width of a newly created NoteBox
     */
    private final int initialBoxWidth = 100;
    
    /**
     * The height of a noteBox
     */
    private final int boxHeight = 10;
    
    /**
     * The rectangle to visually represent the NoteBox
     */
    public Rectangle rectangle;
    
    /**
     * NoteBox Constructor
     * @param instrument A string of the desired instrument
     * @param event the mouse event
     */
    public NoteBox(String instrument,MouseEvent event) {
        this.instrument = instrument;
        rectangle.setId("noteBox" + instrument);
        this.rectangle = new Rectangle(initialBoxWidth, boxHeight);
        //TODO: bring up to spec is selected default
        this.isSelected = false;
        //ensure rectangle doesn't go off screen
        if (event.getX() > 1900) { 
            rectangle.setX(1900);            
        }
        else {
            rectangle.setX(event.getX());
        }
        //snap Y coordinate between horizontal lines in composer
        rectangle.setY(Math.round(event.getY() / 10) * 10);
    }
    
    public String getInstrument(){
        return instrument;
    }
    
    /**
     * Get method for the rectangle
     * @return the rectangle representing the NoteBox
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
   
    /**
     * Changes the width of a noteBox based of an incrementation of size
     * @param sizeDifference the difference  of width between the new note box and the old one
     */
    public void changeNoteBoxLength(int sizeDifference) {
        this.rectangle.setWidth(this.rectangle.getWidth() + sizeDifference);
        this.stretchZone.setX(rectangle.getX() + rectangle.getWidth() - 5);
        this.dragZone.setWidth(this.getWidth()-5);
    }
    
    // TODO: make it so that when the mouse is unclicked on dragging it snaps
    // in rather than making it snap in while dragging
    public void repositionNoteBox(int newXCoordinate, int newYCoordinate){
        // don't let the user make a note go offscreen
        rectangle.setX(newXCoordinate > 1900 ? 1900 : newXCoordinate);
        // snap Y coordinate between horizontal lines in composer
        rectangle.setY(Math.round(newYCoordinate / 10) * 10);
        stretchZone.setX(rectangle.getX() + rectangle.getWidth() -5);
        stretchZone.setY(rectangle.getY());
        dragZone.setX(rectangle.getX());
        dragZone.setY(rectangle.getY());
        
    }
    
    /**
     * marks the note as selected
     */
    public void markNote(){
        this.isSelected = true;
        rectangle.setStyle("-fx-stroke: red; -fx-stroke-width: 3;");
    }
    
    /**
     * unmarks the note as selected
     */
    public void unmarkNote(){
        this.isSelected = false;
        rectangle.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
    }
    
    /**
     * Checks to see whether a given point is inside the notebox
     * @param point the point to be tested
     * @return returns true if the point is within the box, else false
     */
    public boolean pointIsInNoteBox(Point point) {
        
        boolean xValInRange = 
                (point.x >= rectangle.getX() &&
                point.x <= rectangle.getX() + this.getWidth());
        
        boolean yValInRange = 
                (point.y >= rectangle.getY() &&
                point.y <= rectangle.getY() + this.boxHeight);
        
        return (xValInRange && yValInRange);
    }
    

    public static boolean isOverlapping(int max1, int min1, int max2, int min2) {
        return (max1 >= min2 && max2 >= min1);
    }
    
    /**
     * Checks to see if the notebox is within a given rectangle (used for the selection rectangle)
     * @param topLeft Top left corner of the other rectangle
     * @param bottomRight Bottom right corner of the other rectangle
     * @return returns true is the note box is within the other rectangle, else false
     */
    public boolean isInRect(Point topLeft, Point bottomRight) {
        
        int noteXMax = (int)(rectangle.getX() + rectangle.getWidth());
        int noteXMin = (int)rectangle.getX();
        int noteYMax = (int)(rectangle.getY() + rectangle.getHeight());
        int noteYMin = (int)rectangle.getY();
        
        int selectBoxXMax = (bottomRight.x > topLeft.x) ? bottomRight.x : topLeft.x;
        int selectBoxXMin = (bottomRight.x < topLeft.x) ? bottomRight.x : topLeft.x;
        int selectBoxYMax = (topLeft.y < bottomRight.y) ? bottomRight.y : topLeft.y;
        int selectBoxYMin = (topLeft.y > bottomRight.y) ? bottomRight.y : topLeft.y;
        
        boolean isYOverlapping = isOverlapping(noteYMax, noteYMin, selectBoxYMax, selectBoxYMin);
        boolean isXOverlapping = isOverlapping(noteXMax, noteXMin, selectBoxXMax, selectBoxXMin);
        
        return isYOverlapping && isXOverlapping;
        
    }

     private void onMouseDragged(MouseEvent e) {
        Point start = new Point((int)e.getX(),(int)e.getY());
        if (pointIsInRectangle(start, super.getStretchZone())){
            //add the functionality from the sample solution
        }
     }
    public static boolean pointIsInRectangle(Point point, Rectangle rect) {
        boolean xValInRange = 
                (point.x >= rect.getX() &&
                point.x <= rect.getX() + rect.getWidth());
       
        boolean yValInRange = 
                (point.y >= rect.getY() &&
                point.y <= rect.getY() + rect.getHeight());
        
        return (xValInRange && yValInRange);
    }
    
}
