/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.awt.Point;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author limpicbc
 */
public class NoteBox {
    
    protected final String instrument;
    
    protected Rectangle rectangle;
    
    private boolean isSelected;
    
    private final int initialBoxWidth = 100;
    
    private final int boxHeight = 10;
    
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
    }
    
    //TODO: rename to getXCoordinate???
    public int getX() {
        return (int) this.rectangle.getX();
    }
    
    public int getY() {
        return (int) this.rectangle.getY();
    }
    
    public int getWidth() {
        return (int) this.rectangle.getWidth();
    }
    
    public void changeNoteBoxLength(int newLength) {
        this.rectangle.setWidth(newLength);
    }
    
    // TODO: make it so that when the mouse is unclicked on dragging it snaps
    // in rather than making it snap in while dragging
    public void repositionNoteBox(int newXCoordinate, int newYCoordinate){
        // don't let the user make a note go offscreen
        rectangle.setX(newXCoordinate > 1900 ? 1900 : newXCoordinate);
        // snap Y coordinate between horizontal lines in composer
        rectangle.setY(Math.round(newYCoordinate / 10) * 10);
    }
    
    //TODO: make the style not inlined
    public void markNote(){        
        rectangle.setStyle("-fx-stroke: red; -fx-stroke-width: 3;");
    }
    
    //TODO: make the style not inlined
    public void unmarkNote(){
        rectangle.setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
    }
    
    
    public static boolean isOverlapping(int max1, int min1, int max2, int min2) {
        return (max1 >= min2 && max2 >= min1);
    }
    
    public boolean isInRect(Point topLeft, Point bottomRight) {
        
        int noteXMax = this.getX() + this.getWidth();
        int noteXMin = this.getX();
        int noteYMax = this.getY();
        int noteYMin = this.getY() - this.boxHeight;
        
        int selectBoxXMax = bottomRight.x;
        int selectBoxXMin = topLeft.x;
        int selectBoxYMax = topLeft.y;
        int selectBoxYMin = bottomRight.y;
        
        boolean isYOverlapping = isOverlapping(noteYMax, noteYMin, selectBoxYMax, selectBoxYMin);
        boolean isXOverlapping = isOverlapping(noteXMax, noteXMin, selectBoxXMax, selectBoxXMin);
        
        return isYOverlapping && isXOverlapping;
        
    }
    
}
