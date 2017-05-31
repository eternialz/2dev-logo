package fr.deviantsquad.suplogo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import fr.deviantsquad.suplogo.cursor.Cursor;
import fr.deviantsquad.suplogo.interpreter.Interpreter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Suplogo extends Application {

    @Override
    public void start(Stage primaryStage) {

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 800, 850);

        Canvas drawingCanvas = new Canvas(800, 800);
        Canvas cursorCanvas = new Canvas(800, 800);
        final TextField input = new TextField();

        input.setLayoutX(0);
        input.setTranslateY(400);

        root.getChildren().add(drawingCanvas);
        root.getChildren().add(cursorCanvas);
        root.getChildren().add(input);

        Interpreter interpreter = new Interpreter(new Cursor(400.0, 400.0), drawingCanvas, cursorCanvas);

        primaryStage.setTitle("SupLogo");
        primaryStage.setScene(scene);
        primaryStage.show();

        interpreter.td(20);
        interpreter.av(20);
        interpreter.refresh(); // Set color and refresh the cursor canvas
        interpreter.td(20);
        interpreter.av(20);
        interpreter.refresh(); // Set color and refresh the cursor canvas
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
