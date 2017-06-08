package fr.deviantsquad.suplogo.cursor;

public class Cursor
{
    private double x;
    private double y;
    private int angle;
    private String color;
    private boolean hidden;
    private boolean down;

    /**
     * Default Cursor constructor
     */
    public Cursor(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.angle = 270; // Default angle (up)
        this.color = "#000"; // Default color;
        this.hidden = false;
        this.down = true;
    }

    public void resetPos()
    {
        this.x = 400;
        this.y = 400;
    }

    public double getX()
    {
        return this.x;
    }

    public void moveX(double x)
    {
        this.x += x;
    }

    public double getY()
    {
        return this.y;
    }

    public void moveY(double y)
    {
        this.y += y;
    }

    public int getAngle()
    {
        return this.angle;
    }

    public void turn(int angle)
    {
        this.angle = (this.angle + angle) % 360;
    }

    public String getColor()
    {
        return this.color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public boolean isHidden()
    {
        return this.hidden;
    }

    public void setHide(boolean hidden)
    {
        this.hidden = hidden;
    }

    public boolean isDown()
    {
        return this.down;
    }

    public void setDown(boolean down)
    {
        this.down = down;
    }
}
