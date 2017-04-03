/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.awt.Point;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import tunecomposer.TuneComposer;

/**
 *
 * @author limpicbc
 */
public class NoteBox {
    
    /**
     * The instrument to be represented by the NoteBox
     */
    protected final String instrument;
    
    /**
     * The rectangle to visually represent the NoteBox
     */
    protected Rectangle rectangle;
    
    /**
     * returns true if the NoteBox is marked as a selected note
     */
    private boolean isSelected;
    
    /**
     * the default width of a newly created NoteBox
     */
    private final int initialBoxWidth = 100;
    
    /**
     * The height of a noteBox
     */
    private final int boxHeight = 10;
    
    private Rectangle stretchZone;
    
    private Rectangle dragZone;
    
    /**
     * NoteBox Constructor
     * @param instrument A string of the desired instrument
     * @param event the mouse event
     */
    public NoteBox(String instrument, MouseEvent event) {
        this.instrument = instrument;
        this.rectangle = new Rectangle(initialBoxWidth, boxHeight);
        //TODO: bring up to spec is selected default
        this.isSelected = false; 
        rectangle.setId("noteBox" + instrument);
        //ensure rectangle doesn't go off screen
        if (event.getX() > 1900) { 
            rectangle.setX(1900);            
        }
        else {
            rectangle.setX(event.getX());
        }
        //snap Y coordinate between horizontal lines in composer
        rectangle.setY(Math.round(event.getY() / 10) * 10);
        
        stretchZone = new Rectangle(this.getX() + (this.getWidth() - 5),this.getY(),5, this.getBoxHeight());
        dragZone = new Rectangle(this.getX(), this.getY(),this.getWidth()-5, this.getBoxHeight());

    }
    
    /**
     * Returns a rectangle representative of the area of the notebox which can be clicked on to
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
    public int getBoxHeight() {
        return boxHeight;
    }
    
    /**
     * Gives the width of the notebox
     * @return the width of the NoteBox
     */
    public int getWidth() {
        return (int) this.rectangle.getWidth();
    }
    
    /**
     * Returns whether the NoteBox is marked as selected
     * @return true if the note box is selected, else false
     */
    public boolean getIsSelected() {
        return isSelected;
    }
    
    /**
     * Changes the width of a noteBox based of an incrimentation of size
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
        this.rectangle.setX(newXCoordinate > 1900 ? 1900 : newXCoordinate);
        // snap Y coordinate between horizontal lines in composer
        this.rectangle.setY(Math.round(newYCoordinate / 10) * 10);
        this.stretchZone.setX(rectangle.getX() + rectangle.getWidth() -5);
        this.stretchZone.setY(rectangle.getY());
        this.dragZone.setX(rectangle.getX());
        this.dragZone.setY(rectangle.getY());
        
    }
    
    /**
     * marks the note as selected
     */
    public void markNote(){
        this.isSelected = true;
        rectangle.setStyle("-fx-stroke: red; -fx-stroke-width: 3;");
    }
    
    /**
     * unmarkes the note as selected
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
                (point.x >= this.getX() &&
                point.x <= this.getX() + this.getWidth());
        
        boolean yValInRange = 
                (point.y >= this.getY() &&
                point.y <= this.getY() + this.boxHeight);
        
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
        
        int noteXMax = this.getX() + this.getWidth();
        int noteXMin = this.getX();
        int noteYMax = this.getY() + this.boxHeight;
        int noteYMin = this.getY();
        
        int selectBoxXMax = (bottomRight.x > topLeft.x) ? bottomRight.x : topLeft.x;
        int selectBoxXMin = (bottomRight.x < topLeft.x) ? bottomRight.x : topLeft.x;
        int selectBoxYMax = (topLeft.y < bottomRight.y) ? bottomRight.y : topLeft.y;
        int selectBoxYMin = (topLeft.y > bottomRight.y) ? bottomRight.y : topLeft.y;
        
        boolean isYOverlapping = isOverlapping(noteYMax, noteYMin, selectBoxYMax, selectBoxYMin);
        boolean isXOverlapping = isOverlapping(noteXMax, noteXMin, selectBoxXMax, selectBoxXMin);
        
        return isYOverlapping && isXOverlapping;
        
    }
    private Point start;
    private void onMousePressed(MouseEvent e) {
        Point start = new Point((int)e.getX(),(int)e.getY());    
    }

     private void onMouseDragged(MouseEvent e) {

        if (pointIsInRectangle(start, this.getStretchZone())){
            this.rectangle.setWidth(start.getX()-e.getX());
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
