package fr.deviantsquad.suplogo.interpreter;

import fr.deviantsquad.suplogo.cursor.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.lang.*;

public class Interpreter {
    private Cursor cursor;
    private Canvas drawingDisplay;
    private GraphicsContext drawingContext;
    private Canvas cursorDisplay;
    private GraphicsContext cursorContext;
    private TextField input;

    public Interpreter(Cursor cursor, Canvas drawingDisplay, Canvas cursorDisplay, TextField input) {
        this.cursor = cursor;
        this.drawingDisplay = drawingDisplay;
        this.cursorDisplay = cursorDisplay;
        this.drawingContext = drawingDisplay.getGraphicsContext2D();
        this.cursorContext = cursorDisplay.getGraphicsContext2D();
        this.input = input;

        //Default cursorCanvas settings
        this.cursorContext.setLineWidth(3);
        this.cursorContext.setFill(Color.web("#66BB66"));

        //Default drawingCanvas settings
        this.drawingContext.setLineWidth(2);
    }

    public void input()
    {
        //récupère l'input
    }

    private void parser(String commands)
    {
        //sépare l'input
    }

    private void execute()
    {
        //exécute les commandes correspondantes à chacune dans le tableau
    }

    public void refresh()
    {
        this.cursorContext.clearRect(0, 0, 800, 800); // Clear cursor canvas
        this.drawingContext.setStroke(Color.web(this.cursor.getColor())); // Set color

        if (!this.cursor.isHidden()) {
            displayCursor();
        }
    }

    private void displayCursor() {
        double rotationCenterX = this.cursor.getX();
        double rotationCenterY = this.cursor.getY();

        this.cursorContext.save();
        this.cursorContext.translate(rotationCenterX, rotationCenterY);
        this.cursorContext.rotate(45 + this.cursor.getAngle());
        this.cursorContext.translate(-rotationCenterX, -rotationCenterY);

        this.cursorContext.fillRect(
            rotationCenterX - 3,
            rotationCenterY - 3,
            6,
            6
        );

        this.cursorContext.restore();
    }

    public void av(int distance) // Move front
    {
        moveDistance(distance, this.cursor.getAngle());
    }

    public void re(int distance) // Move back
    {
        moveDistance(distance, ( 0 - this.cursor.getAngle()));
    }

    private void moveDistance(int distance, int angle)
    {
        double moveX = Math.cos(Math.toRadians(angle)) * distance;
        double moveY = Math.sin(Math.toRadians(angle)) * distance;

        if (this.cursor.isDown()) {
            this.drawingContext.strokeLine(
                this.cursor.getX(),
                this.cursor.getY(),
                this.cursor.getX() + moveX,
                this.cursor.getY() + moveY
            );
        }

        this.cursor.moveX(moveX);
        this.cursor.moveY(moveY);
    }

    public void td(int angle) // Turn
    {
        this.cursor.turn(0 - angle);
    }

    public void tg(int angle) // Turn
    {
        this.cursor.turn(angle);
    }

    public void fcc(String color) // Set color
    {
        this.cursor.setColor(color);
    }

    public void lc() // Set cursor up -> don't draw
    {
        this.cursor.setDown(false);
    }

    public void bc() // Set cursor down -> draw
    {
        this.cursor.setDown(true);
    }

    public void ct() // Hide cursor
    {
        this.cursor.setHide(true);
    }

    public void mt() // Show cursor
    {
        this.cursor.setHide(false);
    }

    public void repete(int times, String commands) // Repeat commands x times
    {
        for(int i = 0; i<= times; i++)
        {
            parser(commands);
        }
    }

    public void ve() // Clear drawing canvas
    {
        this.drawingContext.clearRect(0, 0, 800, 800);
    }
}
