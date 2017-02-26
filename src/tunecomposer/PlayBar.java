package tunecomposer;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * PlayBar class which creates the animation of a red bar that indicates what notes are playing.
 * @author lazarcl
 */
public class PlayBar {
    
    /**
     * A reference to the red line that will move across the screen.
     */
    private Line playLine; 
    
    /**
     * Animation timeline for playBar.
     */
    private Timeline timeline;
    
    /**
     * Sets the speed that the bar will move. Units are pixels per second.
     */
    private final float movementSpeed = 100;
    
    /**
     * Pane that playLine live in.
     */
    private Pane parentPane;
    
    /**
     * Creates line and sets it to playLine field. 
     * Sets line as invisible until animate is called.
     * @param pane the pane the playBar and timeline are on
     */
    PlayBar(Pane pane) {
        parentPane = pane;
        createPlayLine();
        timeline = new Timeline();
    }
    
    /**
     * Creates the playLine object, styles it, adds it to the main pane,
     * and sets it's initial state to invisible.
     */    
    private void createPlayLine() {
        playLine = new Line(0, 0, 0, 1280);
        playLine.setStrokeWidth(1);
        playLine.setStroke(Color.RED);
        parentPane.getChildren().add(playLine);
        playLine.setVisible(false);
    }
    
    /**
     * Moves the line across the screen at the speed set by 
     * movementSpeed, disappears at end of last note displayed.
     * @param noteList List of rectangles that visually represent notes on the screen
     */
    public void playAnimation(ArrayList noteList) {
        playLine.setVisible(true);
        int endCoordinate = findEndCoordinate(noteList);
        
        timeline.getKeyFrames().clear();
        
        EventHandler onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    playLine.setVisible(false);
            }
            
        };
        
        timeline.setCycleCount(1);
        // create starting and ending keyframes for animation.
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue (playLine.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(endCoordinate*10), onFinished, new KeyValue (playLine.translateXProperty(), endCoordinate));
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.playFromStart();
    }
    
    /**
     * Stop the playLine animation and make it disappear.
     */
    public void stopAnimation() {
        timeline.stop();
        playLine.setVisible(false);
    }
    
    /**
     * Returns the x cord of the right side of 
     * the last note in our list of notes.
     */
    private int findEndCoordinate(ArrayList<Rectangle> noteList) {
        int largestXValue = 0;
        for (Rectangle rect: noteList) {
            if (rect.getX() > largestXValue) 
                largestXValue = (int) rect.getX();
        }
        return largestXValue + 100;
    } 
}
