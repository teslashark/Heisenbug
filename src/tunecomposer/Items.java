
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
     
    /**
     * returns true if the NoteBox is marked as a selected note
     */
    public boolean isSelected;
    
    public Rectangle stretchZone;
    
    public Rectangle dragZone;
    
    public int getX() {
        return 0;
    }
    
    public int getWidth() {
        return 0;
    }
    
}
