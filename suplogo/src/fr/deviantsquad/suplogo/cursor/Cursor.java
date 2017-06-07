package fr.deviantsquad.suplogo.cursor;

import static java.lang.Math.abs;

public class Cursor {
    private double x;
    private double y;
    private int angle;
    private String color;
    private Boolean hidden;
    private Boolean down;

    /**
    * Default Cursor constructor
    */
    public Cursor(double x, double y) {
    	this.x = x;
    	this.y = y;
    	this.angle = 90;
    	this.color = "#000";
    	this.hidden = false;
    	this.down = true;
    }

    public double getX() {
    	return this.x;
    }

    /*
    private void setX(int x) {
    	this.x = x;
    }
    */

    public void moveX(double x) {
        this.x += x;
    }

    public double getY() {
    	return this.y;
    }

    /*
    private void setY(int y) {
    	this.y = y;
    }
    */

    public void moveY(double y) {
        this.y += y;
    }

    public int getAngle() {
    	return this.angle;
    }

    public void turn(int angle) {
    	this.angle = (this.angle + angle) % 360;
    }

    public String getColor() {
    	return this.color;
    }

    public void setColor(String color) {
    	this.color = color;
    }

    public Boolean isHidden() {
    	return this.hidden;
    }

    public void setHide(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isDown() {
    	return this.down;
    }

    public void setDown(Boolean down) {
        this.down = down;
    }
}
