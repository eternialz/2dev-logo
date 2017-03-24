# 2DEV - LOGO Turtle

Projet dont le but est d'interpréter le langage LOGO qui permet de dessiner à l'aide d'un curseur et de commandes.

## Technologies

Java et JavaFX

## Conventions

camelCase

## Classes & Methods & Functions:

```
Interpreter

    attributs:
        private Cursor turtle
        private javafx.scene.canvas.Canvas drawingDisplay
        private javafx.scene.canvas.Canvas cursorDisplay
        private javafx.scene.canvas.GraphicsContext drawingDisplay
        private javafx.scene.canvas.GraphicsContext cursorDisplay

    public void av(int distance) //avancer de "distance"
    public void re(int distance) //reculer de "distance"
    private void moveDistance(int distance, int angle) // bouger dans une direction
    public void td(int angle) //rotation de "angle" vers la droite
    public void tg(int angle) //rotation de "angle" vers la gauche
    public void fcc(string color) //change la couleur du tracé en "color"
    public void lc() //lever le curseur
    public void bc() //baisser le curseur
    public void ct() //cacher le curseur
    public void mt() //afficher le curseur
    public void repete(int times, string commands) //repeter "times" fois "commands"
    public void ve() //reset

    public void refresh() // Actualise l'affichage

    public String input() // Fonction pour gérer les inputs de l'utilisateur
    public Table<string> parser(string commands) // Sépare une chaines de caractères en sous chaînes correspondant à une commande LOGO

Cursor

    attributs:
        private Boolean Hidden
        private Boolean Down

        private Int x
        private Int y
        private Int angle
        private String color

    public Cursor(int x, int y) {
     	this.x = x;
     	this.y = y;
     	this.angle = 90;
     	this.color = "#000";
    	this.hidden = false;
    	this.down = true;
    }

    public int getX()
    public void moveX(Int x)
    public int getY()
    public void moveY(Int y)
    public int getAngle()
    public void turn(Int angle)
    public String getColor()
    public void setColor(String color)
    public Boolean isHidden()
    public void toggleHide()
    public Boolean isDown()
    public void toggleDown()
```
