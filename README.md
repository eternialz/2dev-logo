# 2DEV - LOGO Turtle

Projet dont le but est d'interpréter le langage LOGO qui permet de dessiner à l'aide d'un curseur et de commandes.

## Technologies

Java et JavaFX

## Conventions

camelCase

## Classes & Methods & Functions:

```
Logo
|
|   attributs:
|   |   Cursor turtle
|   |   javafx.scene.canvas.Canvas drawingDisplay
|   |   javafx.scene.canvas.Canvas cursorDisplay
|
|   Void av(int distance) //avancer de "distance"
|   Void re(int distance) //reculer de "distance"
|   Void td(int angle) //rotation de "angle" vers la droite
|   Void tg(int angle) //rotation de "angle" vers la gauche
|   Void fcc(string color) //change la couleur du tracé en "color"
|   Void lc() //lever le curseur
|   Void bc() //baisser le curseur
|   Void ct() //cacher le curseur
|   Void mt() //afficher le curseur
|   Void repete(int times, string commands) //repeter "times" fois "commands"
|   Void ve() //reset
|
|   Void execute(Table<string> commands) // Execute une par une les commandes correspondantes à chacune des chaînes du tableau "commands"
|   Void refresh() // Actualise l'affichage
|
|   String input() // Fonction pour gérer les inputs de l'utilisateur
|   Table<string> parser(string commands) // Sépare une chaines de caractères en sous chaînes correspondant à une commande LOGO

Cursor
|
|   attributs:
|   |   private Boolean Hidden
|   |   private Boolean Down
|   |
|   |   private Int x
|   |   private Int y
|   |   private Int angle
|   |   private String color
|
|   public Void setColor(String color)
|   public Void turn(Int angle)
|   public Void moveX(Int x)
|   public Void moveY(Int y)
|   public Void toggleDown()
|   public Void toggleHide()
|   public Boolean isDown?()
|   public Boolean isHidden?()
|
|   private Void setAngle(Int angle)
|   private Void setX(int x)
|   private Void setY(int y)
```
