/*
 * CS 300-A, 2017S
 */
package scaleplayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This JavaFX app lets the user play scales.
 * @author Janet Davis 
 * @author AOLUTION - PROJECT 1
 * @since January 26, 2017
 */
public class ScalePlayer extends Application {
    
    private static final int[] scale = {0, 2, 4, 5, 7, 9, 11, 12};
    private static final MidiPlayer player = new MidiPlayer(1,60);
    
    /**
     * Play a new scale, after stopping and clearing any previous scale.
     * @param startingPitch An integer between 0 115.
     */
    protected void playScale(int startingPitch) {
        player.stop();
        player.clear();
        for (int i=0; i < 8; i++) {
            player.addNote(startingPitch+scale[i], 127, i, 1, 0, 0);
            player.addNote(startingPitch+scale[i], 127, 16-i, 1, 0, 0);
        }
        player.play();
    }

    
    /**
     * Construct the scene and start the application.
     * @param primaryStage the stage for the main window
     */
    @Override
    public void start(Stage primaryStage) {

        Button startButton = new Button("Play scale");
        startButton.setStyle("-fx-background-color: lightgreen;");

        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{
                                             0.0, 0.0,
                                             20.0, 10.0,
                                             00.0, 20.0 });
        startButton.setGraphic(triangle);
        
        startButton.setOnAction((ActionEvent event) -> {
            TextInputDialog pitchDialog = new TextInputDialog("60");
            pitchDialog.showAndWait().ifPresent(response -> {
                playScale(Integer.parseInt(response));
            });
        });
        
        Button stopButton = new Button("Stop playing");
        stopButton.setStyle("-fx-background-color: pink;");
        
        Polygon square = new Polygon();
        square.getPoints().addAll(new Double[]{
                                             0.0, 0.0,
                                             0.0, 20.0,
                                             20.0, 20.0,
                                             20.0, 0.0});        
        stopButton.setGraphic(square);
        stopButton.setOnAction((ActionEvent event) -> {
            player.stop();
        });
        
        HBox hbox = new HBox(8);
        hbox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(startButton, stopButton);
        
        
        Menu menuFile = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });        
        menuFile.getItems().addAll(exit);
        
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuFile);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(hbox);
        
        Scene scene = new Scene(root, 300, 250);
        
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
