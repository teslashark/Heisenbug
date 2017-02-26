/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author lazarcl
 */
public class PlayBar {
    
    /**
     * a reference to the red line that will move across the screen
     */
    private Line playLine; 
    
    /**
     * animation timeline for playBar
     */
    private Timeline timeline;
    
    /**
     * sets the speed that the bar will move. units are pixels per second
     */
    private final float movementSpeed = 100;
    
    /**
     * pane that playLine live in
     */
    private Pane parentPane;
    
    /**
     * creates line and sets it to playLine field. sets line as invisible until animate is called
     * @param pane 
     */
    PlayBar(Pane pane) {
        parentPane = pane;
        createPlayLine();
        timeline = new Timeline();
    }
    
    private void createPlayLine() {
        playLine = new Line(0, 0, 0, 1280);
        playLine.setStrokeWidth(1);
        playLine.setStroke(Color.RED);
        parentPane.getChildren().add(playLine);
        playLine.setVisible(false);
    }
    
    /**
     * moves the line across the screen at the speed set by movementSpeed, disappears at end of last note displayed
     * @param noteList list of rectangles that visually represent notes on the screen
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
        //create starting and ending keyframes for animation.
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue (playLine.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(endCoordinate*10), onFinished, new KeyValue (playLine.translateXProperty(), endCoordinate));
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.playFromStart();
    }
    
    /**
     * stop animation when called
     */
    public void stopAnimation() {
        playLine.setVisible(false);
        timeline.stop();
    }
    
    /**
     * returns the x cord of the right side of the last note in our list of notes
     */
    private int findEndCoordinate(ArrayList<Rectangle> noteList) {
        int largestXValue = 0;
        for (Rectangle rect: noteList) {
            if (rect.getX() > largestXValue) 
                largestXValue = (int) rect.getX(); // add 100 to x cord b/c rectangle is 100 pixels wide
        }
        return largestXValue + 100;
    } 
}
