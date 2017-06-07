package fr.deviantsquad.suplogo;

import fr.deviantsquad.suplogo.cursor.Cursor;
import fr.deviantsquad.suplogo.interpreter.Interpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Suplogo extends Application {

    @Override
    public void start(Stage primaryStage) {

    	BorderPane root = new BorderPane();
    	StackPane center = new StackPane();

        Scene scene = new Scene(root, 800, 880);

        Canvas drawingCanvas = new Canvas(800, 800);
        Canvas cursorCanvas = new Canvas(800, 800);
        final TextField input = new TextField();

        center.getChildren().add(drawingCanvas);
        center.getChildren().add(cursorCanvas);
        root.setBottom(input);
        root.setCenter(center);

        Interpreter interpreter = new Interpreter(new Cursor(400.0, 400.0), drawingCanvas, cursorCanvas, input);

        primaryStage.setMinHeight(880);
        primaryStage.setMinWidth(800);
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
