package fr.deviantsquad.suplogo.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.deviantsquad.suplogo.cursor.Cursor;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Interpreter
{
    private Stage stage;
    private Cursor cursor;
    private Canvas drawingDisplay;
    private GraphicsContext drawingContext;
    private Canvas cursorDisplay;
    private GraphicsContext cursorContext;
    private TextField input;
    private List<String> history = new ArrayList<String>();
    private int historyIndex;
    private long speed = 50;

    public Interpreter(Stage stage, Cursor cursor, Canvas drawingDisplay, Canvas cursorDisplay, TextField input)
    {
        this.stage = stage;
        this.cursor = cursor;
        this.drawingDisplay = drawingDisplay;
        this.cursorDisplay = cursorDisplay;
        this.drawingContext = drawingDisplay.getGraphicsContext2D();
        this.cursorContext = cursorDisplay.getGraphicsContext2D();
        this.input = input;

        // Default cursorCanvas settings
        this.cursorContext.setLineWidth(3);
        this.cursorContext.setFill(Color.web("#66BB66"));

        // Default drawingCanvas settings
        this.drawingContext.setLineWidth(2);

        // Add an event to execute the commands when pressing ENTER
        input.setOnAction(e ->
        {
            String command = input.getText();
            this.history.add(command);
            this.historyIndex = this.history.size();
            command = command.replaceAll("( )+", " ").trim();
            parser(command);

            input.clear();
        });

        input.setOnKeyPressed(e ->
        {
            if(e.getCode() == KeyCode.UP && this.historyIndex > 0)
            {
                this.historyIndex--;
                input.setText(this.history.get(this.historyIndex));
            }
            else if(e.getCode() == KeyCode.DOWN && this.historyIndex < this.history.size())
            {
                this.historyIndex++;
                if(this.historyIndex == this.history.size())
                {
                    input.setText("");
                }
                else
                {
                    input.setText(this.history.get(this.historyIndex));
                }
            }
        });
    }

    private void showErrorPopup()
    {
        Text txt = new Text("Erreur dans la commande");
        txt.setFill(Color.RED);

        HBox box = new HBox(txt);
        box.setPadding(new Insets(5));
        box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), new Insets(0))));
        box.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(0), BorderWidths.DEFAULT)));
        box.setOpacity(0.0D);

        Popup p = new Popup();
        p.getContent().add(box);
        p.setAutoHide(true);
        Point2D pt = this.input.localToScreen(0, 0);
        p.setX(pt.getX());
        p.setY(pt.getY() - this.input.getHeight());
        p.show(this.stage);
        FadeTransition ft = new FadeTransition(Duration.millis(100), box);
        ft.setFromValue(0.0D);
        ft.setToValue(1.0D);
        ft.setOnFinished(ev ->
        {
            PauseTransition ptr = new PauseTransition(Duration.millis(1000));
            ptr.setOnFinished(eve ->
            {
                FadeTransition ftout = new FadeTransition(Duration.millis(100), box);
                ftout.setFromValue(1.0D);
                ftout.setToValue(0.0D);
                ftout.setOnFinished(even ->
                {
                    p.hide();
                });
                ftout.play();
            });
            ptr.play();
        });
        ft.play();
    }

    // Parse the string written inside the TextField
    private void parser(String commands)
    {
        ArrayList<String> results = preParser(commands);

        ArrayList<String> allCommands = convertRepete(results);

        // Run all commands
        Task<Void> task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                final Task<Void> tas = this;
                for(String command : allCommands)
                {
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            execute(command, tas);
                        }
                    });
                    Thread.sleep(Interpreter.this.speed);
                }
                return null;
            }
        };
        // Disabled the input to avoid multiple command running at the same time
        this.input.setDisable(true);
        task.setOnSucceeded(e ->
        {
            this.input.setDisable(false);
        });
        task.setOnFailed(e ->
        {
            this.input.setDisable(false);
        });
        // The task is cancelled when we encounter an error
        task.setOnCancelled(e ->
        {
            this.input.setDisable(false);
            showErrorPopup();
        });
        // Start the task
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    /*
     * Pre parser, split a sigle string containing one or more commands into a list of separate commands
     */
    private ArrayList<String> preParser(String commands)
    {
        // Create an ArrayList of all distinct command in commands
        ArrayList<String> results = new ArrayList<>();
        int index = -1;
        String[] splitResults;
        Pattern isChar = Pattern.compile("[a-zA-Z]");
        Matcher matcher;
        int counter;// Count different commands
        int counterChar;
        while(commands.length() > 0) // While there's something to parse
        {
            counterChar = 0;
            counter = 0;
            splitResults = commands.split(" ", 2);// want only the first word
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
                    // Repete case, we add all characters until we find corresponding
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
                    if(commands.length() > counterChar + 1)
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
                    try
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
                    catch(Exception e)
                    {
                        showErrorPopup();
                        break;
                    }
                }
            }
        }
        return results;
    }

    /*
     * Convert a all repete command into multiple basic commands
     */
    private ArrayList<String> convertRepete(ArrayList<String> commands)
    {
        ArrayList<String> result = new ArrayList<String>();
        String[] parts;
        for(String command : commands)
        {
            parts = command.split(" ");
            if(parts.length > 2)
            {
                result.addAll(repete(Integer.parseInt(parts[1]), command.split(" ", 3)[2]));
            }
            else
            {
                result.add(command);
            }
        }
        return result;
    }

    /*
     * Convert a single repeat command into mutltiple basic commands
     */
    public ArrayList<String> repete(int times, String commands) // Repeat commands x times
    {
        commands = commands.substring(commands.indexOf("[") + 1).substring(0, commands.lastIndexOf("]") - 1);

        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i <= times; i++)
        {
            result.addAll(preParser(commands));
        }
        return convertRepete(result);
    }

    private void execute(String command, Task<Void> task)
    {
        // Execute the corresponding commands

        Method methods[]; // List of all the possible methods
        Method method = null; // The method to execute
        String[] input;
        String methodName = null;
        String argument = null;

        input = command.split(" ");
        if(input.length > 2)
        {
            // repete(Integer.parseInt(input[1]), command.split(" ", 3)[2]);
            return;
        }
        else if(input.length == 2)
        {
            methodName = input[0].toLowerCase();
            argument = input[1].toLowerCase();
        }
        else
        {
            methodName = input[0].toLowerCase();
            argument = null;
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
            task.cancel();
        }

        // Try to execute the method found
        try
        {
            if(method != null) // If the method has been found
            {
                if(argument != null)
                {
                    method.invoke(Interpreter.this, argument); // execute the command
                }
                else
                {
                    method.invoke(Interpreter.this);
                }

                Interpreter.this.refresh();
            }
            else
            {
                task.cancel();
            }
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
            task.cancel();
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
        moveDistance(Integer.parseInt(distance), (180 + this.cursor.getAngle()%360));
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
        this.cursor.turn(Integer.parseInt(angle));
    }

    public void tg(String angle) // Turn
    {
        this.cursor.turn(-Integer.parseInt(angle));
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

    public void ve() // Clear drawing canvas
    {
        this.drawingContext.clearRect(0, 0, 800, 800);
    }

    public void vi(String newspeed)
    {
        long l = Long.parseLong(newspeed);
        if(l < 0)
        {
            this.speed = 0;
        }
        else
        {
            this.speed = l;
        }
    }
}
