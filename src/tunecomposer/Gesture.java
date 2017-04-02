package tunecomposer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ggsha
 */
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

/**
 *
 * @author limpicbc
 */
public class Gesture {
    
    /**
     * The rectangle to visually represent this Gesture
     */
    protected Rectangle rectangle;
    
    /**
     * returns true if the Gesture is selected
     */
    private boolean isSelected;
    
    /**
     * the initial width of a gesture; 
     * currently just for representing a click, subject to change
     */
    private final int initialGesWidth = 1;
    
    /**
     * The initial height of a gesture
     */
    private final int gesHeight = 1;
    
    private Rectangle stretchZone;
    
    private Rectangle dragZone;
    
    private int gesCount;
    /**
     * gesture Constructor
     * @param event the mouse event
     */
    public Gesture(MouseEvent event) {
        this.gesCount = 0;
        this.rectangle = new Rectangle(initialGesWidth, gesHeight);
        //TODO: bring up to spec is selected default
        this.isSelected = false; 
        rectangle.setId("Gesture" + (gesCount + 1));
        gesCount += 1;
        //ensure rectangle doesn't go off screen
        if (event.getX() > 1900) { 
            rectangle.setX(1900);            
        }
        else {
            rectangle.setX(event.getX());
        }
        //snap Y coordinate between horizontal lines in composer
        rectangle.setY(Math.round(event.getY() / 10) * 10);
        
        stretchZone = new Rectangle(this.getX() + (this.getWidth() - 5),this.getY(),5, this.getGesHeight());
        dragZone = new Rectangle(this.getX(), this.getY(),this.getWidth()-5, this.getGesHeight());

    }
    
    /**
     * Returns a rectangle representative of the area of the gesture which can be clicked on to
     * activate the resizing action.
     * @return The rectangle representing the place to click to resize the NoteBox
     */
    public Rectangle getStretchZone() {
        return stretchZone;
    }
    
    /**
     * Returns a rectangle representative of the area of the notebox which can be clicked on to
     * activate the drag to move action.
     * @return The rectangle representing the place to click to drag and move the NoteBox
     */
    public Rectangle getDragZone() {
        return dragZone;
    }
    
    /**
     * Get method for the rectangle
     * @return the rectangle representing the NoteBox
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
    
    /**
     * Gives the x-coordinate of the NoteBox
     * @return the x-coordinate
     */
    public int getX() {
        return (int) this.rectangle.getX();
    }
    
    /**
     * Gives the y-Coordinate of the NoteBox
     * @return the y-coordinate
     */
    public int getY() {
        return (int) this.rectangle.getY();
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
        return (int) this.rectangle.getWidth();
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
        this.rectangle.setWidth(this.rectangle.getWidth() + sizeDifference);
        this.stretchZone.setX(rectangle.getX() + rectangle.getWidth() - 5);
        this.dragZone.setWidth(this.getWidth()-5);
    }
    
    // TODO: make it so that when the mouse is unclicked on dragging it snaps
    // in rather than making it snap in while dragging
    public void repositionGesture(int newXCoordinate, int newYCoordinate){
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
    public void markGes(){
        this.isSelected = true;
        rectangle.setStyle("-fx-stroke: red; -fx-stroke-width: 3;");
    }
    
    /**
     * unmarkes the note as selected
     */
    public void unmarkGes(){
        this.isSelected = false;
        rectangle.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
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
     * Checks to see if the gesture is within a given rectangle (used for the selection rectangle)
     * @param topLeft Top left corner of the other rectangle
     * @param bottomRight Bottom right corner of the other rectangle
     * @return returns true is the note box is within the other rectangle, else false
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
    
    
}
