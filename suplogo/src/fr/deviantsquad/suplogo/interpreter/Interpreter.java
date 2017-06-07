package fr.deviantsquad.suplogo.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deviantsquad.suplogo.cursor.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Interpreter
{
    private Cursor cursor;
    private Canvas drawingDisplay;
    private GraphicsContext drawingContext;
    private Canvas cursorDisplay;
    private GraphicsContext cursorContext;
    private List<String> history = new ArrayList<String>();
    private int historyIndex;

    public Interpreter(Cursor cursor, Canvas drawingDisplay, Canvas cursorDisplay, TextField input)
    {
        this.cursor = cursor;
        this.drawingDisplay = drawingDisplay;
        this.cursorDisplay = cursorDisplay;
        this.drawingContext = drawingDisplay.getGraphicsContext2D();
        this.cursorContext = cursorDisplay.getGraphicsContext2D();

        // Default cursorCanvas settings
        this.cursorContext.setLineWidth(3);
        this.cursorContext.setFill(Color.web("#66BB66"));

        // Default drawingCanvas settings
        this.drawingContext.setLineWidth(2);

        // Add an event to execute the commands when pressing ENTER
        input.setOnAction(e ->
        {
            String command = input.getText();
            history.add(command);
            historyIndex = history.size();
            parser(command);

            input.clear();
        });

        input.setOnKeyPressed(e ->
        {
            if(e.getCode() == KeyCode.UP && historyIndex > 0)
            {
                historyIndex--;
                input.setText(history.get(historyIndex));
            }
            else if(e.getCode() == KeyCode.DOWN && historyIndex < history.size())
            {
                historyIndex++;
                if(historyIndex == history.size())
                {
                    input.setText("");
                }
                else
                {
                    input.setText(history.get(historyIndex));
                }
            }

        });
    }

    // sépare l'input
    private void parser(String commands)
    {
        // Create an ArrayList of all distinct command in commands
        ArrayList<String> results = new ArrayList<>();
        int index = -1;
        String[] splitResults;
        Pattern isChar = Pattern.compile("[a-zA-Z]");
        Matcher matcher;
        int counter = 0;// Count different commands
        int counterChar = 0;
        while(commands.length() > 0) // While there's something to parse
        {
            splitResults = commands.split(" ", 2); // want only the first word
            matcher = isChar.matcher(String.valueOf(splitResults[0].charAt(0)));
            if(matcher.find())
            {
                // New command
                index++;
                results.add(splitResults[0]);
                if(commands.equals(splitResults[0]))
                {
                    commands = "";
                }
                else
                {
                    commands = splitResults[1];
                }
            }
            else
            {
                if(splitResults[0].charAt(0) == '[')
                {
                    // Repete case, we add all characters until we find corresponing
                    // closed brackets
                    results.set(index, results.get(index) + " ");
                    commands = String.join(" ", splitResults);
                    for(int i = 0; i < commands.length(); i++)
                    {
                        // Add character one by one
                        if(commands.charAt(i) == '[')
                        {
                            counter++;
                        }
                        else if(commands.charAt(i) == ']')
                        {
                            counter--;
                        }
                        results.set(index, results.get(index) + commands.charAt(i));
                        counterChar++;
                        if(counter == 0)
                        {
                            break;
                        }
                    }
                    // Avoid out of range index
                    if(commands.length() > counterChar + 2)
                    {
                        commands = commands.substring(counterChar + 1);
                    }
                    else
                    {
                        commands = "";
                    }
                }
                else
                {
                    // if end of commands splitResults[1] is null
                    results.set(index, results.get(index) + " " + splitResults[0]);
                    if(splitResults[0].equals(commands))
                    {
                        commands = "";
                    }
                    else
                    {
                        commands = splitResults[1];
                    }
                }
            }
        }
        execute(results);

    }

    private void execute(ArrayList<String> commands)
    {
        // Execute the corresponding commands

        Method methods[]; // List of all the possible methods
        Method method = null; // The method to execute
        String[] input;
        String methodName;
        String argument = null;

        // Try to find the method corresponding to the command
        for(int i = 0; i < commands.size(); i++)
        {
            input = commands.get(i).split(" ");
            if(input.length > 2)
            {
                repete(Integer.parseInt(input[1]), commands.get(i).split(" ", 3)[2]);
                continue;
            }
            else if(input.length == 2)
            {
                methodName = input[0].toLowerCase();
                argument = input[1].toLowerCase();
            }
            else
            {
                methodName = input[0].toLowerCase();
            }

            try
            {
                methods = this.getClass().getDeclaredMethods(); // Get all methods
                for(int x = 0; x < methods.length; x++)
                {
                    if(methods[x].getName().equals(methodName))
                    { // If the method exists
                        method = methods[x]; // Set the method to execute
                    }
                }
            }
            catch(SecurityException e)
            {
                e.printStackTrace();
            }

            // Try to execute the method found
            try
            {
                if(method != null) // If the method has been found
                {
                    if(argument != null)
                    {
                        method.invoke(this, argument); // execute the command
                    }
                    else
                    {
                        method.invoke(this);
                    }

                    // TimeUnit.MILLISECONDS.sleep(50); // Wait 200 milliseconds to give an effect of step by step
                    this.drawingContext.save();
                    this.drawingContext.restore();
                    refresh();
                }
            }
            catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void refresh()
    {
        this.cursorContext.clearRect(0, 0, 800, 800); // Clear cursor canvas
        this.drawingContext.setStroke(Color.web(this.cursor.getColor())); // Set color

        if(!this.cursor.isHidden())
        {
            displayCursor();
        }
    }

    /**
     * Cette fonction affiche le curseur
     */
    private void displayCursor()
    {

        double rotationCenterX = this.cursor.getX();
        double rotationCenterY = this.cursor.getY();

        this.cursorContext.save();
        // Translate the context to set the rotation point
        this.cursorContext.translate(rotationCenterX, rotationCenterY);
        // Rotate the context
        this.cursorContext.rotate(45 + this.cursor.getAngle());

        this.cursorContext.translate(-rotationCenterX, -rotationCenterY);

        // Create a rect in a rotated context to create a rotated rect
        this.cursorContext.fillRect(rotationCenterX - 3, rotationCenterY - 3, 6, 6);

        // Restore the context but keep the rotated rect
        this.cursorContext.restore();
    }

    public void av(String distance) // Move front
    {
        moveDistance(Integer.parseInt(distance), this.cursor.getAngle());
    }

    public void re(String distance) // Move back
    {
        moveDistance(Integer.parseInt(distance), (0 - this.cursor.getAngle()));
    }

    private void moveDistance(int distance, int angle)
    {
        // Use the trigonometry to get the distance to move for each axys (x and y)
        double moveX = Math.cos(Math.toRadians(angle)) * distance;
        double moveY = Math.sin(Math.toRadians(angle)) * distance;

        // If the cursor is down
        if(this.cursor.isDown())
        {
            // Create the line from the cursor position to the cursor postion + the distance to move
            this.drawingContext.strokeLine(this.cursor.getX(), this.cursor.getY(), this.cursor.getX() + moveX, this.cursor.getY() + moveY);
        }

        // Move the position of the cursor to the end of the new line
        this.cursor.moveX(moveX);
        this.cursor.moveY(moveY);
    }

    public void td(String angle) // Turn
    {
        this.cursor.turn(0 - Integer.parseInt(angle));
    }

    public void tg(String angle) // Turn
    {
        this.cursor.turn(Integer.parseInt(angle));
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
        commands = commands.substring(1, commands.length() - 1);

        for(int i = 0; i <= times; i++)
        {
            parser(commands);
        }
    }

    public void ve() // Clear drawing canvas
    {
        this.drawingContext.clearRect(0, 0, 800, 800);
    }
}
