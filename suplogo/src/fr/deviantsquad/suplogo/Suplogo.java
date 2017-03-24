package fr.deviantsquad.suplogo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        Scene scene = new Scene(root, 800, 800);

        Canvas drawingCanvas = new Canvas(800, 600);
        Canvas cursorCanvas = new Canvas(800, 600);

        root.getChildren().add(drawingCanvas);
        root.getChildren().add(cursorCanvas);

        Interpreter interpreter = new Interpreter(new Cursor(0,0), drawingCanvas, cursorCanvas);

        primaryStage.setTitle("SupLogo");
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
