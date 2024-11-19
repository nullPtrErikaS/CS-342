import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Setup your JavaFX application here
        primaryStage.setTitle("Blackjack Game");

        // Show your primary scene
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
