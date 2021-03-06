package chess.androchess;

import java.io.Serializable;

/**
 * Created by Brian on 12/9/2017.
 */

public abstract class Piece implements Serializable {
    public boolean eating = false;
    public boolean movedYet = false;
    public boolean checkingKing = false;
    public int xpos;
    public int deltaX;
    public int ypos;
    public int deltaY;
    public char type;
    public char color;
    boolean enpassantReady = false;

    public void setColor(String c){
        if (c.equals("black")){
            this.color = 'b';
        }
        else if (c.equals("white")){
            this.color = 'w';
        }
    }
    public char getColor() {
        return this.color;
    }
    public char getType() {
        return this.type;
    }
    public int delta(int oldX, int newX) {
        return Math.abs(oldX-newX);
    }
    public String toString(){
        String name = Character.toString(this.color)+Character.toString(this.type);
        return name;
    }

    public abstract boolean validMove(int oldX, int oldY, int newX, int newY);

}