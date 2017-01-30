/*
 * CS 300-A, 2017S
 */
package scaleplayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This JavaFX app lets the user play scales.
 * @author Janet Davis 
 * @author AOLUTION - PROJECT 1
 * @since January 26, 2017
 */
public class ScalePlayer extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        Button startButton = new Button();
        startButton.setText("Play scale");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start");
            }
        });
        
        Button stopButton = new Button();
        stopButton.setText("Stop playing");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Stop");
            }
        });
        
        HBox hbox = new HBox(8);
        hbox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(startButton, stopButton);
        
        
        Menu menuFile = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
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
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
