/*
 * CS 300-A, 2017S
 */
package scaleplayer;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This JavaFX app lets the user play scales.
 * @author Janet Davis 
 * @author AOLUTION - PROJECT 1
 * @since January 26, 2017
 */
public class ScalePlayer extends Application {
    
    /**
     * Represents the number of pitch steps for 
     * do, re, mi, fa, so, la, ti, do.
     * Source: https://en.wikipedia.org/wiki/Solfège
     */
    private static final int[] SCALE = {0, 2, 4, 5, 7, 9, 11, 12};
    
    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    private final MidiPlayer player;

    public ScalePlayer() {
        this.player = new MidiPlayer(1,60);
    }
    
    /**
     * Play a new scale, after stopping and clearing any previous scale.
     * @param startingPitch An integer between 0 115.
     */
    protected void playScale(int startingPitch) {
        player.stop();
        player.clear();
        for (int i=0; i < 8; i++) {
            player.addNote(startingPitch+SCALE[i], 127, i, 1, 0, 0);
            player.addNote(startingPitch+SCALE[i], 127, 16-i, 1, 0, 0);
        }
        player.play();
    }
    
    @FXML 
    protected void handlePlayScaleButtonAction(ActionEvent event) {
        TextInputDialog pitchDialog = new TextInputDialog("60");
        pitchDialog.setHeaderText("Give me a starting note (0-115):");
            pitchDialog.showAndWait().ifPresent(response -> {
                playScale(Integer.parseInt(response));
            });
    }    
    
    @FXML 
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        player.stop();
    }    
    
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     */
    @Override
    public void start(Stage primaryStage) throws IOException {                
        Parent root = FXMLLoader.load(getClass().getResource("ScalePlayer.fxml"));
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Scale Player");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });        
        primaryStage.show();
    }

    /**
     * Launch the application.
     * @param args the command line arguments are ignored
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
