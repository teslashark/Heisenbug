
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

/**
 *
 * @author pablofernandez
 */
public class Items {
     
    public boolean isSelected;
    
    public Rectangle stretchZone;
    
    public Rectangle dragZone;
    
    /**
     * the default width of a newly created Item
     */
    private final int initialBoxWidth = 100;
    
    /**
     * The height of an Item
     */
    private final int boxHeight = 10;
    
    /**
     * The rectangle to visually represent the Item
     */
    public Rectangle rectangle;
    
    public Items(MouseEvent event){
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
        
        stretchZone = new Rectangle(this.getX() + (this.getWidth() - 5),this.getY(),5, this.getBoxHeight());
        dragZone = new Rectangle(this.getX(), this.getY(),this.getWidth()-5, this.getBoxHeight());
        
    }
    
    /**
     * Returns a rectangle representative of the area of the Item which can be clicked on to
     * activate the resizing action.
     * @return The rectangle representing the place to click to resize the Item
     */
    public Rectangle getStretchZone() {
        return stretchZone;
    }
    
    /**
     * Returns a rectangle representative of the area of the Item which can be clicked on to
     * activate the drag to move action.
     * @return The rectangle representing the place to click to drag and move the Item
     */
    public Rectangle getDragZone() {
        return dragZone;
    }
    
    /**
     * Get method for the rectangle
     * @return the rectangle representing the Item
     */
    public Rectangle getRectangle() {
        return rectangle;
    }
    
    /**
     * Gives the x-coordinate of the Item
     * @return the x-coordinate
     */
    public int getX() {
        return (int) this.rectangle.getX();
    }
    
    /**
     * Gives the y-Coordinate of the Item
     * @return the y-coordinate
     */
    public int getY() {
        return (int) this.rectangle.getY();
    }
    
    /**
     * Gives the height of the Item
     * @return the height of the Item
     */
    public int getBoxHeight() {
        return boxHeight;
    }
    
    /**
     * Gives the width of the Item
     * @return the width of the Item
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
}

