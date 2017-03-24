package fr.deviantsquad.suplogo.interpreter;

import fr.deviantsquad.suplogo.cursor.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Interpreter {
    private Cursor cursor;
    private Canvas drawingDisplay;
    private GraphicsContext drawingContext;
    private Canvas cursorDisplay;
    private GraphicsContext cursorContext;

    public Interpreter(Cursor cursor, Canvas drawingDisplay, Canvas cursorDisplay) {
        this.cursor = cursor;
        this.drawingDisplay = drawingDisplay;
        this.cursorDisplay = cursorDisplay;
        this.drawingContext = drawingDisplay.getGraphicsContext2D();
        this.cursorContext = cursorDisplay.getGraphicsContext2D();
    }

    public void input()
    {

    }

    public void parser()
    {

    }

    public void execute(String command)
    {

    }

    public void refresh()
    {

    }

    public void av(int distance)
    {
        moveDistance(distance, this.cursor.getAngle());
    }

    public void re(int distance)
    {
        moveDistance(distance, ( 0 - this.cursor.getAngle()));
    }

    private void moveDistance(int distance, int angle)
    {

    }

    public void td(int angle)
    {
        this.cursor.turn(0 - angle);
    }

    public void tg(int angle)
    {
        this.cursor.turn(angle);
    }

    public void fcc(String color)
    {
        this.cursor.setColor(color);
    }

    public void lc()
    {
        this.cursor.setDown(false);
    }

    public void bc()
    {
        this.cursor.setDown(true);
    }

    public void ct()
    {
        this.cursor.setHide(true);
    }

    public void mt()
    {
        this.cursor.setHide(false);
    }

    public void repete(int times, String commands)
    {
        for(int i = 0; i<= times; i++)
        {

        }
    }

    public void ve()
    {

    }

}
