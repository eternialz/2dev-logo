package fr.deviantsquad.suplogo;

import fr.deviantsquad.suplogo.cursor.Cursor;
import fr.deviantsquad.suplogo.interpreter.Interpreter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Suplogo extends Application
{
    @Override
    public void start(Stage primaryStage)
    {

        // Add the zones
        BorderPane root = new BorderPane();
        StackPane center = new StackPane();

        Scene scene = new Scene(root, 800, 880);

        scene.getStylesheets().add("fr/deviantsquad/suplogo/css/style.css");

        // Create the drawing canvas
        Canvas drawingCanvas = new Canvas(800, 800);
        // Create the cursor canvas
        Canvas cursorCanvas = new Canvas(800, 800);

        // Create the input of commands
        final TextField input = new TextField();

        // Add the canvas to center
        center.getChildren().add(drawingCanvas);
        center.getChildren().add(cursorCanvas);

        // Add the canvas and input to the stage
        root.setBottom(input);
        root.setCenter(center);

        // Create the interpreter to get and execute commands from the user
        Interpreter interpreter = new Interpreter(primaryStage, new Cursor(400.0, 400.0), drawingCanvas, cursorCanvas, input);

        // Set the windows parameters
        primaryStage.setMinHeight(880);
        primaryStage.setMinWidth(800);
        primaryStage.setTitle("SupLogo");
        primaryStage.getIcons().add(new Image("/suplogo.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        interpreter.refresh();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
