/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
     * creates line and sets it to playLine field. sets line as invisible until animate is called
     * @param pane 
     */
    PlayBar(Pane pane) {
        playLine = new Line(0, 0, 0, 1280);
        playLine.setStrokeWidth(1);
        playLine.setStroke(Color.RED);
        pane.getChildren().add(playLine);
        playLine.setVisible(false);
        timeline = new Timeline();
    }
    
    /**
     * moves the line across the screen at the speed set by movementSpeed, disappears at end of last note displayed
     * @param noteList list of rectangles that visually represent notes on the screen
     */
    public void playAnimation(ArrayList noteList) {
        playLine.setVisible(true);
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        //create starting and ending keyframes for animation.
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue (playLine.translateXProperty(), 0));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(2000), new KeyValue (playLine.translateXProperty(), findEndCordinate(noteList)));
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.play();
    }
    
    /**
     * test method for playAnimation
     */
    public void playAnimation() {
        playLine.setVisible(true);
        timeline = new Timeline();
        timeline.setCycleCount(1);
        //create starting and ending keyframes for animation.
        KeyFrame keyFrame1 = new KeyFrame(Duration.ZERO, new KeyValue (playLine.translateXProperty(), 0));
//        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(2000), new KeyValue (playLine.translateXProperty(), 200));
        KeyFrame keyFrame2;
        keyFrame2 = new KeyFrame(Duration.millis(6000),new KeyValue (playLine.translateXProperty(), 200));
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.play();
    }
    
    /**
     * stop animation when called
     */
    public void stopAnimation() {
        timeline.stop();
    }
    
    /**
     * returns the x cord of the right side of the last note in our list of notes
     */
    private int findEndCordinate(ArrayList<Rectangle> noteList) {
        int largestXValue = 0;
        for (Rectangle rect: noteList) {
            if (rect.getX() > largestXValue) 
                largestXValue = (int) rect.getX() + 100; // add 100 to x cord b/c rectangle is 100 pixels wide
        }
        return largestXValue;
    } 
}
